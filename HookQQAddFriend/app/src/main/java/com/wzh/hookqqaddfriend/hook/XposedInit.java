package com.wzh.hookqqaddfriend.hook;

import android.util.SparseArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wzh on 2016/5/24 0024.
 */
public class XposedInit implements IXposedHookLoadPackage{

    private SparseArray<QQHook> mArray;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.contains("com.tencent.mobileqq"))
            return ;
        ClassLoader loader = loadPackageParam.classLoader ;

        QQHook qhook = getHook(loadPackageParam.appInfo.uid) ;
        if (qhook != null)
            qhook.hook(loader);
    }

    private QQHook getHook(int uid) {
        if (mArray == null) {
            mArray = new SparseArray<>();
        }
        if (mArray.indexOfKey(uid) != -1) {
            return mArray.get(uid);
        }

        mArray.put(uid, new QQHook());

        return mArray.get(uid);
    }
}
