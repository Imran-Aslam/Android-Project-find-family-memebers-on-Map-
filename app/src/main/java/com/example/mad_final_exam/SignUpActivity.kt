package com.example.mad_final_exam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

    }

    //Get All the data from text boxes and Store in the firebase database
    //Also Perform Validation
    fun onRegister( view: View)
    {

        val email = findViewById<EditText>(R.id.registerEmail).text.toString()
        val password = findViewById<EditText>(R.id.registerPassword).text.toString()
        val guardianName = findViewById<EditText>(R.id.guardianName).text.toString()
        val guardianAge = findViewById<EditText>(R.id.guardianAge).text.toString()
        val fMemberName = findViewById<EditText>(R.id.fMemberName).text.toString()
        val fMemberPassword = findViewById<EditText>(R.id.fMemberPassword).text.toString()
        // Write a message to the database

        if(isValidPassword(password)) {
            if (guardianAge.toInt() > 35) {
                if (!(email.isEmpty() || password.isEmpty() ||
                            guardianAge.isEmpty() || guardianName.isEmpty() ||
                            fMemberName.isEmpty() || fMemberPassword.isEmpty())
                ) {
//        // Write a message to the database
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("users")
                    val e = email.split("@")
                    val user =
                        User(
                            email,
                            password,
                            guardianName,
                            guardianAge,
                            fMemberName,
                            fMemberPassword
                        )
                    myRef.child(e[0]).setValue(user)
                    val intent: Intent =
                        Intent(this, Track_Location::class.java);
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpActivity, "Text box is empty", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(this,"Age must be greater than 35",Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this,"Password must contain 10 letter with 1 digit and 1 special character ",Toast.LENGTH_SHORT).show()
        }
    }

    //Cancel is a function which returns to main activity
    fun cancel(view: View)
    {
        val intent: Intent =
            Intent(this, Home::class.java);
        startActivity(intent)
    }

    //If User click on login button then this method move the activity
    fun On_login(view: View)
    {
        val intent: Intent =
            Intent(this, Login::class.java);
        startActivity(intent)
    }
    //Validate the password
    fun isValidPassword(password: String): Boolean {
        val passwordREGEX = Pattern.compile("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{10,}" +               //at least 10 characters
                "$");
        return passwordREGEX.matcher(password).matches()
    }

}