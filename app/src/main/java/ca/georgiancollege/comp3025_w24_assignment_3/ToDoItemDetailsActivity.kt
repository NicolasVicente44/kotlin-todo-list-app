package ca.georgiancollege.comp3025_w24_assignment_3
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.ToDoItemDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoItemDetailsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Set the navigation bar icons color to gray
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        super.onCreate(savedInstanceState)
        val binding = ToDoItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            binding.todoDueDate.setText(selectedDate)
            updateDueDateTextColor(binding.todoDueDate, selectedDate)
        }

        // Retrieve todo item details from intent extras
        val title = intent.getStringExtra("TODO_TITLE")
        val description = intent.getStringExtra("TODO_DESCRIPTION")
        val dueDate = intent.getStringExtra("TODO_DUE_DATE")
        val status = intent.getBooleanExtra("TODO_STATUS", false)

        // Update EditText fields with todo item details
        binding.todoTitleDetails.setText(title)
        binding.todoDetailsDescription.setText(description)
        binding.todoDueDate.setText(dueDate)
        binding.detailStatusSwitch.isChecked = status

        // Set text color based on due date
        updateDueDateTextColor(binding.todoDueDate, dueDate)

        // Enable the back button in the action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle back button click event
        if (item.itemId == android.R.id.home) {
            // Pass the current state of the switch back to the calling activity
            val intent = Intent()
            intent.putExtra("UPDATED_TODO_STATUS", (findViewById<Switch>(R.id.detailStatusSwitch)).isChecked)
            setResult(RESULT_OK, intent)
            finish() // Finish the activity
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
