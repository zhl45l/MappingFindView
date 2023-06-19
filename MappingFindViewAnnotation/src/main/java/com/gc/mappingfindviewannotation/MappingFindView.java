package com.gc.mappingfindviewannotation;

import android.util.Log;

import com.gc.mappingfindviewannotation.annotation.BindLayoutID;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Activity中的所有属性，只要是View的子类的：
 * 都会尝试去根据  属性名 作为 资源id，来加载资源，并赋值给改字段。
 * 如 属性 TextView tv_userName， 将尝试 findviewbyid(R.id.tv_userName)如果找到View则将自动赋值给 tv_userName;
 */
public class MappingFindView {
    private static String TAG = "MappingFindView";
    private static String pckName;

    public static void mapping(Object object) {
        setContentView(object);
        mappingFindView(object);
    }

    private static void setContentView(Object object) {
        BindLayoutID bindLayoutID = object.getClass().getAnnotation(BindLayoutID.class);
        if (bindLayoutID == null) {
            Log.e(TAG, "BindLayoutID is null");
            return;
        }
        int resID = bindLayoutID.layoutID();
        if (resID == -1) {
            Log.e(TAG, "BindLayoutID resID is not set");
            return;
        }
        // 或者
        Class<?> clz = object.getClass();

        try {
            Method setContentViewMethod = clz.getMethod("setContentView", int.class);

            if (setContentViewMethod != null) {
                setContentViewMethod.invoke(object, resID);
            }

            Method getPackageNameMethod = getMethod(object.getClass(), "getPackageName"); // 获取包名
            if (getPackageNameMethod != null) {
                pckName = (String) getPackageNameMethod.invoke(object); // 获取包名
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mappingFindView(Object object) {
        Class clz = object.getClass();
        Field[] allField = clz.getDeclaredFields();

        Field tmpF;
        try {
            Method resourcesMethod = getMethod(clz, "getResources");
            Object resources = resourcesMethod.invoke(object);
            Method getIdentifierMethod = resources.getClass().getMethod("getIdentifier", String.class, String.class, String.class);
            Method findViewByIdMethod = getMethod(clz, "findViewById");

            for (int i = 0; i < allField.length; i++) {
                tmpF = allField[i];
                if (!isView(tmpF.getType())) {
                    Log.i(TAG, i + " 不是继承自view 属性 跳过: name = " + tmpF.getName() + " type=" + tmpF.getType());
                    continue;
                }

                Log.i(TAG, i + " 继承自view 属性: name = " + tmpF.getName() + "  type=" + tmpF.getType());
                tmpF.setAccessible(true);

                int id = (int) getIdentifierMethod.invoke(resources, tmpF.getName(), "id", pckName);

                if (id == 0) {
                    Log.e(TAG, "没有找到该组件映射的id:组件名称" + tmpF.getName());
                    continue;
                }
                Log.i(TAG, i + " 属性=" + tmpF.getName() + "  id = " + id);

                tmpF.set(object, findViewByIdMethod.invoke(object, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从一个类中包括父类中，获取方法
     *
     * @param clz
     * @param methodName 方法名
     * @return
     */
    private static Method getMethod(Class clz, String methodName) {
        Method[] methods = clz.getMethods();
        Method getPackageNameMethod;
        for (int i = 0; i < methods.length; i++) {
            getPackageNameMethod = methods[i];
            if (getPackageNameMethod.getName().equals(methodName)) {
                return getPackageNameMethod;
            }
        }

        return getMethod(clz.getSuperclass(), methodName);
    }

    private static boolean isView(Class clz) {
        if (clz == null) return false;
        String typeName = clz.getName();
        if (typeName.equals("java.lang.Object")) {
            return false;
        }
        if (typeName.equals("android.view.View")) {
            return true;
        }
        return isView(clz.getSuperclass());
    }
}
