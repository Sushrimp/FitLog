package edu.bluejack23_2.fitlog

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.fitlog.databinding.ActivityAddScheduleBinding
import edu.bluejack23_2.fitlog.databinding.ActivityHomeBinding
import edu.bluejack23_2.fitlog.handler.ForumHandler
import edu.bluejack23_2.fitlog.handler.ScheduleHandler
import edu.bluejack23_2.fitlog.handler.UserHandler
import edu.bluejack23_2.fitlog.models.Forum
import edu.bluejack23_2.fitlog.models.ForumAdapter
import edu.bluejack23_2.fitlog.models.ItemAdapter
import org.jetbrains.annotations.Async.Schedule

class HomeActivity : AppCompatActivity(), ForumAdapter.OnForumClickListener {

    private lateinit var userHandler : UserHandler
    private lateinit var scheduleHandler: ScheduleHandler
    private lateinit var forumHandler: ForumHandler

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        popupMenu()

        userHandler = UserHandler()
        scheduleHandler = ScheduleHandler()
        forumHandler = ForumHandler()

        userHandler.setProfilePicture(binding.profilePictureHome)

        binding.profilePictureHome.setOnClickListener{
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        scheduleHandler.getTodaySchedule { schedule ->
            runOnUiThread {
                if (schedule == null) {
                    val addScheduleButton = Button(this).apply {
                        text = "Add Schedule"
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    }
                    addScheduleButton.setOnClickListener {
                        val intent = Intent(this@HomeActivity, AddScheduleActivity::class.java)
                        startActivity(intent)
                    }
                    binding.scheduleLinearLayout.addView(addScheduleButton)
                    binding.scheduleContainer.setCardBackgroundColor(Color.rgb(128, 0, 0))
                } else {
                    val bodyPartsList = schedule.bodyParts
                    val bodyParts = bodyPartsList?.map { it.bodyPart } ?: listOf()
                    val bodyPartsText = TextView(this).apply {
                        text = "Today's Target: ${bodyParts.toString()}"
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    binding.scheduleLinearLayout.addView(bodyPartsText)
                    val startActivityButton = Button(this).apply {
                        text = "Start Activity"
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    }
                    startActivityButton.setOnClickListener {
                        val intent = Intent(this@HomeActivity, TodaysSessionActivity::class.java)
                        startActivity(intent)
                    }
                    binding.scheduleLinearLayout.addView(startActivityButton)
                }
            }
        }

        setForumList();
    }

    private fun popupMenu() {
        val popupMenu = PopupMenu(applicationContext, binding.addButton)
        popupMenu.inflate(R.menu.add_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.nav_schedule -> {
                    val intent = Intent(this@HomeActivity, AddScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_pr -> {
                    val intent = Intent(this@HomeActivity, AddPersonalRecordActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> true
            }
        }

        binding.addButton.setOnClickListener {
            println("Test")
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupMenu.show()
            }
            true
        }
    }

    private fun setForumList() {
        binding.forumList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.forumList.setHasFixedSize(true)

        forumHandler.getAllForum { forums ->
            if (forums != null) {
                val adapter = ForumAdapter(forums, this)
                binding.forumList.adapter = adapter
                println("Forum list: $forums")
            } else {
                println("No forums found or an error occurred.")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setForumList()
    }

    override fun onForumClick(forumId: String) {
        val intent = Intent(this, ForumActivity::class.java)
        intent.putExtra("forumId", forumId)
        startActivity(intent)
    }
}