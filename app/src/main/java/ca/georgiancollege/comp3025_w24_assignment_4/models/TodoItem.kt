package ca.georgiancollege.comp3025_w24_assignment_4.models

/**
 * TodoItem file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * this file is the model class for the todo item, it sets default values and defines the data structure that the database will follow
 */

import java.text.SimpleDateFormat
import java.util.*

data class TodoItem(
    var id: String? = null,
    var title: String = "",
    var description: String = "",
    var hasDueDate: Boolean = false,
    var dueDate: String = getCurrentDate(),
    val pastDue: Boolean = dueDate < getCurrentDate(),
    var status: Boolean = false,
) {


    companion object {

        //function to get current date and set the default due date equal to the current date
        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(Date())
        }
    }
}
