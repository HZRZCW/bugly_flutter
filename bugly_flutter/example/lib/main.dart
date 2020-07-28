import 'package:flutter/material.dart';
import 'package:bugly_flutter/bugly_flutter.dart';

void main() {
  runApp(MyApp());

  BuglyFlutter.init(androidAppId: "你的Android appId",iOSAppId: "你的iOS appId");
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running on: $_platformVersion\n'),
        ),
      ),
    );
  }
}
