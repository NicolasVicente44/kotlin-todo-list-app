package ca.georgiancollege.comp3025_w24_assignment_3
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.ToDoItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.view.View


/**
 * adapter class that bridges tthe data and the UI compoenents, including the recycler view/list
 */


class RecyclerViewAdapter(private val todos: List<TodoItem>, private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todos[position]
        holder.binding.apply {

            todoTitle.text = todo.title
            todoDescription.text = truncateDescription(todo.description)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val dueDate = todo.dueDate

            // Check if due date is today
            val today = dueDate.isNotEmpty() && dueDate == currentDate

            // Check if due date is past due
            val pastDue = if (dueDate.isNotEmpty()) {
                sdf.parse(dueDate)?.let { it.before(Date()) } ?: false
            } else {
                false
            }

            // Set text color based on due date
            when {
                today -> todoDueDate.setTextColor(ContextCompat.getColor(root.context, R.color.text_due_today))
                pastDue -> todoDueDate.setTextColor(ContextCompat.getColor(root.context, R.color.text_past_due))
                else -> todoDueDate.setTextColor(ContextCompat.getColor(root.context, R.color.text_normal))
            }

            todoDueDate.text = dueDate
            todoMainStatusSwitch.isChecked = todo.status

            // Check if the click was on the edit image view
            svgImageView.setOnClickListener {
                // Navigate to ToDoItemDetailsActivity and pass todo item details
                val intent = Intent(context, ToDoItemDetailsActivity::class.java)
                intent.putExtra("TODO_TITLE", todo.title)
                intent.putExtra("TODO_DESCRIPTION", todo.description)
                intent.putExtra("TODO_DUE_DATE", todo.dueDate)
                intent.putExtra("TODO_STATUS", todo.status)
                context.startActivity(intent)
            }

            todoMainStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
                todo.status = isChecked
                setOpacityBasedOnSwitchStatus(root, isChecked)
            }
        }
    }

    private fun truncateDescription(description: String): String {
        val maxLength = 25
        return if(description.length > maxLength) {
            "${description.substring(0, maxLength)}..."
        } else {
            description
        }
    }


    private fun setOpacityBasedOnSwitchStatus(view: View, isChecked: Boolean) {
        view.alpha = if (isChecked) 0.5f else 1.0f
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}

