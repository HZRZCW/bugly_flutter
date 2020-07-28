import 'dart:async';
import 'dart:io';
import 'dart:convert';

import 'package:flutter/services.dart';

class BuglyFlutter {
  static const MethodChannel _channel =
  const MethodChannel('bugly_flutter');

  ///初始化
  static Future<String> init({
    String androidAppId,
    String iOSAppId,
    String channel,//自定义渠道标识
  }) async {
    assert((Platform.isAndroid && androidAppId != null) ||
        (Platform.isIOS && iOSAppId != null));
    Map<String, Object> map = {
      "appId": Platform.isAndroid ? androidAppId : iOSAppId,
      "channel": channel
    };
    final String result = await _channel.invokeMethod('initBugly', map);
    return result;
  }
}
