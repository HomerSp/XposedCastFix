package se.aqba.app.android.xposed.castfix;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookZygoteInit {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        Log.d("XposedCastFix", "initZygote");

        findAndHookMethod("android.media.AudioManager", null, "getStreamMaxVolume", Integer.TYPE, mStreamVolume);
        findAndHookMethod("android.media.AudioManager", null, "getStreamVolume", Integer.TYPE, mStreamVolume);
        findAndHookMethod("android.media.AudioManager", null, "setStreamVolume", Integer.TYPE, Integer.TYPE, Integer.TYPE, mStreamVolume);
    }

    XC_MethodHook mStreamVolume = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Log.d("XposedCastFix", param.method.getName() + " before " + (Integer)param.args[0]);

            if((Integer)param.args[0] < 0)
                param.args[0] = 3;
        }
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Log.d("XposedCastFix", param.method.getName() + " after " + (Integer)param.getResult());
        }
    };
}
