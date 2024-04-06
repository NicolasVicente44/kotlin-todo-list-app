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
            todoTitle.text = todo.title
            todoDescription.text = todo.description
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

