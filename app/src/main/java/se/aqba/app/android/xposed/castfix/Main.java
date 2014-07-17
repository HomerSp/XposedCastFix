package se.aqba.app.android.xposed.castfix;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class Main implements IXposedHookZygoteInit {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        Log.d("XposedCastFix", "initZygote");

        Class<?> audioServiceClass = findClass("android.media.AudioService", null);

        XposedBridge.hookAllMethods(audioServiceClass, "adjustStreamVolume", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "getLastAudibleStreamVolume", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "getStreamMaxVolume", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "getStreamVolume", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "setStreamMute", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "setStreamVolume", mStreamTypeOverride);
        XposedBridge.hookAllMethods(audioServiceClass, "setStreamVolumeAll", mStreamTypeOverride);
    }

    XC_MethodHook mStreamTypeOverride = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            // just a fail check
            if(!(param.args[0] instanceof Integer))
                return;

            Log.d("XposedCastFix", param.method.getName() + " before " + param.args[0]);

            // Handle STREAM_REMOTE_MUSIC (-200)
            if((Integer)param.args[0] == -200)
                param.args[0] = 3;
        }
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            // just a fail check
            if(!(param.args[0] instanceof Integer))
                return;

            Log.d("XposedCastFix", param.method.getName() + " after " + param.getResult());
        }
    };
}
