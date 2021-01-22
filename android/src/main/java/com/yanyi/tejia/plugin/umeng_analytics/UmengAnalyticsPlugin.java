package com.yanyi.tejia.plugin.umeng_analytics;

import android.content.Context;

import androidx.annotation.NonNull;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.internal.crash.UMCrashManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * UmengAnalyticsPlugin
 */
public class UmengAnalyticsPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    private Context mContext = null;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "umeng_analytics");
        channel.setMethodCallHandler(this);

        if (mContext == null) {
            mContext = flutterPluginBinding.getApplicationContext();
        }

        try {
            Class<?> config = Class.forName("com.umeng.commonsdk.UMConfigure");
            Method method = config.getDeclaredMethod("setWraperType", String.class, String.class);
            method.setAccessible(true);
            method.invoke(null, "flutter", "1.0");
            android.util.Log.i("UMLog", "setWraperType:flutter1.0 success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("initAppKey")) {
            initAppKey(call, result);
        } else if (call.method.equals("onEvent")) {
            onEvent(call, result);
        } else if (call.method.equals("onProfileSignIn")) {
            onProfileSignIn(call, result);
        } else if (call.method.equals("onProfileSignOff")) {
            onProfileSignOff(result);
        } else if (call.method.equals("setPageCollectionModeAuto")) {
            setPageCollectionModeAuto(result);
        } else if (call.method.equals("setPageCollectionModeManual")) {
            setPageCollectionModeManual(result);
        } else if (call.method.equals("onPageStart")) {
            onPageStart(call, result);
        } else if (call.method.equals("onPageEnd")) {
            onPageEnd(call, result);
        } else if (call.method.equals("reportError")) {
            reportError(call, result);
        } else {
            result.notImplemented();
        }
    }

    private void initAppKey(@NonNull MethodCall call, @NonNull Result result) {
        String android = call.hasArgument("android") ? (String) call.argument("android") : null;
//        String ios = call.hasArgument("ios") ? (String) call.argument("ios") : null;
        String channel = call.hasArgument("channel") ? (String) call.argument("channel") : null;
        if (android == null || channel == null) {
            String errorDetail = android == null ? "Android AppKey为空！" : "Channel 为空！";
            result.error(Consts.ARGUMENT_ERROR, "方法参数不正确", errorDetail);
        }
        UMConfigure.init(mContext, android, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
        result.success(true);
    }

    private void onEvent(@NonNull MethodCall call, @NonNull Result result) {
        String id = call.hasArgument("id") ? (String) call.argument("id") : null;
        Map<String, Object> data = call.hasArgument("data") ? (Map<String, Object>) call.argument("data") : null;
        if (id == null) {
            result.error(Consts.ARGUMENT_ERROR, "事件ID不能为空", "事件ID为空");
        }
        MobclickAgent.onEventObject(mContext, id, data);
        android.util.Log.i("UMLog", "onEventObject:" + id + "(" + data == null ? null : data.toString() + ")");
        result.success(true);
    }

    private void onProfileSignIn(@NonNull MethodCall call, @NonNull Result result) {
        String userID = call.hasArgument("userID") ? (String) call.argument("userID") : null;
        if (userID == null) {
            result.error(Consts.ARGUMENT_ERROR, "用户ID不能为空", "用户ID为空");
        }
        MobclickAgent.onProfileSignIn(userID);
        android.util.Log.i("UMLog", "onProfileSignIn:" + userID);
        result.success(true);
    }

    private void onProfileSignOff(@NonNull Result result) {
        MobclickAgent.onProfileSignOff();
        android.util.Log.i("UMLog", "onProfileSignOff");
        result.success(true);
    }

    private void setPageCollectionModeAuto(@NonNull Result result) {
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);
        android.util.Log.i("UMLog", "setPageCollectionModeAuto");
        result.success(true);
    }

    private void setPageCollectionModeManual(@NonNull Result result) {
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
        android.util.Log.i("UMLog", "setPageCollectionModeManual");
        result.success(true);
    }

    private void onPageStart(@NonNull MethodCall call, @NonNull Result result) {
        String viewName = call.hasArgument("viewName") ? (String) call.argument("viewName") : null;
        if (viewName == null) {
            result.error(Consts.ARGUMENT_ERROR, "页面名称不能为空", "页面名称为空");
        }
        MobclickAgent.onPageStart(viewName);
        android.util.Log.i("UMLog", "onPageStart:" + viewName);
        result.success(true);
    }

    private void onPageEnd(@NonNull MethodCall call, @NonNull Result result) {
        String viewName = call.hasArgument("viewName") ? (String) call.argument("viewName") : null;
        if (viewName == null) {
            result.error(Consts.ARGUMENT_ERROR, "页面名称不能为空", "页面名称为空");
        }
        MobclickAgent.onPageEnd(viewName);
        android.util.Log.i("UMLog", "onPageEnd:" + viewName);
        result.success(true);
    }

    private void reportError(@NonNull MethodCall call, @NonNull Result result) {
        String err = call.hasArgument("err") ? (String) call.argument("err") : null;
        if (err == null) {
            result.error(Consts.ARGUMENT_ERROR, "错误信息不能为空", "错误信息为空");
        }
        MobclickAgent.reportError(mContext, err);
        android.util.Log.i("UMLog", "reportError:" + err);
        result.success(true);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
