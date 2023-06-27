package com.gc.mappingfindviewLibary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gc.mappingfindview.MappingFindView;

public class CView extends LinearLayout {
    TextView tv_test1, tv_test2;

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
