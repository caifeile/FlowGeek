package org.thanatos.flowgeek.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.thanatos.base.ui.fragment.BaseTabNavFragment;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.EmotionRules;
import org.thanatos.base.utils.UIHelper;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;

import java.util.Arrays;

/**
 * @author thanatos
 * @create 2016-01-06
 **/
public class EmotionPanelFragment extends BaseTabNavFragment {

    public static final int COLUMN = 7;
    public static final int ROW = 3;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCurrentItem(0);
    }

    @Override
    public View setupTabItemView(String title) {
        TextView view = new TextView(mContext);
        int m4 = UIHelper.dip2px(mContext, 4);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(m4, m4);
        int m5 = UIHelper.dip2px(mContext, 5);
        layout.setMargins(m5, m5, m5, m5);
        view.setLayoutParams(layout);
        view.setBackgroundResource(R.drawable.selector_dot_nav);
        return view;
    }

    @Override
    public void onSetupTabs() {
        EmotionRules[] rules = EmotionRules.values();
        //一页显示几条
        int page_size = COLUMN * ROW;
        //总共多少页
        int page_count = (rules.length + page_size - 1) / page_size;
        int i;
        for (i = 0; i < page_count - 1; i++) {
            addTab("", initView(i * page_size, (i + 1) * page_size - 1));
        }
        addTab("", initView(i * page_size, rules.length - 1));
    }

    public void addTab(String title, View view){
        addTab(title, new ViewFragment(view));
    }

    /**
     * @param start start index on EmotionRules
     * @param end end index on EmotionRules
     * @return
     */
    private View initView(int start, int end) {
        final EmotionRules[] rules = Arrays.copyOfRange(EmotionRules.values(), start, end + 1);
        GridView view = new GridView(mContext);
        GridView.LayoutParams params = new GridView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setLayoutParams(params);
        view.setNumColumns(COLUMN);
        view.setAdapter(new EmotionAdapter(rules));
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Events<EmotionRules> events = Events.just(rules[position]);
                events.what = Events.EventEnum.DELIVER_SELECT_EMOTION;
                RxBus.getInstance().send(events);
            }
        });
        return view;
    }

    @Override
    public FragmentManager getGenuineFragmentManager() {
        return getChildFragmentManager();
    }

    public static class EmotionAdapter extends BaseAdapter {
        private EmotionRules[] rules;

        public EmotionAdapter(EmotionRules[] rules) {
            this.rules = rules;
        }

        @Override
        public int getCount() {
            return rules.length;
        }

        @Override
        public Object getItem(int position) {
            return rules[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHandler handler;
            if (convertView == null) {
                convertView = new ImageView(parent.getContext());
                int m3 = UIHelper.dip2px(parent.getContext(), 3f);
                convertView.setPadding(m3, m3, m3, m3);
                handler = new ViewHandler();
                handler.iView = (ImageView) convertView;
                convertView.setTag(handler);
            } else {
                handler = (ViewHandler) convertView.getTag();
            }
            handler.iView.setImageResource(rules[position].getMResId());
            return convertView;
        }

        class ViewHandler {
            public ImageView iView;
        }
    }
}
