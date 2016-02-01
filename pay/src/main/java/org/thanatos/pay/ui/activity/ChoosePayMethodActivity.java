package org.thanatos.pay.ui.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.pay.R;
import org.thanatos.pay.domain.PayMethod;
import org.thanatos.pay.ui.fragment.EntryFragment;



public class ChoosePayMethodActivity extends BaseHoldBackActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private float castDonate2Me;

    private ListView mListView;
    private TextView mOrderCast;
    private TextView mOrderDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 看我多良心,默认1块钱
        castDonate2Me = Float.parseFloat(getIntent().getStringExtra(EntryFragment.BUNDLE_KEY_CAST));

        mListView = (ListView) findViewById(R.id.list_view);
        mOrderCast = (TextView) findViewById(R.id.tv_order_cast);
        mOrderDetail = (TextView) findViewById(R.id.tv_order_detail);

        findViewById(R.id.btn_chooser).setOnClickListener(this);

        mOrderCast.setText(getResources().getString(R.string.order_cast) + "  " + castDonate2Me + "￥");
        mOrderDetail.setText(getResources().getString(R.string.order_detail) + "  " + "选择你的捐助方式吧~");

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return PayMethod.values().length;
            }

            @Override
            public Object getItem(int position) {
                return PayMethod.values()[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                PayMethod pay = PayMethod.values()[position];
                View view = getLayoutInflater().inflate(R.layout.list_item_pay, parent, false);

                ImageView mLogoView = (ImageView) view.findViewById(R.id.iv_logo);
                TextView mPayName = (TextView) view.findViewById(R.id.tv_pay_method);
                TextView mPayDescribe = (TextView) view.findViewById(R.id.tv_pay_describe);
                ImageView mChooserView = (ImageView) view.findViewById(R.id.iv_chooser);

                view.setTag(mChooserView);

                mLogoView.setImageResource(pay.getLogo());
                mPayName.setText(pay.getPayName());
                mPayDescribe.setText(pay.getPayDescribe());

                if (position == 0) {
                    Log.d("thanatos", "----------=----------");
                    mPayName.setCompoundDrawables(null, null,
                            getResources().getDrawable(R.mipmap.icon_recommend), null);
                }

                return view;
            }
        });

        mListView.setOnItemClickListener(this);
        mListView.setSelection(0);
    }

    @Override
    protected String onSetTitle() {
        return getResources().getString(R.string.donate_me);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_choose_pay_method;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_chooser){
            onNext(v);
        }
    }

    /**
     * 确定支付方式
     */
    private void onNext(View view) {
        mListView.getSelectedItem();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView mChooserView = (ImageView) view.getTag();
        mChooserView.setSelected(!mChooserView.isSelected());
    }
}
