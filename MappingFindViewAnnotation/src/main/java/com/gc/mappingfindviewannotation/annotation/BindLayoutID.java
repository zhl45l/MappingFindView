package com.gc.mappingfindviewannotation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 类的
@Retention(RetentionPolicy.RUNTIME) // 运行时可查询
public @interface BindLayoutID {
    int layoutID() default -1;
}
