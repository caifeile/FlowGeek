package org.thanatos.pay.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.thanatos.base.utils.Utilities;
import org.thanatos.pay.R;
import org.thanatos.pay.ui.activity.ChoosePayMethodActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryFragment extends Fragment implements View.OnClickListener {

    private EditText mInput;

    public static final String BUNDLE_KEY_CAST = "bundle_key_cast";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
//                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enrty, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInput = (EditText) view.findViewById(R.id.et_input);

        view.findViewById(R.id.btn_chooser).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // 作为library的module里的资源非final型的,要注意,所以这里不能使用switch
        if (v.getId() == R.id.btn_chooser){
            onNext();
        }
    }

    /**
     * 点击确定按钮进行下一步
     */
    private void onNext() {
        // check
        if(Utilities.isEmpty(mInput.getText().toString())){
            Toast.makeText(getContext(), "100块都不给我o(╯□╰)o", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getContext(), ChoosePayMethodActivity.class);
        intent.putExtra(BUNDLE_KEY_CAST, mInput.getText().toString());
        startActivity(intent);
    }
}
