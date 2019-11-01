package com.kawa1lg1rl.lotto

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.view.LayoutInflater
import android.view.Menu
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kawa1lg1rl.lotto.data.MySharedPreferences
import io.realm.Realm

class App : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: App? = null
        var lottoNumbersPrefs: MySharedPreferences? = null

        fun context() : Context {
            return instance!!.applicationContext
        }

        fun getGlideObject(context: Context) : RequestManager {
            return Glide.with(context)
        }
        
        fun getGlideObject(activity: Activity) : RequestManager {
            return Glide.with(activity)
        }

        fun isConnected() : Boolean {
            val cm = context().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            val isConnected: Boolean? = activeNetwork?.isConnected

            return if(isConnected == null) false else isConnected
        }
    }

    override fun onCreate() {
        // realm 사용하려면 꼭 해줘야함.
        Realm.init(this)

        lottoNumbersPrefs = MySharedPreferences(R.string.prefsGeneratedNumbers)

        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        super.onCreate()

    }
}