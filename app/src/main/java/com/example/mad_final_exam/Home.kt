package com.example.mad_final_exam

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
    }

    //this method show the signup-login activities
    fun OnSignUpPage(view: View)
    {
        val intent: Intent =
            Intent(this, SignUpActivity::class.java);
        startActivity(intent)
    }

    //This method show the My_Location Activity
    fun OnSignIn(view: View)
    {
        val intent: Intent =
            Intent(this, my_location::class.java);
        startActivity(intent)
    }

}