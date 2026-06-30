-keep class * extends androidx.room3.RoomDatabase { <init>(); }

-keep class androidx.room3.** { *; }
-keep class androidx.sqlite.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep attributes required by Room
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
