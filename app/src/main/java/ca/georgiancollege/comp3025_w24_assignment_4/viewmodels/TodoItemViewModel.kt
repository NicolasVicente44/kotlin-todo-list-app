package ca.georgiancollege.comp3025_w24_assignment_4.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.georgiancollege.comp3025_w24_assignment_4.data.DataManager
import ca.georgiancollege.comp3025_w24_assignment_4.models.TodoItem
import kotlinx.coroutines.launch

class TodoItemViewModel(private val dataManager: DataManager = DataManager()) : ViewModel() {

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

    fun getAllTodoItems(onSuccess: (List<TodoItem>) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            dataManager.getAllTodoItems(onSuccess, onFailure)

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
}