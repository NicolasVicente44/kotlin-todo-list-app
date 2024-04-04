package ca.georgiancollege.comp3025_w24_assignment_4.models

/**
 * todo itemm data class that has instance varibales pertaining to the todos and will connect with the firestore database in assignment 4
 */
import java.text.SimpleDateFormat
import java.util.*

data class TodoItem(
    var id: String? = null,
    val title: String = "",
    val description: String = "",
    val hasDueDate: Boolean = false,
    val dueDate: String = getCurrentDate(),
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
