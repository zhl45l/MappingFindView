package com.gc.mappingfindviewannotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Activity中的所有属性，只要是View的子类的：
 * 都会尝试去根据  属性名 作为 资源id，来加载资源，并赋值给改字段。
 * 如 属性 TextView tv_userName， 将尝试 findviewbyid(R.id.tv_userName)如果找到View则将自动赋值给 tv_userName;
 */
public class MappingFindView {
    private static String TAG = "MappingFindView";
    private static String pckName;

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
        if (pckName == null || pckName.equals("")) {
            try {
                pckName = getPackName(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert (pckName != null && !pckName.equals(""));

        mappingFindView(object);
    }

    /**
     * 映射一个自定义View，将其中所有的字段和 已存在的同名布局 绑定
     * 必须主线程中调用，确保方法参数 都不为null
     *
     * @param content Content
     * @param view    自定义view根布局
     */
    public static void mappingView(Object content, Object view) {
        assert Thread.currentThread().getName().equals("main");
        assert content != null;
        assert view != null;
        if (pckName == null || pckName.equals("")) {
            try {
                pckName = getPackName(content.getClass().getMethod("getPackageName").invoke(content));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert (pckName != null && !pckName.equals(""));
        mappingFindView(view, view);
    }

    /**
     * 映射一个Fragment，将其中所有的字段和 已存在的同名布局 绑定
     * 目前需使用：androidx.fragment.app报下的 fragment
     * 必须主线程中调用，确保方法参数 都不为null
     *
     * @param target fragment
     * @param source 自定义view根布局
     */
    public static void mappingFragment(Object target, Object source) {
        assert Thread.currentThread().getName().equals("main");
        assert target != null;
        assert source != null;
        if (pckName == null || pckName.equals("")) {
            try {
                if (isFragment(target.getClass())) { // fragment
                    pckName = getPackName(target.getClass().getMethod("getActivity").invoke(target));
                } else { // view
                    pckName = getPackName(target.getClass().getMethod("getPackageName").invoke(target));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assert (pckName != null && !pckName.equals(""));
        mappingFindView(target, source);
    }

    private static void mappingFindView(Object object) {
        mappingFindView(object, object);
    }

    private static void mappingFindView(Object object, Object rootV) {
        Class clz = object.getClass();
        Field[] allField = clz.getDeclaredFields();

        Field tmpF;
        try {
            for (int i = 0; i < allField.length; i++) {
                tmpF = allField[i];
                if (!isView(tmpF.getType())) {
//                    Log.i(TAG, i + " 不是继承自view 属性 跳过: name = " + tmpF.getName() + " type=" + tmpF.getType());
                    continue;
                }

                tmpF.setAccessible(true);

                Object view = findViewById(rootV, tmpF.getName());

                if (view == null) {
//                    Log.e(TAG, "没有找到该控件映射的id:控件名称" + tmpF.getName());
                    continue;
                }

                tmpF.set(object, view);
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


    private static String getPackName(Object activity) throws InvocationTargetException, IllegalAccessException {
        Method getPackageNameMethod = getMethod(activity.getClass(), "getPackageName"); // 获取包名
        if (getPackageNameMethod != null) {
            return (String) getPackageNameMethod.invoke(activity); // 获取包名
        }
        return null;
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

        return (int) getIdentifierMethod.invoke(resources, idName, "id", pckName);
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
