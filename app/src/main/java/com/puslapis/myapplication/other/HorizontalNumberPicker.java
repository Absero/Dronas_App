package com.puslapis.myapplication.other;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.puslapis.myapplication.R;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class HorizontalNumberPicker extends LinearLayout {
    private final String TAG = "HorizontalPicker";
    private TextView et_number;
    private int min=-10, max=10;
    private int pokytis = 1;
    private OnChangeListener mCallback;

    public HorizontalNumberPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.numberpicker_horizontal, this);

        et_number = findViewById(R.id.et_number);
        et_number.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_number.setText("0");
                mCallback.onMove();
            }
        });

        final Button btn_less = findViewById(R.id.btn_less);
        btn_less.setOnClickListener(new AddHandler(-pokytis));

        final Button btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(new AddHandler(pokytis));
    }

    /***
     * HANDLERS
     **/

    private class AddHandler implements OnClickListener {
        final int diff;

        public AddHandler(int diff) {
            this.diff = diff;
        }

        @Override
        public void onClick(View v) {
            int newValue = getValue() + diff;
            if (newValue < min) {
                newValue = min;
            } else if (newValue > max) {
                newValue = max;
            }

            et_number.setText(String.valueOf(newValue));
            mCallback.onMove();
        }
    }

    /***
     * GETTERS & SETTERS
     */

    public int getPokytis() {
        return pokytis;
    }

    public void setPokytis(int pokytis) {
        this.pokytis = pokytis;
    }

    public int getValue() {
        if (et_number != null) {
            try {
                final String value = et_number.getText().toString();
                return Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
        }
        return 0;
    }

    public void setValue(final int value) {
        if (et_number != null) {
            et_number.setText(String.valueOf(value));
        }
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setOnChangeListener(OnChangeListener l) { mCallback = l; }

    /**
     *  interface for value changed
     */
    public interface OnChangeListener {
        void onMove();
    }
}
