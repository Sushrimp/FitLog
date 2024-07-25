package edu.bluejack23_2.fitlog

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import edu.bluejack23_2.fitlog.databinding.FragmentTimerBinding
import edu.bluejack23_2.fitlog.handler.ScheduleHandler
import java.util.Locale

class TimerFragment : DialogFragment() {
    private var _binding: FragmentTimerBinding? = null
    private lateinit var timer: CountDownTimer

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = this.context
        var myTimer = 0
        if(context != null) {
            val sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            myTimer = sharedPreferences.getInt("myTimer", 0)
            val editor = sharedPreferences.edit()
            if (myTimer == 0) {
                editor.putInt("myTimer", 152500)
                editor.apply()
            }
            myTimer = sharedPreferences.getInt("myTimer", 0)
            timer = object : CountDownTimer(myTimer.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val minutes = ((millisUntilFinished / 1000) % 3600) / 60
                    val seconds = (millisUntilFinished / 1000) % 60
                    val timeFormatted =
                        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                    binding.timerLbl.text = timeFormatted
                }

                override fun onFinish() {
                    binding.timerLbl.text = "00:00"
                    Toast.makeText(context, "Time's up", Toast.LENGTH_SHORT).show()
                }
            }.start()

            binding.stopBtn.setOnClickListener() {
                timer.cancel()
                dismiss()
            }
        }

    }
}