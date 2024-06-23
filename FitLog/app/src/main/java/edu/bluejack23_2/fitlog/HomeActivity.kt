package edu.bluejack23_2.fitlog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import edu.bluejack23_2.fitlog.handler.ScheduleHandler
import edu.bluejack23_2.fitlog.handler.UserHandler
import org.jetbrains.annotations.Async.Schedule

class HomeActivity : AppCompatActivity() {

    private lateinit var profilePicture : ImageView
    private lateinit var scheduleContainer : CardView
    private lateinit var scheduleLinearLayout: LinearLayout
    private lateinit var scheduleText : TextView

    private lateinit var userHandler : UserHandler
    private lateinit var scheduleHandler: ScheduleHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        userHandler = UserHandler()
        scheduleHandler = ScheduleHandler()

        profilePicture = findViewById(R.id.profilePicture_Home)
        userHandler.setProfilePicture(profilePicture)

        profilePicture.setOnClickListener{
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        scheduleContainer = findViewById(R.id.scheduleContainer)
        scheduleLinearLayout = findViewById(R.id.scheduleLinearLayout)
        scheduleText = findViewById(R.id.scheduleText)
        scheduleHandler.getTodaySchedule { schedule ->
            runOnUiThread {
                if (schedule == null) {
                    val addScheduleButton = Button(this)
                    addScheduleButton.text = "Add Schedule"
                    addScheduleButton.setOnClickListener {
                        // add schedule function
                    }
                    scheduleLinearLayout.addView(addScheduleButton)
                } else {
                    val bodyPartsList = schedule.bodyParts
                    val bodyParts = bodyPartsList?.map { it.bodyPart } ?: listOf()
                    val bodyPartsText = TextView(this)
                    val tempString = "Today's Target: " + bodyParts.joinToString(", ")
                    bodyPartsText.text = tempString
                    scheduleLinearLayout.addView(bodyPartsText)
                }
            }
        }
    }

}