package ca.georgiancollege.comp3025_w24_assignment_3

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TodoItem(
    val title: String = "",
    val description: String = "",
    val dueDate: String = "",
    val pastDue: Boolean = false,
    var status: Boolean = false,
    )
