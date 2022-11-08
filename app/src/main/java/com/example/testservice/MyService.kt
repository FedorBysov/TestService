package com.example.testservice

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

class MyService:android.app.Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate" )
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val start = intent?.getIntExtra(PUT_EXTRA, 0) ?: 0
        coroutineScope.launch {
            for (i in start until start + 100) {
                delay(1000)
                log("Timer $i")
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy" )

    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message:String){
        Log.d("SERVICE_TAG", "MyService: $message")
    }


    companion object{

        private const val PUT_EXTRA ="start"

        fun newIntent(context: Context, start:Int):Intent{
            return Intent(context, MyService::class.java).apply {
                putExtra(PUT_EXTRA, start)
            }
        }
    }

}