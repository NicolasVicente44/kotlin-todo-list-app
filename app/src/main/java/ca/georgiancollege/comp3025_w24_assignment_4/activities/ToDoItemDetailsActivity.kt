package ca.georgiancollege.comp3025_w24_assignment_4.activities

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import ca.georgiancollege.comp3025_w24_assignment_4.R
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ToDoItemDetailsBinding
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ToDoItemDetailsActivity : Activity() {
    private lateinit var todoItemViewModel: TodoItemViewModel
    private lateinit var binding: ToDoItemDetailsBinding
    private var todoItemId: String? = null // Declare a variable to store the todo item ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ToDoItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        // Initialize TodoItemViewModel
        todoItemViewModel = TodoItemViewModel()

        // Retrieve todo item details from intent extras
        val title = intent.getStringExtra("TODO_TITLE")
        val description = intent.getStringExtra("TODO_DESCRIPTION")
        val dueDate = intent.getStringExtra("TODO_DUE_DATE")
        val status = intent.getBooleanExtra("TODO_STATUS", false)
        val hasDueDate = intent.getBooleanExtra("HAS_DUE_DATE", false)
        val pastDue = intent.getBooleanExtra("PAST_DUE", false)
        todoItemId = intent.getStringExtra("TODO_ID") // Retrieve the todo item ID

        // Update EditText fields with todo item details
        binding.todoTitleDetails.setText(title)
        binding.todoDetailsDescription.setText(description)
        binding.todoDueDate.setText(dueDate)
        binding.detailStatusSwitch.isChecked = status

        // Set text color based on due date
        updateDueDateTextColor(binding.todoDueDate, dueDate)

        // Set OnClickListener for delete button
        binding.deleteButton.setOnClickListener {
            if (todoItemId != null) {
                // Call deleteTodoItem() method from TodoItemViewModel with the todo item ID
                todoItemViewModel.deleteTodoItem(
                    todoItemId!!,
                    // onSuccess: Action to perform when deletion is successful
                    onSuccess = {
                        // Log success message
                        Log.d("ToDoItemDetails", "Todo item deleted successfully")
                        showToast("Todo deleted successfully")
                        val intent = Intent(this@ToDoItemDetailsActivity, MainActivity::class.java)
                        startActivity(intent)                    },
                    // onFailure: Action to perform when deletion fails
                    onFailure = { exception ->
                        // Log error message
                        Log.e("ToDoItemDetails", "Failed to delete todo item: ${exception.message}")
                        showToast("Todo deletion failed")
                    }
                )
            } else {
                // Log an error if todoItemId is null
                Log.e("ToDoItemDetails", "Todo item ID is null")
            }
        }
    }

    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 100) // Adjust the Y offset as needed
        toast.show()
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
