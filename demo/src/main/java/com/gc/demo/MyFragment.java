package com.gc.demo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gc.mappingfindview.MappingFindView;
import com.gc.demo.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    /**
     * 可以直接使用，因为布局中有 对应名字的id，且类型一致
     */
    TextView tv_test1;
    /**
     * 可以直接使用，因为布局中有 对应名字的id，且类型一致
     */
    Button bt_test2;

    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootV = inflater.inflate(R.layout.fragment_blank, container, false);
        MappingFindView.mappingFragment(this, rootV);
        // 之后可直接使用
        init();
        return rootV;
    }

    private void init() {
        tv_test1.setText("设置成功1");
        bt_test2.setText("设置成功2");
    }
}