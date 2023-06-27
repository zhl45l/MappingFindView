package com.gc.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gc.mappingfindview.MappingFindView;

public class CView extends LinearLayout {
    /**
     * 可以直接使用，因为布局中有 对应名字的id，且类型一致
     */
    TextView tv_test1, tv_test2;

    /**
     * 不能直接使用，因为不居中找不到对应名字的id
     */
    Button bt_bt1;

    public CView(Context context) {
        super(context);
        init();
    }


    public CView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_c_view, this, true);
        MappingFindView.mappingView(this);
        // 之后可直接使用
        tv_test1.setText("设置成功13232");
        tv_test2.setText("设置成功21111");
    }


}
