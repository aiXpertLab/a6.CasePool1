### Android SDK v2.9.0

#### ChangeLog

##### BugFix:
+ 修复聊天室消息发送的一些问题
+ 修复用户反馈的一些其他bug

##### BehaviorChange
+ ***JMessage 从2.9.0版本开始自定义消息默认行为发生变化：默认将展示通知栏和增加会话未读数，请开发者注意***

##### NewFeature
+ 适配JCore2.0.0
+ 支持消息发送时自定义未读消息数

#### 升级提示
+ 建议升级！
+ JMessage从2.9.0开始只支持2.0.0及以上的JCore版本，升级 SDK 的时候请将 JCore 一起升级。

#### 升级指南
+ 首先解压您获取到的 zip 压缩包

+ 更新库文件
	+ 打开libs文件夹。添加jcore-android-2.0.0.jar。用 jmessage-sdk-android-2.9.0.jar 替换项目中原有的极光jar文件，并删除原有极光jar文件。用对应CPU文件夹下的 libjcore200.so 文件，替换项目中原有的libjcoreXXX.so文件，并删除原有的极光so文件，每种型号的so文件都可以在SDK下载包中找到。

+ 更新AndroidManifest.xml
	+ 请参考 SDK下载包最新版本的 demo 来更新AndroidManifest.xml 文件配置。
	***注意JCore从2.0.0版本开始原有PushService不再使用，改使用JCommonService，如果项目中使用的JCore是2.0.0之前的版本，集成时需要注意修改manifest中的PushService的配置***
```
 <!-- Since JCore2.0.0 Required SDK核心功能-->
 <!-- 这个Service要继承JCommonService 可参考demo用法-->
 <service android:name="xx.xx.XService"
         android:process=":pushcore">
         <intent-filter>
             <action android:name="cn.jiguang.user.service.action" />
         </intent-filter>
 </service>
```
+ 添加资源文件
    + Android5.0以上，使用应用图标作为通知栏小图标可能显示异常，请参考res/drawable-xxxx/jmessage_notification_icon作为通知栏小图标。详情请见
    Android SDK集成指南中的说明，或者demo中的示例
+ 如果使用jcenter的方式集成JMessage，不需要添加相关组件，详细集成说明请参考官方[集成指南](https://docs.jiguang.cn/jmessage/client/jmessage_android_guide/)