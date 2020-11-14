package com.example.acquatikaapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.DateUtil;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesOrderEditorActivity extends AppCompatActivity {

    public static final String SALES_ORDER_ADD_EXTRA = "SALES_ORDER_ADD_EXTRA";
    public static final String SALES_ORDER_EDIT_EXTRA = "SALES_ORDER_EDIT_EXTRA";

    private static final String TAG = SalesOrderEditorActivity.class.getSimpleName();

    private AutoCompleteTextView mCustomerNameAtv;
    private TextView mDateTv;
    private TextView mTimeTv;
    private ListView mSalesDetailLv;
    private RadioGroup mOrderTypeRg;
    private Switch mPendingOrderSw;
    private EditText mDiscountEt;
    private TextView mTotalPriceTv;
    private ArrayAdapter customerAtvAdapter;
    private Button mCancelBtn;
    private Button mCheckOutBtn;
    private LinearLayout editButtonsLl;
    private EditText mRemarksEt;

    private SalesOrderEditorViewModel viewModel;
    private SalesDetailAdapter salesDetailAdapter;
    private int mOrderType = 0;
    private int mStatus = 0;
    private long mTotalPrice;
    private long salesOrderId;
    private long mTotalDeliveryCharge = 0;
    private Date currentDate = new Date();
    private boolean isAdd;
    private SalesOrderDto salesOrder;
    private List<SalesDetailDto> salesDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_editor);
        setupLayout();

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(SALES_ORDER_ADD_EXTRA)) {
                Bundle bundle = getIntent().getBundleExtra(SALES_ORDER_ADD_EXTRA);
                if(bundle != null) {
                    ArrayList<AddSalesItemDto> mAddSalesItemDtos = bundle.getParcelableArrayList(SALES_ORDER_ADD_EXTRA);
                    processSalesItemDetail(mAddSalesItemDtos);
                    isAdd = true;
                }
            } else if (intent.hasExtra(SALES_ORDER_EDIT_EXTRA)) {
                salesOrderId = intent.getLongExtra(SALES_ORDER_EDIT_EXTRA, 0L);
            }
        }
        setupLayout();
        setupViewModel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!isAdd) {
            getMenuInflater().inflate(R.menu.menu_sales_order_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.sales_order_action_delete) {
            showDeleteDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this record?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.delete(salesOrder);
                finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupLayout() {
        mCustomerNameAtv = findViewById(R.id.editor_customer_name_atv);
        mDateTv = findViewById(R.id.editor_date_tv);
        mTimeTv = findViewById(R.id.editor_time_tv);
        mSalesDetailLv = findViewById(R.id.editor_sales_detail_lv);
        mOrderTypeRg = findViewById(R.id.editor_order_type_rg);
        mPendingOrderSw = findViewById(R.id.pending_order_sw);
        mDiscountEt = findViewById(R.id.editor_discount_et);
        mTotalPriceTv = findViewById(R.id.editor_total_tv);
        mCancelBtn = findViewById(R.id.editor_cancel_btn);
        mCheckOutBtn = findViewById(R.id.editor_checkout_btn);
        mDateTv.setText(DateUtil.convertDateToStringDate(currentDate));
        mTimeTv.setText(DateUtil.convertDateToStringTime(currentDate));
        editButtonsLl = findViewById(R.id.editor_buttons_ll);
        mRemarksEt = findViewById(R.id.editor_remarks_et);
        mPendingOrderSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mStatus = isChecked ? 1 : 0;
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOrderTypeRg.check(R.id.editor_walkin_rb);
        mOrderTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.editor_delivery_rb:
                        mOrderType = 1;
                        updateTotalPrice(mTotalDeliveryCharge);
                        break;
                    case R.id.editor_walkin_rb:
                    default:
                        mOrderType = 0;
                        updateTotalPrice(-mTotalDeliveryCharge);
                        break;
                }
            }
        });

    }

    private void setupViewModel() {
        SalesOrderEditorViewModelFactory factory = new SalesOrderEditorViewModelFactory(getApplication(), salesOrderId);
        viewModel = ViewModelProviders.of(this, factory).get(SalesOrderEditorViewModel.class);
        final Context context = this;
        viewModel.getCustomerNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                customerAtvAdapter =
                        new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, strings);
                mCustomerNameAtv.setAdapter(customerAtvAdapter);
                customerAtvAdapter.notifyDataSetChanged();
            }
        });

        if(isAdd) {
            setSalesDetailAdapter();
            updateTotalPrice(salesOrder.getTotalPrice());

        } else {
            viewModel.getSalesOrderDetails().observe(this, new Observer<SalesOrderDto>() {
                @Override
                public void onChanged(SalesOrderDto salesOrderDto) {
                    if(salesOrderDto == null) {
                        return;
                    }

                    salesOrder = salesOrderDto;
                    setSalesOrderDataToUI(salesOrderDto);
                }
            });

            viewModel.getSalesDetails().observe(this, new Observer<List<SalesDetailDto>>() {
                @Override
                public void onChanged(List<SalesDetailDto> salesDetailDtos) {
                    if(salesDetailDtos == null) {
                        return;
                    }

                    salesDetails = salesDetailDtos;
                    setSalesDetailAdapter();
                }
            });
        }

        mCheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdd) {
                    String customerName = null;
                    if(mCustomerNameAtv.getText() != null) {
                        customerName = mCustomerNameAtv.getText().toString().trim();
                    }

                    salesOrder.setDate(currentDate);
                    salesOrder.setStatus(mStatus);
                    salesOrder.setOrderType(mOrderType);
                    salesOrder.setTotalPrice(mTotalPrice);
                    salesOrder.setDiscount(getDiscountValue());
                    salesOrder.setRemarks(getRemarks());
                    viewModel.insert(customerName, salesOrder, salesDetails);
                } else {
                    salesOrder.setDiscount(getDiscountValue());
                    salesOrder.setStatus(mStatus);
                    salesOrder.setOrderType(mOrderType);
                    salesOrder.setTotalPrice(mTotalPrice);
                    salesOrder.setRemarks(getRemarks());
                    viewModel.update(salesOrder);
                }
                finish();
            }
        });

        mDiscountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s != null && !s.toString().isEmpty()) {
                    String discountString = s.toString();
                    long discount = ValueUtil.convertDisplayPriceToLong(discountString);
                    updateTotalPrice(+discount);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null && !s.toString().isEmpty()) {
                    String discountString = s.toString();
                    long discount = ValueUtil.convertDisplayPriceToLong(discountString);
                    updateTotalPrice(-discount);
                }
            }
        });

    }

    private long getDiscountValue() {
        if(mDiscountEt.getText() == null || mDiscountEt.getText().toString().isEmpty()) {
            return 0L;
        }

        String discountString = mDiscountEt.getText().toString();
        return ValueUtil.convertDisplayPriceToLong(discountString);
    }

    private String getRemarks() {
        if(mRemarksEt.getText() == null && mRemarksEt.getText().toString().isEmpty()) {
            return null;
        }

        return mRemarksEt.getText().toString().trim();
    }

    private void updateTotalPrice(long valueToAdd) {
        mTotalPrice += valueToAdd;
        mTotalPriceTv.setText(ValueUtil.convertPriceToDisplayValue(mTotalPrice));
    }

    private void setSalesDetailAdapter() {
        salesDetailAdapter = new SalesDetailAdapter(this, salesDetails);
        mSalesDetailLv.setAdapter(salesDetailAdapter);
    }

    private void setSalesOrderDataToUI(SalesOrderDto data) {
        mCustomerNameAtv.setText(data.getCustomerName());
        mDateTv.setText(DateUtil.convertDateToStringDate(data.getDate()));
        mTimeTv.setText(DateUtil.convertDateToStringTime(data.getDate()));
        mOrderTypeRg.check(getOrderTypeViewId(data.getOrderType()));
        boolean status = data.getStatus() == Constants.PENDING ? true : false;
        mPendingOrderSw.setChecked(status);
        mDiscountEt.setText(ValueUtil.convertAmountToDisplayValue(data.getDiscount()));
        mTotalPrice = data.getTotalPrice();
        mTotalPriceTv.setText(ValueUtil.convertPriceToDisplayValue(data.getTotalPrice()));

        if(status) {
            mCheckOutBtn.setText("Save");
            mCustomerNameAtv.setEnabled(false);
        } else {
            mRemarksEt.setEnabled(false);
            mOrderTypeRg.setEnabled(false);
            mCustomerNameAtv.setEnabled(false);
            mDiscountEt.setEnabled(false);
            mPendingOrderSw.setEnabled(false);
            editButtonsLl.setVisibility(View.GONE);
        }
    }

    private int getOrderTypeViewId(int orderType) {
        switch (orderType) {
            case Constants.DELIVERY:
                return R.id.editor_delivery_rb;
            case Constants.COMPLETE:
            default:
                return R.id.editor_walkin_rb;
        }
    }

    public void processSalesItemDetail(List<AddSalesItemDto> addSalesItemDtos) {
        long totalPrice = 0;
        salesDetails = new ArrayList<>();
        StringBuilder descriptionSB = new StringBuilder();
        int listSize = addSalesItemDtos.size();

        for (AddSalesItemDto detail : addSalesItemDtos) {
            String remarks = getRemarks(detail);
            salesDetails.add(new SalesDetailDto(
                    detail.getProductId(),
                    detail.getPrice(),
                    detail.getQuantity(),
                    0L,
                    remarks,
                    remarks != null ? remarks : detail.getProductName()
            ));

            totalPrice += detail.getPrice();
            mTotalDeliveryCharge += Constants.DELIVERY_CHARGE;
            descriptionSB.append(detail.getProductName());
            descriptionSB.append(" x ");
            descriptionSB.append(detail.getQuantity());

            if(--listSize > 0) {
                descriptionSB.append("   ");
            }
        }

        salesOrder = new SalesOrderDto(totalPrice, descriptionSB.toString());
    }

    private String getRemarks(AddSalesItemDto dto) {
        if(dto.getProductId() != Constants.CUSTOM) {
            return null;
        }

        return dto.getProductName();
    }

}
