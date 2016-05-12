package org.howe.keyboardpanelswitcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.howe.keyboardpanelswitcher.helper.DisplayUtils;
import org.howe.keyboardpanelswitcher.helper.InputMethodUtils;

/**
 * @author xh2009cn
 */
public class MainActivity extends AppCompatActivity {
    
    private View mEmotionPanel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.init(this);
        setContentView(R.layout.activity_main);
        mEmotionPanel = findViewById(R.id.v_panel);
        findViewById(R.id.iv_switcher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmotionPanelShowing()) {
                    InputMethodUtils.toggleSoftInput(getCurrentFocus());
                    mEmotionPanel.postDelayed(mHideEmotionPanelTask, 500);
                } else {
                    showEmotionPanel();
                }
            }
        });

        findViewById(R.id.et_input).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodUtils.setKeyboardShowing(true);
                    if (isEmotionPanelShowing()) {
                        mEmotionPanel.postDelayed(mHideEmotionPanelTask, 500);
                    }
                }
                return false;
            }
        });
        InputMethodUtils.detectKeyboard(this);
        InputMethodUtils.enableCloseKeyboardOnTouchOutside(this);
    }

    public boolean isEmotionPanelShowing() {
        return mEmotionPanel.getVisibility() == View.VISIBLE;
    }

    public void showEmotionPanel() {
        mEmotionPanel.removeCallbacks(mHideEmotionPanelTask);
        InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mEmotionPanel.setVisibility(View.VISIBLE);
        InputMethodUtils.hideKeyboard(getCurrentFocus());
    }

    public void hideEmotionPanel() {
        if (mEmotionPanel.getVisibility() != View.GONE) {
            mEmotionPanel.setVisibility(View.GONE);
            InputMethodUtils.updateSoftInputMethod(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
    }

    public void updateEmotionPanelHeight(int keyboardHeight) {
        ViewGroup.LayoutParams params = mEmotionPanel.getLayoutParams();
        if (params != null && params.height != keyboardHeight) {
            params.height = keyboardHeight;
            mEmotionPanel.setLayoutParams(params);
        }
    }

    private Runnable mHideEmotionPanelTask = new Runnable() {
        @Override
        public void run() {
            hideEmotionPanel();
        }
    };
}
