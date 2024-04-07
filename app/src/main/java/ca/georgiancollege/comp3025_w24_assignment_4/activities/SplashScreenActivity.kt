package ca.georgiancollege.comp3025_w24_assignment_4.activities

/**
 * SplashScreenActivity file
 * Nicolas Vicente
 * 200539594
 * 2024-04-07
 * assignment 4, the todo list app functionality that uses mvvm and firebase realtime db to allow the user to
 * create, read, update, and delete a todo item use a main activity, details, and create activity along with a splash screen
 *
 * Splash screen clss file to load a splash screen for 2 seconds prior to the main acitivity load
 */




import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.comp3025_w24_assignment_4.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        //android navigation buttons colour setting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            window.navigationBarColor = getColor(R.color.mainBackGround)
        }

        //start main activity after a delay
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}
