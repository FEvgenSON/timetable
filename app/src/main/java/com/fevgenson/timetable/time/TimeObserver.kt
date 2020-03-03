package com.fevgenson.timetable.time

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.Observable

object TimeObserver {
    private val minuteObserveFilter = IntentFilter(Intent.ACTION_TIME_TICK)
    private val dayObserveFilter = IntentFilter(Intent.ACTION_DATE_CHANGED)
    lateinit var minuteObservable: Observable<Boolean>
    lateinit var dayObservable: Observable<Boolean>

    fun init(applicationContext: Context) {
        minuteObservable = Observable.fromPublisher {
            val minuteChangeReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    when (intent?.action.toString()) {
                        Intent.ACTION_TIME_TICK -> it.onNext(true)
                    }
                }
            }
            applicationContext.registerReceiver(minuteChangeReceiver, minuteObserveFilter)
        }
        dayObservable = Observable.fromPublisher {
            val dayChangeReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    when (intent?.action.toString()) {
                        Intent.ACTION_DATE_CHANGED -> {
                            TimeChecker.init()
                            it.onNext(true)
                        }
                    }
                }
            }
            applicationContext.registerReceiver(dayChangeReceiver, dayObserveFilter)
        }
    }
}