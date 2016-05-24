package com.wzh.hookqqaddfriend.hook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wzh.hookqqaddfriend.util.ReflectUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

//
//import static de.robv.android.xposed.XposedHelpers.callMethod;
//import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
//import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
//import static de.robv.android.xposed.XposedHelpers.findClass;
//import static de.robv.android.xposed.XposedHelpers.setObjectField;


/**
 * Created by Administrator on 2016/5/24 0024.
 */
public class QQHook {
    private Object AutoRemarkActivity ;
    private Object FriendListHandler ;

    public void hook(final ClassLoader loader) {
        try {
            hookQQAddFriend2(loader) ;
        } catch (Throwable t) {
            XposedBridge.log(t);
            Log.i("-----hook-----", "----hookQQAddFriend()方法出异常了----") ;
        }
    }

    /**
     *  劫持qq添加好友的方法，然后实现批量加好友
     * @param loader
     */
    private void hookQQAddFriend(final ClassLoader loader){

        Log.i("-----hook-----","----hookQQAddFriend()方法执行----") ;

        //a(Ljava/lang/String;IZBLjava/lang/String;IILjava/lang/String;B[BLjava/lang/String;ZLjava/lang/String;)V
        //com/tencent/mobileqq/app/FriendListHandler
        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.app.FriendListHandler",loader, "a",
                String.class,
                int.class,
                boolean.class,
                byte.class,
                String.class,
                int.class,
                int.class,
                String.class,
                byte.class,
                byte[].class,
                String.class,
                boolean.class,
                String.class,
                new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                Log.i("-----hook-----","----beforeHookedMethod()方法执行----") ;

                FriendListHandler = param.thisObject ;

                Object[] temp = param.args ;

                String uin = (String)temp[0]  ;              //p1 : uin
                int friend_setting = (int)temp[1]   ;        // p2 ："friend_setting"

                byte group_id = (byte)temp[3] ;             //#p4 ："group_id"

                String msg = (String)temp[4] ;               //p5 ： "msg"

                int source_id = (int)temp[5] ;              // p6："source_id"

                String friend_mobile_number = (String)temp[7]  ;            // p8 ："friend_mobile_number"

                String troop_uin = (String)temp[10] ;                       //#p11  ："troop_uin"

                String remark = (String)temp[12] ;                          //#p13 ："remark"  ：备注

                String str = "uin："+ uin +"--" +
                             "friend_setting："+ friend_setting +"--"+
                             "group_id："+ group_id +"--"+
                             "msg：" + msg+"--" +
                             "source_id："+source_id +"--"+
                             "friend_mobile_number：" + friend_mobile_number +"--" +
                             "troop_uin："+troop_uin +"--" +
                             "remark：" +remark ;

                Log.i("-----hook-----",str) ;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        });
    }

    /**
     * 劫持AutoRemarkActivity中的.method private d()V
     */
    private void hookQQAddFriend2(final ClassLoader loader) {

        XposedHelpers.findAndHookMethod("com.tencent.mobileqq.activity.AutoRemarkActivity", loader, "d", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                AutoRemarkActivity = param.thisObject ;
                FriendListHandler = ReflectUtil.getObjectField(param.thisObject,"a","com.tencent.mobileqq.app.FriendListHandler") ;

                Toast.makeText((Context)AutoRemarkActivity, "劫持！！！", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            }
        }) ;
    }
}
