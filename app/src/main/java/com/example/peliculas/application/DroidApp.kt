package com.example.peliculas.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.example.peliculas.BuildConfig
import com.example.peliculas.interfaces.AppComponent
import com.example.peliculas.interfaces.DaggerAppComponent
import com.example.peliculas.utils.security.RootUtil
import kotlin.system.exitProcess

class DroidApp: Application() {

    val appComponent: AppComponent = DaggerAppComponent.create()

    companion object {
        lateinit var instance: DroidApp
            private set
        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        //PREVENT ROOT DEVICE
        if (RootUtil.isDeviceRooted) {
            exitProcess(-1)
        }
        if(BuildConfig.BUILD_TYPE == "release" && !BuildConfig.DEBUG){
            //PREVENT SCREENSHOT
            setupActivityListener()
        }
    }

    /**
     * Prevenir screenshot
     */
    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

}