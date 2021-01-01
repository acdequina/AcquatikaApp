package com.example.acquatikaapp.ui.sales.editor;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.DateUtil;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SalesOrderEditorActivity extends AppCompatActivity {

    public static final String SALES_ORDER_ADD_EXTRA = "SALES_ORDER_ADD_EXTRA";
    public static final String SALES_ORDER_EDIT_EXTRA = "SALES_ORDER_EDIT_EXTRA";

    private static final String TAG = SalesOrderEditorActivity.class.getSimpleName();
    private final Calendar calendar = Calendar.getInstance();
    private AutoCompleteTextView mCustomerNameAtv;
    private TextView mCustomerNameTv;
    private TextView mDateTv;
    private TextView mTimeTv;
    private ListView mSalesDetailLv;
    private RadioGroup mOrderTypeRg;
    private Switch mPendingOrderSw;
    private EditText mDiscountEt;
    private TextView mTotalPriceTv;
    private ArrayAdapter customerAtvAdapter;
    private Button mCheckOutBtn;
    private LinearLayout editButtonsLl;
    private EditText mRemarksEt;
    private LinearLayout mDistributorLl;
    private Switch mDistributorSw;
    private SalesOrderEditorViewModel viewModel;
    private long salesOrderId;
    private boolean isAdd;
    private ArrayList<AddSalesItemDto> addSalesItemDtos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_editor);
        setTitle(getString(R.string.order_summary));
        setupLayout();

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(SALES_ORDER_ADD_EXTRA)) {
                Bundle bundle = getIntent().getBundleExtra(SALES_ORDER_ADD_EXTRA);
                if (bundle != null) {
                    addSalesItemDtos = bundle.getParcelableArrayList(SALES_ORDER_ADD_EXTRA);
                    isAdd = true;
                }
            } else if (intent.hasExtra(SALES_ORDER_EDIT_EXTRA)) {
                salesOrderId = intent.getLongExtra(SALES_ORDER_EDIT_EXTRA, 0L);
            }
        }

        setupLayout();
        setupViewModel();
        setupEventListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isAdd) {
            getMenuInflater().inflate(R.menu.menu_sales_order_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sales_order_action_delete) {
            showDeleteDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_this_record_prompt);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.delete();
                finish();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
        mCustomerNameTv = findViewById(R.id.editor_customer_name_tv);
        mDateTv = findViewById(R.id.editor_date_tv);
        mTimeTv = findViewById(R.id.editor_time_tv);
        mSalesDetailLv = findViewById(R.id.editor_sales_detail_lv);
        mOrderTypeRg = findViewById(R.id.editor_order_type_rg);
        mPendingOrderSw = findViewById(R.id.pending_order_sw);
        mDiscountEt = findViewById(R.id.editor_discount_et);
        mTotalPriceTv = findViewById(R.id.editor_total_tv);
        mCheckOutBtn = findViewById(R.id.editor_checkout_btn);
        mDateTv.setText(DateUtil.convertDateToStringDate(calendar.getTime()));
        mTimeTv.setText(DateUtil.convertDateToStringTime(calendar.getTime()));
        editButtonsLl = findViewById(R.id.editor_buttons_ll);
        mRemarksEt = findViewById(R.id.editor_remarks_et);
        mDistributorLl = findViewById(R.id.distributor_ll);
        mDistributorSw = findViewById(R.id.distributor_sw);
    }

    private void setupViewModel() {
        SalesOrderEditorViewModelFactory factory = new SalesOrderEditorViewModelFactory(getApplication(), salesOrderId);
        viewModel = ViewModelProviders.of(this, factory).get(SalesOrderEditorViewModel.class);
        final Context context = this;
        viewModel.getCustomerNamesLd().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                customerAtvAdapter =
                        new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, strings);
                mCustomerNameAtv.setAdapter(customerAtvAdapter);
                customerAtvAdapter.notifyDataSetChanged();
            }
        });

        if (isAdd) {
            viewModel.processSalesItemDetail(addSalesItemDtos);
            SalesOrderDto salesOrder = viewModel.getSalesOrder();
            setSalesDetailAdapter();
            mTotalPriceTv.setText(ValueUtil.convertPriceToDisplayValue(salesOrder.getTotalPrice()));

        } else {
            viewModel.getSalesOrderDetailsLd().observe(this, new Observer<SalesOrderDto>() {
                @Override
                public void onChanged(SalesOrderDto salesOrderDto) {
                    if (salesOrderDto == null) {
                        return;
                    }
                    viewModel.setSalesOrder(salesOrderDto);
                    setSalesOrderDataToUI(salesOrderDto);
                }
            });

            viewModel.getSalesDetailsLd().observe(this, new Observer<List<SalesDetailDto>>() {
                @Override
                public void onChanged(List<SalesDetailDto> salesDetailDtos) {
                    if (salesDetailDtos == null) {
                        return;
                    }
                    viewModel.setSalesDetails(salesDetailDtos);
                    setSalesDetailAdapter();
                }
            });
        }
    }

    private void setupEventListeners() {
        mPendingOrderSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.getSalesOrder().setStatus(isChecked ? 1 : 0);
            }
        });

        mOrderTypeRg.check(R.id.editor_walkin_rb);
        mOrderTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.editor_delivery_rb:
                        viewModel.processOrderType(Constants.DELIVERY);
                        updateTotalPrice(viewModel.getSalesOrder().getTotalPrice());
                        distributorOption(true);
                        break;
                    case R.id.editor_walkin_rb:
                    default:
                        viewModel.processOrderType(Constants.WALKIN);
                        updateTotalPrice(viewModel.getSalesOrder().getTotalPrice());
                        distributorOption(false);
                        break;
                }
            }
        });

        mDistributorSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.getSalesOrder().setDistributor(isChecked);
                viewModel.hasDeliveryCharge(!isChecked);
                updateTotalPrice(viewModel.getSalesOrder().getTotalPrice());
            }
        });

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateTv.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                viewModel.getSalesOrder().setDate(DateUtil.generateDate(year, monthOfYear, dayOfMonth));
            }

        };

        mDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SalesOrderEditorActivity.this, datePickerListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mCheckOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getSalesOrder().setRemarks(getRemarks());

                if (isAdd) {
                    String customerName = null;
                    if (mCustomerNameAtv.getText() != null) {
                        customerName = mCustomerNameAtv.getText().toString().trim();
                    }

                    viewModel.insert(customerName);
                } else {

                    viewModel.update();
                }

                NavUtils.navigateUpFromSameTask(SalesOrderEditorActivity.this);
            }
        });

        mDiscountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s != null && !s.toString().isEmpty()) {
                    long discount = ValueUtil.convertDisplayPriceToLong(s.toString());
                    long totalPrice = viewModel.setDiscount(discount);
                    updateTotalPrice(totalPrice);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.toString().isEmpty()) {
                    long discount = ValueUtil.convertDisplayPriceToLong(s.toString());
                    long totalPrice = viewModel.setDiscount(-discount);
                    updateTotalPrice(totalPrice);
                }
            }
        });
    }

    private void distributorOption(boolean isOptionEnabled) {
        int visibility = isOptionEnabled ? View.VISIBLE : View.GONE;
        viewModel.getSalesOrder().setDistributor(isOptionEnabled);
        if (!isOptionEnabled) {
            mDistributorSw.setChecked(false);
        }
        mDistributorLl.setVisibility(visibility);
    }

    private void updateTotalPrice(long totalPrice) {
        mTotalPriceTv.setText(ValueUtil.convertPriceToDisplayValue(totalPrice));
    }

    private void setSalesDetailAdapter() {
        SalesDetailAdapter mSalesDetailAdapter = new SalesDetailAdapter(this, viewModel.getSalesDetails());
        mSalesDetailLv.setAdapter(mSalesDetailAdapter);
    }

    private void setSalesOrderDataToUI(SalesOrderDto data) {
        mCustomerNameTv.setText(data.getCustomerName());
        mCustomerNameAtv.setVisibility(View.GONE);
        mCustomerNameTv.setVisibility(View.VISIBLE);
        mDateTv.setText(DateUtil.convertDateToStringDate(data.getDate()));
        mTimeTv.setText(DateUtil.convertDateToStringTime(data.getDate()));
        mOrderTypeRg.check(getOrderTypeViewId(data.getOrderType()));
        boolean status = data.getStatus() == Constants.PENDING;
        mPendingOrderSw.setChecked(status);
        mDiscountEt.setText(ValueUtil.convertAmountToDisplayValue(data.getDiscount()));
        mTotalPriceTv.setText(ValueUtil.convertPriceToDisplayValue(data.getTotalPrice()));
        mRemarksEt.setText(data.getRemarks());
        distributorOption(Constants.DELIVERY == data.getOrderType());
        mDistributorSw.setChecked(data.isDistributor());

        if (status) {
            mCheckOutBtn.setText(R.string.save);
        } else {
            mRemarksEt.setEnabled(false);
            mOrderTypeRg.setEnabled(false);
            mDiscountEt.setEnabled(false);
            mPendingOrderSw.setEnabled(false);
            editButtonsLl.setVisibility(View.GONE);
            RadioButton walkinButton = findViewById(getOrderTypeViewId(Constants.WALKIN));
            walkinButton.setEnabled(false);
            RadioButton deliveryButton = findViewById(getOrderTypeViewId(Constants.DELIVERY));
            deliveryButton.setEnabled(false);
            mDistributorSw.setEnabled(false);
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

    private String getRemarks() {
        if (mRemarksEt.getText() == null && mRemarksEt.getText().toString().isEmpty()) {
            return null;
        }

        return mRemarksEt.getText().toString().trim();
    }

}
