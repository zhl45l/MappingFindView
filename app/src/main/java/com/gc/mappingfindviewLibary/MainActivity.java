package com.gc.mappingfindviewLibary;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gc.mappingfindview.MappingFindView;

import java.util.ArrayList;
import java.util.List;

/**
 * 在 activity
 * setContentView 之后 使用：  MappingFindView.mapping(this);
 * 之后，在本类中声明的控件 只要 和 布局文件中的 id一致，就会自动绑定。
 * 如：声明成员属性： TextView tv_userName;
 * 对应布局中：
 * <TextView
 * android:id="@+id/tv_userName"  id 和 属性名称一致
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:text="Hello World!" />
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 工具会帮你将布局文件中 id=tv_userName的控件 和 TextView tv_userName 这个属性绑定起来
     * 请注意：名字必须一致，控件类型必须一致
     * 可以直接使用
     */
    TextView tv_userName;

    /**
     * 可以直接使用
     */
    TextView tv_pwd;

    /**
     * 可以直接使用
     */
    ImageView iv_view;

    /**
     * 不能直接使用，只有 View的子类才会进行映射关联
     */
    String strText;
    /**
     * 可以直接使用
     */
    MyFragment fragmentcenter;
    /**
     * 可以直接使用
     */
    CView CView;
    /**
     * 可以直接使用
     */
    RecyclerView rv_panel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 在setContentView后，添加我们的映射。之后该类中声明的View控件就可以直接使用，当然，不居中要能找到对应控件名的id才行。
        MappingFindView.mappingActivity(this);

        tv_userName.setText("这里可以直接使用了");
        tv_pwd.setText("可以直接使用");

        rv_panel.setLayoutManager(new LinearLayoutManager(this));
        rv_panel.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
        List<String> datas = new ArrayList<>();
        datas.add("测试1");
        datas.add("测试2");
        datas.add("测试3");

        MyAdapter myAdapter = new MyAdapter(datas, this);
        rv_panel.setAdapter(myAdapter);

        // iv_view.setImageResource(R.drawable.flower); 不能直接使用，因为布局文件中，找不到id为iv_view的控件
        // strText.split("a"); // 空指针异常，不能直接使用
    }
}