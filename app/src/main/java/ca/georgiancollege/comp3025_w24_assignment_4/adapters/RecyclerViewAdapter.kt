package ca.georgiancollege.comp3025_w24_assignment_4.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.comp3025_w24_assignment_4.R
import ca.georgiancollege.comp3025_w24_assignment_4.activities.ToDoItemDetailsActivity
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ToDoItemBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel

class RecyclerViewAdapter(
    private var dataSet: List<TodoItem>,
    private val context: Context,
    private val todoItemViewModel: TodoItemViewModel // Pass the TodoItemViewModel instance
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val editButton: ImageView = binding.svgImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Inside onBindViewHolder method of RecyclerViewAdapter
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = dataSet[position]
        holder.binding.apply {
            todoTitle.text = todo.title
            todoDescription.text = todo.description
            todoDueDate.text = todo.dueDate

            // Set text color based on status
            val statusColor = if (todo.status) R.color.text_completed else R.color.text_uncompleted
            todoTitle.setTextColor(ContextCompat.getColor(context, statusColor))

            // Set click listener for edit button
            holder.editButton.setOnClickListener {
                // Retrieve the auto-generated ID of the todo item
                val todoId = todo.id ?: ""

                // Call getTodoItemById to fetch the details of the todo item using its ID
                todoItemViewModel.getTodoItemById(todoId,
                    onSuccess = { todoItem ->
                        // Navigate to ToDoItemDetailsActivity and pass todo item details
                        val intent = Intent(context, ToDoItemDetailsActivity::class.java).apply {
                            putExtra("TODO_ID", todoItem?.id) // Pass the document ID
                            putExtra("TODO_TITLE", todoItem?.title)
                            putExtra("TODO_DESCRIPTION", todoItem?.description)
                            putExtra("TODO_DUE_DATE", todoItem?.dueDate)
                            putExtra("TODO_STATUS", todoItem?.status)
                            putExtra("HAS_DUE_DATE", todoItem?.hasDueDate)
                            putExtra("PAST_DUE", todoItem?.pastDue)
                        }
                        context.startActivity(intent)
                    },
                    onFailure = { exception ->
                        // Handle error if unable to fetch todo item details
                        Log.e("RecyclerViewAdapter", "Failed to fetch todo item details: ${exception.message}")
                    }
                )
            }

            // Set click listener for item view
            svgImageView.setOnClickListener {
                // Navigate to ToDoItemDetailsActivity and pass todo item details
                val intent = Intent(context, ToDoItemDetailsActivity::class.java).apply {
                    putExtra("TODO_ID", todo.id) // Pass the document ID
                    putExtra("TODO_TITLE", todo.title)
                    putExtra("TODO_DESCRIPTION", todo.description)
                    putExtra("TODO_DUE_DATE", todo.dueDate)
                    putExtra("TODO_STATUS", todo.status)
                    putExtra("HAS_DUE_DATE", todo.hasDueDate)
                    putExtra("PAST_DUE", todo.pastDue)
                }
                context.startActivity(intent)
            }
        }
        Log.d("TODO", "Todo at position $position: $todo")
    }


    fun updateTodoList(newTodos: List<TodoItem>) {
        dataSet = newTodos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}

