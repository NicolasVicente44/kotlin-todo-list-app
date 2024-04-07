package ca.georgiancollege.comp3025_w24_assignment_4.data

import android.util.Log
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import com.google.firebase.database.*
/**
 * DataManager file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * This file handles the database interaction for the crud methods
 */

class DataManager {
    private val database = FirebaseDatabase.getInstance()
    private val todoReference = database.getReference("todos")
    fun createTodoItem(
        todoItem: TodoItem,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val key = todoReference.push().key
        key?.let { todoKey ->
            // Set the ID of the todoItem to the auto-generated key
            todoItem.id = todoKey
            todoReference.child(todoKey).setValue(todoItem)
                .addOnSuccessListener {
                    onSuccess(todoKey)
                    Log.d("MY LOG", "Todo item created: $todoItem")
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }
    }


    fun getAllTodoItems(onComplete: (List<TodoItem>) -> Unit) {
        todoReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val todos = mutableListOf<TodoItem>()
                for (snapshot in dataSnapshot.children) {
                    val todoItem = snapshot.getValue(TodoItem::class.java)
                    todoItem?.let {
                        todos.add(it)
                    }
                }
                onComplete(todos)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DataManager", "Error fetching todos: ${databaseError.message}")
                onComplete(emptyList())
            }
        })
    }

    fun getTodoItemById(documentId: String, onSuccess: (TodoItem?) -> Unit, onFailure: (Exception) -> Unit) {
        todoReference.child(documentId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val todoItem = dataSnapshot.getValue(TodoItem::class.java)
                onSuccess(todoItem)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                onFailure(databaseError.toException())
            }
        })
    }


    fun updateTodoItem(todoItem: TodoItem, documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        todoReference.child(documentId).setValue(todoItem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun deleteTodoItem(documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        todoReference.child(documentId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateTodoStatus(todoId: String, newStatus: Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val updatedData = hashMapOf(
            "status" to newStatus
        )

        todoReference.child(todoId).updateChildren(updatedData as Map<String, Any>)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
