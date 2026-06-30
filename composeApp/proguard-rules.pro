-keep class * extends androidx.room3.RoomDatabase { <init>(); }

-keep class androidx.room3.** { *; }
-keep class androidx.sqlite.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

# JNA rules
-keep class com.sun.jna.** { *; }
-keepclassmembers class com.sun.jna.** { *; }
-keepnames class com.sun.jna.** { *; }
-dontwarn com.sun.jna.**
-dontnote com.sun.jna.**

# FileKit rules
-keep class io.github.vinceglb.filekit.** { *; }
-dontwarn io.github.vinceglb.filekit.**

# Keep attributes required by Room and JNA
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod,Exceptions,Deprecated,SourceFile,LineNumberTable
