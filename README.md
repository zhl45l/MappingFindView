# MappingFindView
一个 安卓 省去 findViewByID的小工具，只需要在类声明处，添加一个注解，就会按照规则 将所有用户声明的View控件全部绑定到对应的布局文件。

#example
package com.gc.mappingfindview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gc.mappingfindviewannotation.MappingFindView
import com.gc.mappingfindviewannotation.annotation.BindLayoutID

@BindLayoutID(layoutID = R.layout.activity_main)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        MappingFindView.mapping(this);
    }
}