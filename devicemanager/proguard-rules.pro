-keep public class com.hanamiLink.devicemanager.**
-keep public class com.hanamiLink.devicemanager.** {public protected *;}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, EnclosingMethod
