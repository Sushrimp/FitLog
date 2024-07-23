package edu.bluejack23_2.fitlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_2.fitlog.databinding.ActivityAddPersonalRecordBinding
import edu.bluejack23_2.fitlog.handler.PersonalRecordHandler

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

        handler.
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}