# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk-windows-1.6_r1/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class org.andengine.** { *; }
-keep class com.a30corner.combomaster.pad.monster.** { *; }
-keep class com.a30corner.combomaster.provider.vo.** { *; }

-keep class com.a30corner.combomaster.padherder.vo.** { *; }

-keep class com.a30corner.combomaster.texturepacker.** { *; }
-keep class com.a30corner.combomaster.playground.** { *; }

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-dontwarn com.viewpagerindicator.**
-dontwarn fi.foyt.foursquare.**
-dontwarn com.squareup.okhttp.**
-dontwarn com.google.appengine.api.urlfetch.**
-dontwarn android.graphics.**
-dontwarn android.**


-dontnote libcore.icu.ICU
-dontnote sun.misc.Unsafe