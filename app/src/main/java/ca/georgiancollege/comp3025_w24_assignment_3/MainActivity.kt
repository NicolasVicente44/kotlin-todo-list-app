package ca.georgiancollege.comp3025_w24_assignment_3

/**
 * Nicolas Vicente
 * 200539594
 * March 3rd, 2023
 * Main Activity file
 *To do list UI app. This is part one of the todo list application that uses firebase and and authentication.
 * this part is only the UI for the application with limited functionality, of which will be built upon in assignment 4
 */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.to_do_item_details)



    }
}