package com.example.testservice

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyJobIntentService : JobIntentService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var id = 1


    override fun onCreate() {
        super.onCreate()
        log("onCreate")

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notifications = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(id, notifications)

    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent")
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

//    override fun onHandleIntent(intent: Intent?) {
//        log("onHandleIntent")
//        for (i in 0 until 10) {
//            Thread.sleep(1000)
//            log("Timer $i")
//        }
//    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        log("onStartCommand")
//        coroutineScope.launch {
//            for (i in 0 until 3) {
//                delay(1000)
//                log("Timer $i")
//            }
////            stopSelf()
//        }
//        return START_STICKY
//    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")

    }



    private fun log(message: String) {
        Log.d("FOREGROUND_SERVICE_TAG", "MyJobIntentService: $message")
    }


    companion object {

        private const val PAGE = "page"
        private const val JOB_ID = 145
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"

        private const val PUT_EXTRA = "start"

        fun newIntent(context: Context, page:Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }

        fun enqueue(context:Context, page:Int){
           JobIntentService.enqueueWork(context, MyJobIntentService::class.java, JOB_ID,
           newIntent(context, page)
           )
        }
    }

}