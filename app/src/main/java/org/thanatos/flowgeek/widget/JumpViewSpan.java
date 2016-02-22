package org.thanatos.flowgeek.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by thanatos on 16/2/19.
 */
public class JumpViewSpan extends ClickableSpan{

    private Intent intent;
    private Context context;

    public JumpViewSpan(Intent intent, Context context) {
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(0XFF339933);
    }

    @Override
    public void onClick(View widget) {
        context.startActivity(intent);
    }
}
