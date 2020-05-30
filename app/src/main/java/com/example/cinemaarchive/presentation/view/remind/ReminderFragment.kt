package com.example.cinemaarchive.presentation.view.remind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.view.remind.NotifyWork.Companion.NOTIFICATION_ID
import com.example.cinemaarchive.presentation.view.remind.NotifyWork.Companion.NOTIFICATION_WORK
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.remind_fragment.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val FILM_REMIND_FRAGMENT_TAG = "FILM_REMIND_FRAGMENT"

class ReminderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        return inflater.inflate(R.layout.remind_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film: Film? = arguments?.getParcelable("filmRemind")

        done_fab.setOnClickListener {
            addScheduleNotification(film)
        }
    }

    fun newInstance(film: Film): ReminderFragment {
        val myFragment = ReminderFragment()
        val args = Bundle()
        args.putParcelable("filmRemind", film)
        myFragment.arguments = args
        return myFragment
    }

    private fun addScheduleNotification(film: Film?) {
        val customCalendar = Calendar.getInstance()
        customCalendar.set(
            date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
        )
        val customTime = customCalendar.timeInMillis
        val currentTime = System.currentTimeMillis()


        if (customTime > currentTime) {
            val data = Data.Builder()
                .putInt(NOTIFICATION_ID, 0)
                .build()

            val delay = customTime - currentTime
            scheduleNotification(delay, data)
            showDoneMassage(customCalendar)
        } else {
            showErrorMassage()
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager.beginUniqueWork(
            NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork
        ).enqueue()
    }

    private fun showDoneMassage(customCalendar: Calendar) {
        val titleNotificationSchedule = getString(R.string.notification_schedule_title)
        val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
        Snackbar.make(
            coordinator_reminder,
            titleNotificationSchedule + SimpleDateFormat(
                patternNotificationSchedule, Locale.getDefault()
            ).format(customCalendar.time).toString(),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showErrorMassage() {
        val errorNotificationSchedule = getString(R.string.notification_schedule_error)
        Snackbar.make(coordinator_reminder, errorNotificationSchedule, Snackbar.LENGTH_LONG).show()
    }
}