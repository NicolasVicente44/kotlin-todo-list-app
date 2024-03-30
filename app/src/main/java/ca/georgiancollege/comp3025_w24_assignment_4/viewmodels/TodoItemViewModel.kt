package ca.georgiancollege.comp3025_w24_assignment_4.viewmodels

import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.lifecycle.ViewModel
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import com.google.firebase.firestore.FirebaseFirestore

class TodoItemViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val todoCollection = db.collection("todos")

    fun addTodoItem(todoItem: TodoItem) {
        todoCollection.add(todoItem)
            .addOnSuccessListener { documentReference ->
                // Handle success

                Log.d(TAG, "Todo item added successfully with ID: ${documentReference.id}")

            }
            .addOnFailureListener { exception ->
                // Handle failure


                Log.e(TAG, "Error adding todo item", exception)

            }
    }






    // Implement similar methods for update, delete, and query operations
}
