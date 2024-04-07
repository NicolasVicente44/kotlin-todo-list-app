package ca.georgiancollege.comp3025_w24_assignment_4.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
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
import android.app.AlertDialog
import android.graphics.Paint
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem

/**
 * TodoItemDetailsActivity file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * this file contorls the details activitiy and its interaction with the main activity and the firebase realtime db, allowing the user
 * to create and update the todo or cancel editing it
 */



class ToDoItemDetailsActivity : Activity() {
    private lateinit var todoItemViewModel: TodoItemViewModel
    private lateinit var binding: ToDoItemDetailsBinding
    private var todoItemId: String? = null // Declare a variable to store the todo item ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ToDoItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        // Initialize TodoItemViewModel
        todoItemViewModel = TodoItemViewModel()

        // Retrieve todo item details from intent extras
        val title = intent.getStringExtra("TODO_TITLE")
        val description = intent.getStringExtra("TODO_DESCRIPTION")
        val dueDate = intent.getStringExtra("TODO_DUE_DATE")
        val status = intent.getBooleanExtra("TODO_STATUS", false)
        var hasDueDate = intent.getBooleanExtra("HAS_DUE_DATE", false)
        val pastDue = intent.getBooleanExtra("PAST_DUE", false)
        todoItemId = intent.getStringExtra("TODO_ID") // Retrieve the todo item ID
        val documentId = intent.getStringExtra("DOCUMENT_ID") // Retrieve the document ID

        // Create a new TodoItem object
        val todoItem = TodoItem(
            id = documentId ?: "", // Use an empty string if todoId is null
            title = title ?: "", // Use an empty string if title is null
            description = description ?: "", // Use an empty string if description is null
            dueDate = dueDate ?: "", // Use an empty string if dueDate is null
            status = status, // Use the provided status value
            hasDueDate = hasDueDate, // Use the provided hasDueDate value
            pastDue = pastDue // Use the provided pastDue value
        )

        binding.detailStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Set the text color and apply strike-through effect if the switch is checked (i.e., task is completed)
                binding.todoTitleDetails.setTextColor(ContextCompat.getColor(this, R.color.text_completed))
                binding.todoDetailsDescription.setTextColor(ContextCompat.getColor(this, R.color.text_completed))
                binding.todoTitleDetails.paintFlags = binding.todoTitleDetails.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.todoDetailsDescription.paintFlags = binding.todoDetailsDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Set the text color to normal if the switch is unchecked (i.e., task is not completed)
                binding.todoTitleDetails.setTextColor(ContextCompat.getColor(this, R.color.text_uncompleted))
                binding.todoDetailsDescription.setTextColor(ContextCompat.getColor(this, R.color.text_uncompleted))
                binding.todoTitleDetails.paintFlags = binding.todoTitleDetails.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.todoDetailsDescription.paintFlags = binding.todoDetailsDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        // Update EditText fields with todo item details
        binding.todoTitleDetails.setText(title)
        binding.todoDetailsDescription.setText(description)
        binding.todoDueDate.text = dueDate



        binding.detailStatusSwitch.post {
            binding.detailStatusSwitch.isChecked = status // Set the switch based on the status
        }

        binding.calendarStatusSwitch.post {
            binding.calendarStatusSwitch.isChecked = hasDueDate // Set the switch based on the status
            if (!hasDueDate) {
                binding.todoDueDate.text = ""
            }
        }

        // Set text color based on due date
        updateDueDateTextColor(binding.todoDueDate, dueDate)

        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        binding.cancelButton.setOnClickListener {
            showCancelConfirmationDialog()
        }



        binding.updateButton.setOnClickListener {
            if (todoItemId != null) {
                // Update the TodoItem object with the new values
                todoItem.title = binding.todoTitleDetails.text.toString()
                todoItem.description = binding.todoDetailsDescription.text.toString()
                todoItem.dueDate = binding.todoDueDate.text.toString()
                todoItem.status = binding.detailStatusSwitch.isChecked
                todoItem.hasDueDate = binding.calendarStatusSwitch.isChecked

                // Check if documentId is not null before calling showUpdateConfirmationDialog
                showUpdateConfirmationDialog(todoItem, todoItemId!!)
            } else {
                // Display an error message or handle the case where the documentId is null
                showToast("Unable to update the todo item. Document ID is missing.")
            }
        }



        // Set up the calendar view
        binding.calendarStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                animatePopUp(binding.calendarView)
                animatePopUp(binding.linearLayout2)
                hasDueDate = true

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())

                binding.todoDueDate.text = currentDate

                updateDueDateTextColor(binding.todoDueDate, currentDate)

                // Set up listener for the calendar view
                binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                    // Format the selected date
                    val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(Date(year - 1900, month, dayOfMonth))

                    // Update the due date TextView
                    binding.todoDueDate.text = selectedDate

                    // Update the text color of the due date TextView based on the selected date
                    updateDueDateTextColor(binding.todoDueDate, selectedDate)
                }
            } else {
                animatePopDown(binding.calendarView)
                animatePopDown(binding.linearLayout2)
            }
        }

        // Initially hide the layout if hasDueDate is false
        if (!hasDueDate) {
            binding.calendarView.visibility = View.INVISIBLE
            binding.linearLayout2.visibility = View.INVISIBLE
        }
    }


    // Function to show the delete confirmation dialog
    private fun showCancelConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Cancel Changes to Todo Item")
        builder.setMessage("Are you sure you want to cancel changes to this todo item?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes button, proceed with deletion
            cancelChange()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User clicked No button, dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun cancelChange() {
        if (todoItemId != null) {
            val title = intent.getStringExtra("TODO_TITLE")
            val description = intent.getStringExtra("TODO_DESCRIPTION")
            val dueDate = intent.getStringExtra("TODO_DUE_DATE")
            val status = intent.getBooleanExtra("TODO_STATUS", false)
            var hasDueDate = intent.getBooleanExtra("HAS_DUE_DATE", false)
            val pastDue = intent.getBooleanExtra("PAST_DUE", false)
            todoItemId = intent.getStringExtra("TODO_ID") // Retrieve the todo item ID
            val documentId = intent.getStringExtra("DOCUMENT_ID") // Retrieve the document ID

            binding.todoTitleDetails.setText(title)
            binding.todoDetailsDescription.setText(description)
            binding.todoDueDate.text = dueDate
            binding.detailStatusSwitch.isChecked = status
            binding.calendarStatusSwitch.isChecked = hasDueDate
            showToast("Todo item change cancelled")



        } else {
            // Log an error if todoItemId is null
            showToast("Todo item ID is null or failed to cancel")
        }
    }


    private fun showUpdateConfirmationDialog(todoItem: TodoItem, documentId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Todo Item")
        builder.setMessage("Are you sure you want to update this todo item?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes button, proceed with update
            updateTodoItem(todoItem, documentId)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User clicked No button, dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun updateTodoItem(todoItem: TodoItem, documentId: String) {
        todoItemId?.let {
            todoItemViewModel.updateTodoItem(
                todoItem,
                it,
                onSuccess = {
                    // Log success message
                    showToast("Todo updated successfully")
                    // Finish the activity to go back to the previous screen
                    val intent = Intent(this@ToDoItemDetailsActivity, MainActivity::class.java)
                    startActivity(intent)
                },
                onFailure = { exception ->
                    // Log error message
                    showToast("Todo update failed")
                }
            )
        }
    }

    // Function to show the delete confirmation dialog
    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Todo Item")
        builder.setMessage("Are you sure you want to delete this todo item?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes button, proceed with deletion
            deleteTodoItem()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User clicked No button, dismiss the dialog
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    // Function to delete the todo item
    private fun deleteTodoItem() {
        if (todoItemId != null) {
            // Call deleteTodoItem() method from TodoItemViewModel with the todo item ID
            todoItemViewModel.deleteTodoItem(
                todoItemId!!,
                // onSuccess: Action to perform when deletion is successful
                onSuccess = {
                    // Log success message
                    showToast("Todo deleted successfully")
                    val intent = Intent(this@ToDoItemDetailsActivity, MainActivity::class.java)
                    startActivity(intent)
                },
                // onFailure: Action to perform when deletion fails
                onFailure = { exception ->
                    // Log error message
                    showToast("Todo deletion failed")
                }
            )
        } else {
            // Log an error if todoItemId is null
            showToast("Todo item ID is null")
        }
    }

    private fun animatePopUp(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cx = view.width / 2
            val cy = view.height / 2
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
            view.visibility = View.VISIBLE
            anim.start()
        } else {
            view.visibility = View.VISIBLE
        }
    }

    private fun animatePopDown(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cx = view.width / 2
            val cy = view.height / 2
            val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
            anim.start()
        } else {
            view.visibility = View.INVISIBLE
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
            intent.putExtra(
                "UPDATED_TODO_STATUS",
                (findViewById<Switch>(R.id.detailStatusSwitch)).isChecked
            )
            setResult(RESULT_OK, intent)
            finish() // Finish the activity
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
