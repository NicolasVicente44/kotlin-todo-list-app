package ca.georgiancollege.comp3025_w24_assignment_4.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.georgiancollege.comp3025_w24_assignment_4.data.DataManager
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        viewModelScope.launch(Dispatchers.Main) {
            dataManager.getAllTodoItems { todos ->
                _allTodoItems.postValue(todos)
            }
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
