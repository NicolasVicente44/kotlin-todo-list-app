package ca.georgiancollege.comp3025_w24_assignment_4.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.georgiancollege.comp3025_w24_assignment_4.data.DataManager
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import kotlinx.coroutines.launch
/**
 * TodoViewModel file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * This file is the view model for the todo item and handles the datamanager methods
 */

class TodoItemViewModel(private val dataManager: DataManager = DataManager()) : ViewModel() {

    private val _allTodoItems = MutableLiveData<List<TodoItem>>()
    val allTodoItems: LiveData<List<TodoItem>>
        get() = _allTodoItems

    fun addTodoItem(todoItem: TodoItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            dataManager.createTodoItem(todoItem,
                {
                    onSuccess()
                },
                onFailure
            )
        }
    }

    fun getAllTodoItems() {
        dataManager.getAllTodoItems { todos ->
            _allTodoItems.postValue(todos)
        }
    }

    fun getTodoItemById(documentId: String, onSuccess: (TodoItem?) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            dataManager.getTodoItemById(documentId, onSuccess, onFailure)
        }
    }

    fun updateTodoItem(todoItem: TodoItem, documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            dataManager.updateTodoItem(todoItem, documentId, onSuccess, onFailure)
        }
    }

    fun deleteTodoItem(documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            dataManager.deleteTodoItem(documentId, onSuccess, onFailure)
        }
    }

    // Function to update the status of a todo item from completed to incomplete
    fun updateTodoStatus(todoId: String, newStatus: Boolean, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        dataManager.updateTodoStatus(todoId, newStatus, onSuccess, onFailure)
    }
}
