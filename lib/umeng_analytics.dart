import 'dart:async';
import 'dart:collection';

import 'package:flutter/services.dart';

class UmengAnalytics {
  static const MethodChannel _channel = const MethodChannel('umeng_analytics');

  /// 初始化Appkey
  static Future<bool> initAppKey(
      String androidAppKey, String iosAppKey, String channel) async {
    Map<String, String> params = HashMap();
    params['android'] = androidAppKey;
    params['ios'] = iosAppKey;
    params['channel'] = channel;
    final dynamic result = await _channel.invokeMethod("initAppKey", params);
    return result;
  }

  /// 事件统计功能
  static Future<bool> onEvent(String id, Map<String, Object> data) async {
    Map<String, Object> params = HashMap();
    params['id'] = id;
    params['data'] = data;
    final dynamic result = await _channel.invokeMethod("onEvent", params);
    return result;
  }

  /// 设置用户账户
  static Future<bool> onProfileSignIn(String userID) async {
    Map<String, Object> params = HashMap();
    params['userID'] = userID;
    final dynamic result =
        await _channel.invokeMethod("onProfileSignIn", params);
    return result;
  }

  /// 取消用户账户
  static Future<bool> onProfileSignOff() async {
    final dynamic result = await _channel.invokeMethod("onProfileSignOff");
    return result;
  }

  /// 自动采集页面
  static Future<bool> setPageCollectionModeAuto() async {
    final dynamic result =
        await _channel.invokeMethod("setPageCollectionModeAuto");
    return result;
  }

  /// 手动采集页面
  static Future<bool> setPageCollectionModeManual() async {
    final dynamic result =
        await _channel.invokeMethod("setPageCollectionModeManual");
    return result;
  }

  /// 自动页面时长统计，必须配对使用
  static Future<bool> onPageStart(String viewName) async {
    Map<String, Object> params = HashMap();
    params['viewName'] = viewName;
    final dynamic result = await _channel.invokeMethod("onPageStart", params);
    return result;
  }

  /// 结束页面时长统计，必须配对使用
  static Future<bool> onPageEnd(String viewName) async {
    Map<String, Object> params = HashMap();
    params['viewName'] = viewName;
    final dynamic result = await _channel.invokeMethod("onPageEnd", params);
    return result;
  }
}
