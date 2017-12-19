package com.lesson.vv_bobkov.a2l5_bobkov;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by samsung on 19.12.2017.
 */

class SelectDialog extends Dialog {

    private PressedButton pressedButton;
    private final View.OnClickListener pressedEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pressedButton = PressedButton.EDIT;
        }
    };
    private final View.OnClickListener pressedOpen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pressedButton = PressedButton.OPEN;
        }
    };

    public SelectDialog(@NonNull Context context) {
        super(context);

        this.setCancelable(false);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout llContent = new LinearLayout(context);
        llContent.setOrientation(LinearLayout.VERTICAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageButton ibtnEdit = new ImageButton(context);
            ibtnEdit.setBackground(context.getDrawable(R.drawable.ic_edit_black_24dp));
            ibtnEdit.setOnClickListener(pressedEdit);
            llContent.addView(ibtnEdit, params);
            ImageButton ibtnOpen = new ImageButton(context);
            ibtnOpen.setBackground(context.getDrawable(R.drawable.ic_open_in_browser_black_24dp));
            ibtnOpen.setOnClickListener(pressedOpen);
            llContent.addView(ibtnOpen, params);
        } else {
            Button btnEdit = new Button(context);
            btnEdit.setText(R.string.edit);
            btnEdit.setOnClickListener(pressedEdit);
            llContent.addView(btnEdit, params);
            Button btnOpen = new Button(context);
            btnOpen.setText(R.string.open);
            btnOpen.setOnClickListener(pressedOpen);
            llContent.addView(btnOpen, params);
        }

        this.setContentView(llContent, params);
    }

    public PressedButton showDialig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.create();
        } else {
            this.show();
        }
        return pressedButton;
    }

    enum PressedButton {
        EDIT, OPEN;
    }
}
