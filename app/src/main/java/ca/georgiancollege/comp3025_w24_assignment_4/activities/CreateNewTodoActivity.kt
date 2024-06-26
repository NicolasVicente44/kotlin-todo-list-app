package ca.georgiancollege.comp3025_w24_assignment_4.activities


/**
 * CreateNewTodoActivity file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * create new todo class that handles the create a new todo funcitonality once the user clicks on the
 * floating action button in the main activity to create a new todo, calls the methods to intereact with the database
 * */




import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import ca.georgiancollege.comp3025_w24_assignment_4.R
import ca.georgiancollege.comp3025_w24_assignment_4.data.DataManager
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.CreateNewTodoBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateNewTodoActivity : Activity() {
    private lateinit var binding: CreateNewTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //view bidning
        super.onCreate(savedInstanceState)
        binding = CreateNewTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //android navitgation buttons changes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        // Set initial state of calendar based on hasDueDate switch
        binding.todoCreateCalendarSwitch.isChecked = false
        binding.calendarView.isEnabled = false
        binding.calendarView.visibility = View.INVISIBLE
        binding.linearLayout2.visibility = View.INVISIBLE


        // Attach click listener to the create button
        binding.createButton.setOnClickListener {
            createTodo()
        }

        //cancel button listener
        binding.createActivtiyCancelButton.setOnClickListener {
            cancelTodoCreation()
        }

        // Set up the calendar view
        binding.todoCreateCalendarSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                animatePopUp(binding.calendarView)
                animatePopUp(binding.linearLayout2)

                // Set the current date to the due date TextView
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())
                binding.todoCreateDueDate.text = currentDate
                updateDueDateTextColor(binding.todoCreateDueDate, currentDate)
            } else {
                //close calendar view
                animatePopDown(binding.calendarView)
                animatePopDown(binding.linearLayout2)
            }
        }

        //listener for calendar view and date setter
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate =
                String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)
            binding.todoCreateDueDate.setText(selectedDate)
            updateDueDateTextColor(binding.todoCreateDueDate, selectedDate)
        }
    }

    //animation for calendar view
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

    //animation for calendar view
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

    //calling of todo creation in realtime db
    private fun createTodo() {
        // Retrieve user-entered data
        val title = binding.todoCreateTitle.text.toString()
        val description = binding.todoCreateDescription.text.toString()
        val hasDueDate = binding.todoCreateCalendarSwitch.isChecked

        val dueDate = if (hasDueDate) {
            binding.todoCreateDueDate.text.toString()
        } else {
            ""
        }

        // Validate due date format if it is not empty
        if (hasDueDate && dueDate.isNotEmpty() && !validateDateFormat(dueDate)) {
            showToast("Please enter due date in the format yyyy-MM-dd or use the calendar.")
            return
        }

        // Check if any of the required fields are empty
        if (title.isEmpty()) {
            showToast("Please add a title.")
            return
        }

        // Create a TodoItem object
        val todoItem = TodoItem(
            title = title,
            description = description,
            dueDate = dueDate,
            hasDueDate = hasDueDate
        )

        // Instantiate the TodoItemViewModel with the DataManager instance
        val todoItemViewModel = TodoItemViewModel(DataManager())

        // Call the addTodoItem method to save the todo item
        todoItemViewModel.addTodoItem(
            todoItem,
            {
                // Handle success scenario here
                showToast("Todo created successfully")
                val intent = Intent(this@CreateNewTodoActivity, MainActivity::class.java)
                startActivity(intent)
            },
            { exception ->
                // Handle failure scenario here
                showToast("Failed to create todo: ${exception.message}")
            }
        )
    }


    //cancel button handling
    private fun cancelTodoCreation() {
        // Reset title and description fields
        binding.todoCreateTitle.text.clear()
        binding.todoCreateDescription.text.clear()



        // Reset calendar switch to unchecked and disable calendar view
        binding.todoCreateCalendarSwitch.isChecked = false
        binding.calendarView.isEnabled = false

        // Clear due date text
        binding.todoCreateDueDate.text = ""

        // Show a toast message indicating cancellation
        showToast("Todo creation canceled")
    }

    //date validation
    private fun validateDateFormat(date: String): Boolean {
        val regex = """^\d{4}-\d{2}-\d{2}$""".toRegex()
        return regex.matches(date)
    }


    //show toast pop up mesasges
    private fun showToast(message: String) {
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 100) // Adjust the Y offset as needed
        toast.show()
    }


    //update text colour of the date based on the current date function
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

