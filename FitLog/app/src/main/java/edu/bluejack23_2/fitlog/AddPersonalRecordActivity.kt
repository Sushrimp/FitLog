package edu.bluejack23_2.fitlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.fitlog.databinding.ActivityAddPersonalRecordBinding
import edu.bluejack23_2.fitlog.handler.PersonalRecordHandler
import edu.bluejack23_2.fitlog.models.PRAdapter
import edu.bluejack23_2.fitlog.models.PersonalRecordDetail

class AddPersonalRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPersonalRecordBinding
    private lateinit var handler: PersonalRecordHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPersonalRecordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = PersonalRecordHandler()

        // Action Bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Add Personal Record"

        binding.addPRButton.setOnClickListener {
            val showForm = AddPersonalRecordFragment()
            showForm.show(supportFragmentManager, "showForm")
        }

        setPRDetails()
    }

    private fun setPRDetails() {
        handler.getUserPersonalRecord { personalRecords, message ->
            if (personalRecords != null) {
                val prSets = personalRecords.toMutableList()
                val adapter = PRAdapter(prSets)
                binding.prs.adapter = adapter
                binding.prs.layoutManager = LinearLayoutManager(this)
            } else {
                // Handle the case where personalRecords is null
                // You might want to show a message or log the error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}