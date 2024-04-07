/**
 * MainActivity file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * this file controls the main activity and its interaction with the adapter that loads the todos into the recycler view
 */




package ca.georgiancollege.comp3025_w24_assignment_4.activities
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.comp3025_w24_assignment_4.adapters.RecyclerViewAdapter
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ActivityMainBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    //instance varibles for the adapter, recycler view, and viewmodel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var todoItemViewModel: TodoItemViewModel

    companion object {
        const val CREATE_TODO_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //viewe bidning init
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the navigation bar icons color to gray
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        // Initialize ViewModel
        todoItemViewModel = ViewModelProvider(this).get(TodoItemViewModel::class.java)

        // Set up RecyclerView
        binding.FirstRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(emptyList(), this, todoItemViewModel)
        binding.FirstRecyclerView.adapter = adapter

        // Call getAllTodoItems() to fetch todo items
        todoItemViewModel.getAllTodoItems()

        // Observe the todo list from the ViewModel
        todoItemViewModel.allTodoItems.observe(this, Observer { todos ->
            todos?.let {
                adapter.updateTodoList(it)
                Log.d("TODO", "Total Todos: ${todos.size}")
                for ((index, todo) in todos.withIndex()) {
                    Log.d("TODO", "Todo $index: $todo")
                }
            }
        })

        // Set up FloatingActionButton OnClickListener for the create page to create a new todo
        binding.addMovieFAB.setOnClickListener {
            // Start the CreateNewTodoActivity
            val intent = Intent(this, CreateNewTodoActivity::class.java)
            startActivityForResult(intent, CREATE_TODO_REQUEST_CODE)
        }
    }

    //refreshing the list
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_TODO_REQUEST_CODE && resultCode == RESULT_OK) {
            // Refresh data after creating new todo item
            todoItemViewModel.getAllTodoItems()
        }
    }
}

