package edu.bluejack23_2.fitlog

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import edu.bluejack23_2.fitlog.databinding.ActivityAddScheduleBinding
import edu.bluejack23_2.fitlog.handler.ScheduleHandler
import edu.bluejack23_2.fitlog.models.BodyPart
import edu.bluejack23_2.fitlog.models.BodyPartSpinnerAdapter
import edu.bluejack23_2.fitlog.models.Item
import edu.bluejack23_2.fitlog.models.ItemAdapter
import java.util.ArrayList
import java.util.Calendar

class AddScheduleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddScheduleBinding
    private lateinit var scheduleHandler: ScheduleHandler

    private lateinit var bodyPartList: ArrayList<Item>

    private lateinit var spinnerAdapter: BodyPartSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScheduleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add Schedule"

        scheduleHandler = ScheduleHandler()

        binding.datePickerButton.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    binding.datePickerText.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        setRecycleView()
    }

    fun setRecycleView(){
        binding.bodyParts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.bodyParts.setHasFixedSize(true)

        bodyPartList = ArrayList<Item>()
        bodyPartList.add(Item("Chest", R.drawable.chest))
        bodyPartList.add(Item("Back", R.drawable.back))
        bodyPartList.add(Item("Leg", R.drawable.leg))
        bodyPartList.add(Item("Bicep", R.drawable.bicep))
        bodyPartList.add(Item("Tricep", R.drawable.tricep))

        binding.bodyParts.adapter = ItemAdapter(bodyPartList)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}