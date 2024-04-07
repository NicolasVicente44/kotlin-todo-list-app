package ca.georgiancollege.comp3025_w24_assignment_4.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.comp3025_w24_assignment_4.R
import ca.georgiancollege.comp3025_w24_assignment_4.activities.ToDoItemDetailsActivity
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ToDoItemBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import java.text.SimpleDateFormat
import java.util.*
/**
 * RecyclerViewAdapter file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * this file allows the main activity to load the todos from the realtime firebase database into a recycler view
 * and handles loading the deatails page when the user clicks on the edit button in the list
 */

class RecyclerViewAdapter(
    private var dataSet: List<TodoItem>,
    private val context: Context,
    private val todoItemViewModel: TodoItemViewModel
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = dataSet[position]
        holder.binding.apply {
            val truncatedTitle = if (todo.title.length > 10) {
                "${todo.title.substring(0, 18)}..." // Truncate if title length exceeds 10
            } else {
                todo.title // Keep the original title if it's shorter than 10 characters
            }
            todoTitle.text = truncatedTitle
            // Bind description with max length of 10 characters
            val truncatedDescription = if (todo.description.length > 10) {
                "${todo.description.substring(0, 20)}..." // Truncate if description length exceeds 10
            } else {
                todo.description // Keep the original description if it's shorter than 10 characters
            }

            todoDescription.text = truncatedDescription

            todoDueDate.text = todo.dueDate


            todoDueDate.text = todo.dueDate

            // Set switch state based on the status of the TodoItem
            todoMainStatusSwitch.isChecked = todo.status

            // Update the strike-through effect and text color based on initial status
            if (todo.status) {
                todoTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.text_completed)
                )
                todoDescription.setTextColor(
                    ContextCompat.getColor(context, R.color.text_completed)
                )
                todoTitle.paintFlags =
                    todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                todoDescription.paintFlags =
                    todoDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                todoTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.text_uncompleted)
                )
                todoDescription.setTextColor(
                    ContextCompat.getColor(context, R.color.text_uncompleted)
                )
                todoTitle.paintFlags =
                    todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                todoDescription.paintFlags =
                    todoDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            // Set click listener for the completion status indicator (e.g., a CheckBox or a Switch)
            todoMainStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
                // Update the completion status of the corresponding TodoItem
                todo.id?.let {
                    todo.id?.let { todoId ->
                        todoItemViewModel.updateTodoStatus(it, isChecked,
                            onSuccess = {
                                // Update UI based on the new completion status
                                if (isChecked) {
                                    // Set text color and apply strike-through effect if task is completed
                                    todoTitle.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.text_completed
                                        )
                                    )
                                    todoDescription.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.text_completed
                                        )
                                    )
                                    todoTitle.paintFlags =
                                        todoTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                    todoDescription.paintFlags =
                                        todoDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                } else {
                                    // Set text color to normal and remove strike-through effect if task is not completed
                                    todoTitle.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.text_uncompleted
                                        )
                                    )
                                    todoDescription.setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.text_uncompleted
                                        )
                                    )
                                    todoTitle.paintFlags =
                                        todoTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                    todoDescription.paintFlags =
                                        todoDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                            },
                            onFailure = { exception ->
                                // Handle error if unable to update completion status
                                Log.e(
                                    "RecyclerViewAdapter",
                                    "Failed to update completion status: ${exception.message}"
                                )
                            }
                        )
                    }
                }
            }

            // Call function to update due date text color
            updateDueDateTextColor(todoDueDate, todo.dueDate)

            // Set click listener for edit button
            holder.binding.svgImageView.setOnClickListener {
                // Retrieve the auto-generated ID of the todo item
                val todoId = todo.id ?: ""

                // Call getTodoItemById to fetch the details of the todo item using its ID
                todoItemViewModel.getTodoItemById(todoId,
                    onSuccess = { todoItem ->
                        // Navigate to ToDoItemDetailsActivity and pass todo item details
                        val intent = Intent(context, ToDoItemDetailsActivity::class.java).apply {
                            if (todoItem != null) {
                                putExtra("TODO_ID", todoItem.id) // Pass the document ID
                                putExtra("TODO_TITLE", todoItem.title)
                                putExtra("TODO_DESCRIPTION", todoItem.description)
                                putExtra("TODO_DUE_DATE", todoItem.dueDate)
                                putExtra("TODO_STATUS", todoItem.status)
                                putExtra("HAS_DUE_DATE", todoItem.hasDueDate)
                                putExtra("PAST_DUE", todoItem.pastDue)
                            }
                        }
                        context.startActivity(intent)
                    },
                    onFailure = { exception ->
                        // Handle error if unable to fetch todo item details
                        Log.e(
                            "RecyclerViewAdapter",
                            "Failed to fetch todo item details: ${exception.message}"
                        )
                    }
                )
            }
        }
    }

    fun updateTodoList(newTodos: List<TodoItem>) {
        dataSet = newTodos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun updateDueDateTextColor(todoDueDate: TextView, date: String?) {
        if (date != null && date.isNotEmpty()) {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val today = date == currentDate

            if (today) {
                todoDueDate.setTextColor(ContextCompat.getColor(context, R.color.text_due_today))
            } else {
                val pastDue = sdf.parse(date)?.before(Date()) ?: false

                if (pastDue) {
                    todoDueDate.setTextColor(ContextCompat.getColor(context, R.color.text_past_due))
                } else {
                    todoDueDate.setTextColor(ContextCompat.getColor(context, R.color.text_normal))
                }
            }
        }
    }
}

