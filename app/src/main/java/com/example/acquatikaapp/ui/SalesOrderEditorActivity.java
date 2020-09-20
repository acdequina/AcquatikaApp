package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.ui.util.DisplayValueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesOrderEditorActivity extends AppCompatActivity {

    public static final String SALES_ORDER_ADD_EXTRA = "SALES_ORDER_ADD_EXTRA";
    public static final String SALES_ORDER_EDIT_EXTRA = "SALES_ORDER_EDIT_EXTRA";
    private int CUSTOM_PRODUCT_INDEX = 2;

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

    private SalesDetailAdapter salesDetailAdapter;
    private int mOrderType = 0;
    private int mStatus = 0;
    private long mTotalPrice;
    private long salesOrderId;
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
        mDateTv.setText(DisplayValueUtil.getDisplayValueDate(currentDate));
        mTimeTv.setText(DisplayValueUtil.getDisplayValueTime(currentDate));
        editButtonsLl = findViewById(R.id.editor_buttons_ll);
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
                        updateTotalPrice(500);
                        break;
                    case R.id.editor_walkin_rb:
                    default:
                        mOrderType = 0;
                        updateTotalPrice(-500);
                        break;
                }
            }
        });

    }

    private void setupViewModel() {
        SalesOrderEditorViewModelFactory factory = new SalesOrderEditorViewModelFactory(getApplication(), salesOrderId);
        final SalesOrderEditorViewModel viewModel = ViewModelProviders.of(this, factory).get(SalesOrderEditorViewModel.class);
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
                    salesOrder = salesOrderDto;
                    setSalesOrderDataToUI(salesOrderDto);
                }
            });

            viewModel.getSalesDetails().observe(this, new Observer<List<SalesDetailDto>>() {
                @Override
                public void onChanged(List<SalesDetailDto> salesDetailDtos) {
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
                    viewModel.insert(customerName, salesOrder, salesDetails);
                } else {
                    salesOrder.setDiscount(getDiscountValue());
                    salesOrder.setStatus(mStatus);
                    salesOrder.setOrderType(mOrderType);
                    salesOrder.setTotalPrice(mTotalPrice);
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
                    long discount = DisplayValueUtil.convertDisplayPriceToLong(discountString);
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
                    long discount = DisplayValueUtil.convertDisplayPriceToLong(discountString);
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
        return DisplayValueUtil.convertDisplayPriceToLong(discountString);
    }

    private void updateTotalPrice(long valueToAdd) {
        mTotalPrice += valueToAdd;
        mTotalPriceTv.setText(DisplayValueUtil.convertPriceToDisplayValue(mTotalPrice));
    }

    private void setSalesDetailAdapter() {
        salesDetailAdapter = new SalesDetailAdapter(this, salesDetails);
        mSalesDetailLv.setAdapter(salesDetailAdapter);
    }

    private void setSalesOrderDataToUI(SalesOrderDto data) {
        mCustomerNameAtv.setText(data.getCustomerName());
        mDateTv.setText(DisplayValueUtil.getDisplayValueDate(data.getDate()));
        mTimeTv.setText(DisplayValueUtil.getDisplayValueTime(data.getDate()));
        mOrderTypeRg.check(getOrderTypeViewId(data.getOrderType()));
        boolean status = data.getStatus() == 1 ? true : false;
        mPendingOrderSw.setChecked(status);
        mDiscountEt.setText(DisplayValueUtil.convertAmountToDisplayValue(data.getDiscount()));
        mTotalPrice = data.getTotalPrice();
        mTotalPriceTv.setText(DisplayValueUtil.convertPriceToDisplayValue(data.getTotalPrice()));

        if(status) {
            mCheckOutBtn.setText("Save");
            mCustomerNameAtv.setEnabled(false);
            mOrderTypeRg.setEnabled(false);
        } else {
            mCustomerNameAtv.setEnabled(false);
            editButtonsLl.setVisibility(View.GONE);
        }
    }

    private int getOrderTypeViewId(int orderType) {
        switch (orderType) {
            case 1:
                return R.id.editor_delivery_rb;
            case 0:
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
        if(dto.getProductId() != CUSTOM_PRODUCT_INDEX) {
            return null;
        }

        return dto.getProductName();
    }

}
