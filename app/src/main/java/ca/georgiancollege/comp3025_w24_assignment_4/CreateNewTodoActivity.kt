package ca.georgiancollege.comp3025_w24_assignment_3

/**
 * create new todo class that handles the create a new todo funcitonality once the user clicks on the
 * floating actions button in the main activity
 */

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.CreateNewTodoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNewTodoActivity : Activity() {
    private lateinit var binding: CreateNewTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set the navigation bar icons color to gray
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        super.onCreate(savedInstanceState)
        binding = CreateNewTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            binding.todoDueDate.setText(selectedDate)
            updateDueDateTextColor(binding.todoDueDate, selectedDate)
        }
    }

    private fun updateDueDateTextColor(todoDueDate: TextView, date: String?) {
        if (date != null && date.isNotEmpty()) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            Log.d("Dates", "Current Date: $currentDate, Selected Date: $date")

            val today = date == currentDate
            Log.d("Dates", "Today: $today")

            if (today) {
                todoDueDate.setTextColor(ContextCompat.getColor(this, R.color.text_due_today))
            } else {
                // Handle past due logic if needed
                val pastDue = sdf.parse(date)?.before(Date()) ?: false
                Log.d("Dates", "Past Due: $pastDue")

                if (pastDue) {
                    todoDueDate.setTextColor(ContextCompat.getColor(this, R.color.text_past_due))
                } else {
                    todoDueDate.setTextColor(ContextCompat.getColor(this, R.color.text_normal))
                }
            }
        }
    }
}


