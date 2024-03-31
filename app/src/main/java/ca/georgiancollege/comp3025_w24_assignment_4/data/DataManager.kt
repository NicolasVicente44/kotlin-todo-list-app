package ca.georgiancollege.comp3025_w24_assignment_4.data

import android.util.Log
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class DataManager {
    private val db = FirebaseFirestore.getInstance()
    private val todoCollection = db.collection("todos")

    fun createTodoItem(todoItem: TodoItem, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        todoCollection.add(todoItem)
            .addOnSuccessListener { documentReference ->
                onSuccess(documentReference.id)
                Log.d("MY LOG", "todos retrieved: ${todoItem}")
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun getAllTodoItems(onSuccess: (List<TodoItem>) -> Unit, onFailure: (Exception) -> Unit) {
        todoCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val todoItems = mutableListOf<TodoItem>()
                for (document in querySnapshot.documents) {
                    val todoItem = document.toObject(TodoItem::class.java)
                    todoItem?.let {
                        todoItems.add(it)
                    }
                }
                onSuccess(todoItems)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun getTodoItemById(documentId: String, onSuccess: (TodoItem?) -> Unit, onFailure: (Exception) -> Unit) {
        todoCollection.document(documentId).get()
            .addOnSuccessListener { documentSnapshot ->
                val todoItem = documentSnapshot.toObject(TodoItem::class.java)
                onSuccess(todoItem)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }


    fun updateTodoItem(todoItem: TodoItem, documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        todoCollection.document(documentId).set(todoItem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteTodoItem(documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        todoCollection.document(documentId).delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}
