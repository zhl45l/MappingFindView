# MappingFindView
一个 安卓 省去 findViewByID的小工具，只需一句话，就会按照规则 将用户声明的所有 View控件 全部 映射绑定到 布局文件中 同名的的View控件。

# 使用说明：
```
请在项目的build.gradle中添加jitpack仓库：
  repositories {
        maven { url 'https://www.jitpack.io' }
    }
```
```
请在模块的build.gradle中添加jitpack仓库：
  dependencies {
        implementation 'com.github.zhl45l:MappingFindView:v1.6.0'
  }
```
## 在Activity中：
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MappingFindView.mappingActivity(this);
        // 之后可直接使用所有控件，告别findViewByid，告别注释绑定
         ...
    }
```
## 在Fragment中：
```
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootV = inflater.inflate(R.layout.fragment_blank, container, false);
        MappingFindView.mappingFragment(this, rootV);
       // 之后可直接使用所有控件，告别findViewByid，告别注释绑定
        init();
        return rootV;
    }
```
## 在自定义View中：
```
   private void init() {
       // 注意，这里加载的layout必须要挂载后 也就是 addview后才可以使用。
       LayoutInflater.from(getContext()).inflate(R.layout.item_c_view, this, true);
       MappingFindView.mappingView(this);
       // 之后可直接使用所有控件，告别findViewByid，告别注释绑定
        ...
   }
```
## 在RecyclerView.ViewHolder中：
```
    public MyHolder(@NonNull View itemView) {
        super(itemView);
        MappingFindView.mappingAdapter(this, itemView);
       // 之后可直接使用所有控件，告别findViewByid，告别注释绑定
       ...
    }
```

# example
```
package demo;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import mappingfindview.MappingFindView;

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

    MyFragment fragmentcenter;

    CView CView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 在setContentView后，添加我们的映射。之后该类中声明的View控件就可以直接使用，当然，不居中要能找到对应控件名的id才行。
        MappingFindView.mappingActivity(this);

        tv_userName.setText("这里可以直接使用了");
        tv_pwd.setText("可以直接使用");

        // iv_view.setImageResource(R.drawable.flower); 不能直接使用，因为布局文件中，找不到id为iv_view的控件

        // strText.split("a"); // 空指针异常，不能直接使用
    }
}
```
# activity_main.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/tv_userName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />

    <TextView
        android:id="@+id/tv_pwd"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />
</LinearLayout>
```

# 自定义View：CView
```
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
        MappingFindView.mappingView(getContext(), this);
        tv_test1.setText("设置成功13232");
        tv_test2.setText("设置成功21111");
    }
}
```
# item_c_view.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/tv_test1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="测试下1"/>

    <TextView
        android:id="@+id/tv_test2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="测试下2"/>

</LinearLayout>
```

# Fragment：MyFragment
```
public class MyFragment extends Fragment {
    TextView tv_test1;
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
        // Inflate the layout for this fragment
        View rootV = inflater.inflate(R.layout.fragment_blank, container, false);
        MappingFindView.mappingFragment(this, rootV);
        init(rootV);
        return rootV;
    }

    private void init(View rootV) {
        tv_test1.setText("设置陈工1");
        bt_test2.setText("设置陈工222");
    }
}
```

# fragment_blank.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="30dp"
    android:gravity="center"
    android:orientation="vertical"
    tools:context=".MyFragment">

    <TextView
        android:id="@+id/tv_test1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_blank_fragment" />

    <Button
        android:id="@+id/bt_test2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_blank_fragment" />

</LinearLayout>
```
