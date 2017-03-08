-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-dontoptimize
-dontpreverify

-dontwarn
-ignorewarnings

-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontwarn android.support.**

#
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*


-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.wander.life.R$*{
public static final int *;
}

-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.v8.** { *; }



-keep class io.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

-keep class com.wander.life.widget.** {
*;
}

#保留应用中的接口，不然接口中的方法会变少（当编译器发现该方法在应用中没有用到时就会删除，现在测试时这样，没有深入研究）
-keep interface com.wander.**{*;}
#保留mapping文件中的行号，崩溃的时候就有源文件和行号的信息
-keepattributes SourceFile,LineNumberTable

#用来保持生成的表名不被混淆
-keep class **$Properties
-keep class com.wander.life.bean.**{*;}
-keep class com.yunos.**{*;}

-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

-dontwarn org.hamcrest.**
-keep class org.hamcrest.** { *;}
-dontwarn org.apache.**
-keep class org.apache.** { *;}
-dontwarn io.**
-keep class io.** { *;}
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *;}
-dontwarn com.mstar.**
-keep class com.mstar.** { *;}
-dontwarn android.net.**
-keep class android.net.** { *;}
-keep class com.umeng.** { *;}
-keep class okio.** { *;}
-dontwarn okio.**
-keep class okhttp3.** { *;}
-dontwarn okhttp3.**
-keep class pl.droidsonroids.gif** { *;}
-dontwarn pl.droidsonroids.gif**

-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

#ali hotfix
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}

