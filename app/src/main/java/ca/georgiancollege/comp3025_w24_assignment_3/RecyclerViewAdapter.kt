package ca.georgiancollege.comp3025_w24_assignment_3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.comp3025_w24_assignment_3.databinding.ToDoItemBinding

class RecyclerViewAdapter(private val todos: List<TodoItem>) :
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
            todoDescription.text = todo.description
            todoDueDate.text = todo.dueDate
            todoMainStatusSwitch.isChecked = false


            root.setOnClickListener {


            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}
