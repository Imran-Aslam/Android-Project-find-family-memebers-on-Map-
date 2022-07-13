package com.example.mad_final_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    lateinit var familyName :String
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
    }

    fun onLogin(view: View)
    {
        val email : String = findViewById<EditText>(R.id.email).text.toString()
        if  (email.isNotEmpty()){
            readData(email)
        }else{
            Toast.makeText(this,"PLease enter the Username",Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(userName: String) {

        val e = userName.split("@")
        val pass = findViewById<EditText>(R.id.password).text.toString()
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(e[0]).get().addOnSuccessListener {

            if (it.exists()) {

                if (it.child("password").value.toString() == pass) {


                    familyName = it.child("fMemberName").value.toString()

                    var intent: Intent = Intent(this, Track_Location::class.java);
                    intent.putExtra("name", familyName)
                    startActivity(intent)

                    Toast.makeText(this, "Successfuly Read", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(this, "Passowrod is not correct", Toast.LENGTH_SHORT).show()

                }

            }else {
                Toast.makeText(this, "User Doesn't Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
        }

    }

    fun clickOnCancel(view: View)
    {
        val intent:Intent = Intent(this, Home::class.java)
        startActivity(intent)
    }
}