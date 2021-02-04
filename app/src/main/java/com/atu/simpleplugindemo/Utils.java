package com.atu.simpleplugindemo;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

/**
 * Create by atu on 2021/2/4
 */
class Utils {

    public static void loadClass(Context context){
        try {
            //获取BaseDexClassLoader中的pathList（dexPathList）
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            //获取的雪PathList中的dexElemetns
            Class<?> dexClazz = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexClazz.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);

            //获取宿主类加载器
            ClassLoader pathClassLoader = context.getClassLoader();
            //pathlist类对象
            Object hostPathList = pathListField.get(pathClassLoader);
            //宿主dexelements
            Object[] hostDexElements = (Object[]) dexElementsField.get(hostPathList);

            String apkPath = "/sdcard/output.dex";
            //插件的类加载器
            ClassLoader dexClassLoader = new DexClassLoader(apkPath, context.getCacheDir().getAbsolutePath(), null, pathClassLoader);
            Object pluginPathList = pathListField.get(dexClassLoader);
            Object[] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);
            //创建一个新数组
            Object[] newElements = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType(), hostDexElements.length + pluginDexElements.length);
            //把两个dexElements合并到新数组
            System.arraycopy(hostDexElements,0,newElements,0,hostDexElements.length);
            System.arraycopy(pluginDexElements,0,newElements,hostDexElements.length,pluginDexElements.length);
            //赋值
            dexElementsField.set(hostPathList,newElements);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
