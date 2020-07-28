#import "BuglyFlutterPlugin.h"
#import <Bugly/Bugly.h>

@implementation BuglyFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"bugly_flutter"
            binaryMessenger:[registrar messenger]];
  BuglyFlutterPlugin* instance = [[BuglyFlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];

}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {

  if ([@"initBugly" isEqualToString:call.method]) {
      NSString *appId = call.arguments[@"appId"];
      BOOL b = [self isBlankString:appId];
      if(!b){
          BuglyConfig * config = [[BuglyConfig alloc] init];
          NSString *channel = call.arguments[@"channel"];
          BOOL isChannelEmpty = [self isBlankString:channel];
          if(!isChannelEmpty){
            config.channel = channel;
          }
          [Bugly startWithAppId:appId config:config];
          result(@"Bugly 初始化成功");
      }else{
          result(@"Bugly appId不能为空");
      }
   }
    else {
      result(FlutterMethodNotImplemented);
    }
}
- (BOOL) isBlankString:(NSString *)string {
    if (string == nil || string == NULL) {
        return YES;
    }

    if ([string isKindOfClass:[NSNull class]]) {
        return YES;
    }
    if ([[string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] length]==0) {
        return YES;
    }
    return NO;

}

@end
