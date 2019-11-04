package com.kawa1lg1rl.lotto.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kawa1lg1rl.lotto.App
import com.kawa1lg1rl.lotto.R
import com.kawa1lg1rl.lotto.utils.LottoResultUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.*

/**
 * Created by kawa1lg1rl on 2019-10-30
 */

class NotifyService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var context = this
        var scope = CoroutineScope(Dispatchers.Default)

        scope.async {
            while(true) {
                App.isConnected()?.let {
                    if(it == true) {
                        LottoResultUtils.instance.changeCurrentLottoResult()
                    }
                }

                var lottoResult = LottoResultUtils.instance.getCurrentLottoResult()

                var builder = NotificationCompat.Builder(sourcecontext, "kawa1lg1rlNoti")
                    .setSmallIcon(R.drawable.number_1)
                    .setContentTitle("${lottoResult.count}회차")
                    .setContentText("당첨번호 : ${lottoResult.numbers.joinToString(", ")}")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)

                createNotificationChannel()

                with(NotificationManagerCompat.from(context)) {
                    notify(1234, builder.build())
                }
                delay(60*60*1000)
            }
        }


        return START_STICKY
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "kawa1lg1rlNoti"
            val descriptionText = "noti Test Contents"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("kawa1lg1rlNoti", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        Log.d("kawa1lg1rl_service", "onDestroy")
        super.onDestroy()

    }

}