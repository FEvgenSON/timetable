package com.fevgenson.timetable.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fevgenson.timetable.R
import com.fevgenson.timetable.room.DBHolder
import com.fevgenson.timetable.room.entity.Lesson
import com.fevgenson.timetable.time.TimeChecker
import com.fevgenson.timetable.time.TimeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class NotificationService : LifecycleService() {
    private val dayLessons = MutableLiveData<List<Lesson>>()
    private var currentLessonPosition = -1
    private val disposable = CompositeDisposable()

    companion object {
        private const val CHANNEL_ID = "TIMETABLE_NOTIFICATION_CHANNEL_ID"
        private const val CHANNEL_NAME = "lesson notification"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        TimeChecker.init()
        TimeObserver.init(this)
        disposable.add(TimeObserver.minuteObservable.subscribe {
            if (currentLessonPosition == -1) return@subscribe
            if (dayLessons.value!![currentLessonPosition].isNow()) {
                updateNotification(dayLessons.value!![currentLessonPosition].createNotification())
            } else {
                currentLessonPosition = findLesson(dayLessons.value!!)
                if (currentLessonPosition == -1) {
                    updateNotification(getEmptyNotification())
                } else {
                    updateNotification(dayLessons.value!![currentLessonPosition].createNotification())
                }
            }
        })
        disposable.add(TimeObserver.dayObservable.subscribe {
            loadLessons()
        })
        dayLessons.observe(this, Observer {
            currentLessonPosition = findLesson(it)
            val notification =
                if (currentLessonPosition == -1) getEmptyNotification() else it[currentLessonPosition].createNotification()
            startForeground(1, notification)
        })
        createChannel()
        loadLessons()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun Lesson.createNotification(): Notification {
        val title = if (type == "") name else "($type) $name"
        val currentTime = TimeChecker.getCurrentTime()
        val firstTime = TimeChecker.getFirstTime(time)
        val secondTime = TimeChecker.getSecondTime(time)
        var mainText = when {
            currentTime < firstTime -> getString(
                R.string.before_start,
                TimeChecker.minus(firstTime, currentTime)
            )
            currentTime > secondTime -> getString(
                R.string.after_end,
                TimeChecker.minus(currentTime, secondTime)
            )
            else -> getString(
                R.string.before_end,
                TimeChecker.minus(secondTime, currentTime)
            )
        }
        if (!building.isBlank() || !classroom.isBlank()) {
            mainText += ", " + if (building.isBlank()) classroom else
                getString(
                    R.string.main_with_additional,
                    classroom,
                    building
                )
        }
        return NotificationCompat.Builder(this@NotificationService, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(mainText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private fun getEmptyNotification(): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.freedom))
            .setContentText(getString(R.string.no_lesson_today))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager =
                getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun loadLessons() {
        DBHolder.init(this)
        disposable.add(DBHolder.database.lessonDao()
            .getLessons(TimeChecker.currentWeekType, TimeChecker.currentDay)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { dayLessons.value = it }
        )
    }

    private fun findLesson(lessons: List<Lesson>): Int {
        lessons.forEachIndexed { index, lesson ->
            if (lesson.isNow()) {
                return index
            }
        }
        return -1
    }

    private fun Lesson.isNow(): Boolean {
        val currentTime = TimeChecker.getCurrentTime()
        val secondTime = TimeChecker.getSecondTime(time)
        return currentTime < secondTime
    }

    private fun updateNotification(notification: Notification) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        disposable.clear()
    }
}