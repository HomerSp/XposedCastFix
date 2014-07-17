XposedCastFix
=============

Fixes SystemUI crash on LG and possibly Samsung devices when trying to pull down the notification bar while casting to a Chromecast.

This problem is ultimately caused by getActiveStreamType (https://github.com/android/platform_frameworks_base/blob/master/media/java/android/media/AudioService.java#L2652) returning STREAM_REMOTE_MUSIC, or -200 and neither Samsung nor LG are checking for that resulting in a crash since the getStream* and setStream* methods in AudioManager don't accept STREAM_REMOTE_MUSIC as a valid stream.
