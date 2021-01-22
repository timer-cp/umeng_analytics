import 'dart:collection';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:umeng_analytics/umeng_analytics.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    UmengAnalytics.initAppKey("android appkey", "ios appkey", "um");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Umeng Analytics Plugin example'),
        ),
        body: Center(
          child: Column(
            children: [
              RaisedButton(
                onPressed: () {
                  Map<String, Object> data = HashMap();
                  data['tab1click'] = 1;
                  UmengAnalytics.onEvent("UMEvent", data);
                },
                child: Text("事件统计"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.onProfileSignIn("1001");
                },
                child: Text("设置用户账户"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.onProfileSignOff();
                },
                child: Text("取消用户账户"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.setPageCollectionModeAuto();
                },
                child: Text("自动采集页面"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.setPageCollectionModeManual();
                },
                child: Text("手动采集页面"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.onPageStart("page1");
                },
                child: Text("页面时长统计开始"),
              ),
              RaisedButton(
                onPressed: () {
                  UmengAnalytics.onPageStart("page2");
                },
                child: Text("页面时长统计结束"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
