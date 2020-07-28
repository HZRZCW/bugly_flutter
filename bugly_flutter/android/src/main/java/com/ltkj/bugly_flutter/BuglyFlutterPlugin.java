package com.ltkj.bugly_flutter;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.bugly.crashreport.CrashReport.UserStrategy;


/** BuglyFlutterPlugin */
public class BuglyFlutterPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Application mApplication;
  private Context mApplicationContext;
  private WeakReference<Activity> mActivity;


  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "bugly_flutter");
    mApplication = (Application) flutterPluginBinding.getApplicationContext();
    mApplicationContext = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(this);
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    channel = null;
  }

  public BuglyFlutterPlugin initPlugin(MethodChannel methodChannel, Registrar registrar) {
    channel = methodChannel;
    mApplication = (Application) registrar.context().getApplicationContext();
    mActivity = new WeakReference<>(registrar.activity());
    mApplicationContext = registrar.context().getApplicationContext();
    return this;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    mActivity = new WeakReference<>(binding.getActivity());
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    mActivity = null;
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("initBugly")) {
      String appId = call.argument("appId").toString();
      UserStrategy strategy = new UserStrategy(mApplicationContext);
      if (call.hasArgument("channel")) {
        String channel = call.argument("channel");
        if (!TextUtils.isEmpty(channel))
          strategy.setAppChannel(channel);  //设置渠道
      }
      CrashReport.initCrashReport(mApplicationContext, appId, false, strategy);
      result.success("Bugly 初始化成功");
    } else {
      result.notImplemented();
    }
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "bugly_flutter");
    channel.setMethodCallHandler(new BuglyFlutterPlugin().initPlugin(channel, registrar));
  }

}
