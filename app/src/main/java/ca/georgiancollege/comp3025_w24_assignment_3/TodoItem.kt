package ca.georgiancollege.comp3025_w24_assignment_3

data class TodoItem(
val title: String = "",
val description: String = "",
val dueDate: String = "",
val pastDue: Boolean = false,
val status: Boolean = false,


)
