package com.gc.mappingfindviewtools;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Activity、Fragment、自定义View，Adapter中的所有属性，只要是View的子类的：，
 * 都会尝试去根据  属性名 作为 资源id，来加载资源，并赋值给该字段。
 * 如 属性 TextView tv_userName， 将尝试 findviewbyid(R.id.tv_userName)
 * 如果找到View则将自动赋值给 tv_userName;
 */
public class MappingFindView {
    private static String TAG = "MappingFindView";
    private static String pkgName;

    /**
     * 映射 Activity中所有view子类的属性
     * 目前Activity必须继承自：AppCompatActivity
     * 必须主线程中调用，确保方法参数 都不为null
     *
     * @param object Activity
     */
    public static void mappingActivity(Object object) {
        assert Thread.currentThread().getName().equals("main");
        assert object != null;
        mappingFindView(object, object, object);
    }

    /**
     * 映射一个自定义View，将其中所有的字段和 已存在的同名布局 绑定
     * 必须主线程中调用，确保方法参数 都不为null
     *
     * @param view 自定义view根布局
     */
    public static void mappingView(Object view) {
        assert Thread.currentThread().getName().equals("main");
        assert view != null;
        Object context = null;
        try {
            context = getMethod(view.getClass(), "getContext").invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mappingFindView(context, view, view);
    }

    /**
     * 映射一个Fragment，将其中所有的字段和 已存在的同名布局 绑定
     * 目前需使用：androidx.fragment.app报下的 fragment
     * 必须主线程中调用，确保方法参数 都不为null
     *
     * @param fragment fragment
     * @param view     自定义view根布局
     */
    public static void mappingFragment(Object fragment, Object view) {
        assert Thread.currentThread().getName().equals("main");
        assert fragment != null;
        assert view != null;
        Object context = null;
        try {
            context = getMethod(fragment.getClass(), "getContext").invoke(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mappingFindView(context, fragment, view);
    }

    /**
     * @param holder RecyclerView.ViewHolder
     * @param view   view的根节点
     */
    public static void mappingAdapter(Object holder, Object view) {
        Object context = null;
        try {
            context = getMethod(view.getClass(), "getContext").invoke(view);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        assert Thread.currentThread().getName().equals("main");
        assert context != null;
        assert holder != null;
        assert view != null;

        mappingFindView(context, holder, view);
    }

    /**
     * @param context context
     * @param target  持有各控件声明的类
     * @param rootV   对应的 view 根节点
     */
    private static void mappingFindView(Object context, Object target, Object rootV) {
        if (null == pkgName || pkgName.equals(""))
            pkgName = getPackageName(context);
        assert (pkgName != null && !pkgName.equals(""));
        Class clz = target.getClass();
        Field[] allField = clz.getDeclaredFields();

        Field tmpF;
        try {
            for (int i = 0; i < allField.length; i++) {
                tmpF = allField[i];
                if (!isView(tmpF.getType())) {
                    Log.i(TAG, i + " 不是继承自view 属性 跳过: name = " + tmpF.getName() + " type=" + tmpF.getType());
                    continue;
                }

                tmpF.setAccessible(true);

                Object view = findViewById(rootV, tmpF.getName());

                if (view == null) {
                    Log.e(TAG, "没有找到该控件映射的id:控件名称" + tmpF.getName());
                    continue;
                }

                tmpF.set(target, view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据Context获取包名
     * @param content android.content
     * @return 包名
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static String getPackageName(Object content) {
        Method getPackageNameMethod = getMethod(content.getClass(), "getPackageName"); // 获取包名
        if (getPackageNameMethod != null) {
            try {
                return (String) getPackageNameMethod.invoke(content); // 获取包名
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
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
        Method method;
        for (int i = 0; i < methods.length; i++) {
            method = methods[i];
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        Class sc = clz.getSuperclass();
        if (sc == null) {
            return null;
        }
        return getMethod(sc, methodName);
    }

    /**
     * 从一个类中包括父类中，获取方法
     *
     * @param clz
     * @param methodName 方法名
     * @return
     */
    private static Method getMethod(Class clz, String methodName, Class<?>... params) {
        Method[] methods = clz.getMethods();
        Method method;
        for (int i = 0; i < methods.length; i++) {
            method = methods[i];

            if (method.getName().equals(methodName)) {
                if (params != null) {
                    Class<?>[] pts = method.getParameterTypes();
                    if (pts == null || params.length != pts.length) {
                        continue;
                    }
                    for (int j = 0; j < params.length; j++) {
                        if (params[j].getName() == pts[j].getName()) {
                            return method;
                        }
                    }
                } else {
                    return method;
                }

            }
        }
        Class sc = clz.getSuperclass();
        if (sc == null) {
            return null;
        }
        return getMethod(sc, methodName);
    }


    /**
     * 获取资源id,int值
     *
     * @param object 宿主
     * @param idName id的名字
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private static Object findViewById(Object object, String idName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        int id = getResourceID(object, idName);
        return getMethod(object.getClass(), "findViewById").invoke(object, id);
    }

    /**
     * 获取资源id
     *
     * @param object 宿主
     * @param idName id的名字
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    private static int getResourceID(Object object, String idName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method resourcesMethod = getMethod(object.getClass(), "getResources");
        Object resources = resourcesMethod.invoke(object);
        Method getIdentifierMethod = resources.getClass().getMethod("getIdentifier", String.class, String.class, String.class);

        return (int) getIdentifierMethod.invoke(resources, idName, "id", pkgName);
    }

    private static boolean isActivity(Class clz) {
        return inheritFrom(clz, "android.app.Activity");
    }

    private static boolean isFragment(Class clz) {
        boolean isFragment = inheritFrom(clz, "androidx.fragment.app.Fragment");
        if (!isFragment)
            isFragment = inheritFrom(clz, "android.app.Fragment");
        return isFragment;
    }

    private static boolean isView(Class clz) {
        return inheritFrom(clz, "android.view.View");
    }

    private static boolean isContent(Class clz) {
        return inheritFrom(clz, "android.content.Context");
    }

    // 是否继承自父类
    private static boolean inheritFrom(Class clz, String superClass) {
        if (clz == null) return false;
        String typeName = clz.getName().trim();
        if (typeName.equals("java.lang.Object")) {
            return false;
        }
        if (typeName.equals(superClass)) {
            return true;
        }
        return inheritFrom(clz.getSuperclass(), superClass);
    }
}
