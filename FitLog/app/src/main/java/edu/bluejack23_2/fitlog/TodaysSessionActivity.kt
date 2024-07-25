package edu.bluejack23_2.fitlog

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import edu.bluejack23_2.fitlog.databinding.ActivityTodaysSessionBinding
import edu.bluejack23_2.fitlog.handler.ScheduleHandler

class TodaysSessionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTodaysSessionBinding
    private lateinit var handler: ScheduleHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodaysSessionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = ScheduleHandler()

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Today's Session"

        handler.getTodaySchedule { schedule ->
            runOnUiThread {
                if (schedule == null) {
                    Toast.makeText(this, "You don't have any schedule today. How did you ge there?", Toast.LENGTH_SHORT).show()
                } else {
                    val bodyPartsList = schedule.bodyParts
                    val bodyParts = bodyPartsList?.map { it.bodyPart } ?: listOf()
                    binding.todaysTargetLbl.text = "Today's Target: ${bodyParts.toString()}"
                }
            }
        }

        binding.timerButton.setOnClickListener {
            val showForm = TimerFragment()
            showForm.show(supportFragmentManager, "showForm")
        }

        binding.timerButton.setOnLongClickListener {
            val showForm = EditTimerFragment()
            showForm.show(supportFragmentManager, "showForm")
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}