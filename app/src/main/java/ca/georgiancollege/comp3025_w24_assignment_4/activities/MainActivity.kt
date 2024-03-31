package ca.georgiancollege.comp3025_w24_assignment_4.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.comp3025_w24_assignment_4.adapters.RecyclerViewAdapter
import ca.georgiancollege.comp3025_w24_assignment_4.databinding.ActivityMainBinding
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import ca.georgiancollege.comp3025_w24_assignment_4.viewmodels.TodoItemViewModel
import android.util.Log
import android.view.View
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoItemViewModel: TodoItemViewModel
    private lateinit var adapter: RecyclerViewAdapter
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //change the android button ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        // Initialize ViewModel
        todoItemViewModel = ViewModelProvider(this).get(TodoItemViewModel::class.java)

        // Set up RecyclerView
        val recyclerView = binding.FirstRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter(emptyList(), this)
        recyclerView.adapter = adapter

        // Observe LiveData for todo items and ouput them to the recycler veiw
        todoItemViewModel.getAllTodoItems(
            onSuccess = { todoList ->
                adapter.todos = todoList
                adapter.notifyDataSetChanged()
                Log.d("MainActivity", "Todo items retrieved: $todoList")
            },
            onFailure = { exception ->
                Log.e("MainActivity", "Failed to retrieve todo items", exception)
            }
        )
        //get todos and output them to logcat
        db.collection("todos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("todo", "todos: ${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("todo error", "Error getting documents.", exception)
            }








        // Set up FloatingActionButton OnClickListener
        val fab = binding.addMovieFAB
        fab.setOnClickListener {
            // Start the CreateNewTodoActivity
            val intent = Intent(this, CreateNewTodoActivity::class.java)
            startActivity(intent)
        }


    }
}
