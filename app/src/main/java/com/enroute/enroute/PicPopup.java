package com.enroute.enroute;

/**
 * Created by haoyu on 2018/4/3.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PicPopup extends PopupWindow {

    private LinearLayout Layout_take_photo, Layout_pick_photo, Layout_cancel;
    private View mMenuView;

    public PicPopup(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog, null);
        Layout_take_photo = (LinearLayout) mMenuView.findViewById(R.id.Layout_take_photo);
        Layout_pick_photo = (LinearLayout) mMenuView.findViewById(R.id.Layout_pick_photo);
        Layout_cancel = (LinearLayout) mMenuView.findViewById(R.id.Layout_cancel);

        Layout_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                dismiss();
            }
        });

        Layout_pick_photo.setOnClickListener(itemsOnClick);
        Layout_take_photo.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);

        this.setWidth(LayoutParams.FILL_PARENT);

        this.setHeight(LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);

        this.setAnimationStyle(R.style.AnimBottom);

        ColorDrawable dw = new ColorDrawable(0xb0000000);

        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}

