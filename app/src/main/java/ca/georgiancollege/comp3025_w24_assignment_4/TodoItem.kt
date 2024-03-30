package ca.georgiancollege.comp3025_w24_assignment_4

/**
 * todo itemm data class that has instance varibales pertaining to the todos and will connect with the firestore database in assignment 4
 */

data class TodoItem(
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val pastDue: Boolean = false,
    var status: Boolean = false,
    )
