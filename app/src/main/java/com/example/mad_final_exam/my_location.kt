package com.example.mad_final_exam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


class my_location : AppCompatActivity(){

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var logi : String
    lateinit var lati: String
    var pic: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_location)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()

         var imgBtn = findViewById<Button>(R.id.takeImage)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        }
        else
            imgBtn.isEnabled = true
        imgBtn.setOnClickListener{
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101)
        {
             pic = data?.getParcelableExtra<Bitmap>("data")
            var imgView  = findViewById<ImageView>(R.id.image)
            imgView.setImageBitmap(pic)


        }
    }

    //This method store the family Members data in firebase database
    fun onSubmit(view: View)
    {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermission()
        //Here store the image and data to firebase database
        //Convert Image to Base64 binary then save it to firebase
        val fName = findViewById<EditText>(R.id.familyMemberName).text.toString()
        val fPassword = findViewById<EditText>(R.id.familyMemberPassowrd).text.toString()
        var imgView  = findViewById<ImageView>(R.id.image)
        val byteArrayOutputStream = ByteArrayOutputStream()
        pic?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imageBytes: ByteArray = byteArrayOutputStream.toByteArray()
        val imageString: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        if (isValidPassword(fPassword)) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Family members")
            val fm = FamilyMembers(fName, fPassword, imageString, logi, lati)
            myRef.child(fName).setValue(fm)
        }else
        {
            Toast.makeText(this,"Password must contain 10 letter with 1 digit and 1 special character ",Toast.LENGTH_SHORT).show()
        }

    }

    //Check location permission plus give the current user location lattitude and longitude
    private fun checkLocationPermission() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        task.addOnSuccessListener {
            if (it !=null)
            {
                Toast.makeText(this, "${it.latitude} ${it.longitude}", Toast.LENGTH_SHORT).show()
                lati = it.latitude.toString()
                logi = it.longitude.toString()
            }
        }
    }
    //this method send the user to main activity
    fun clickCancel(view: View)
    {
        val intent: Intent =
            Intent(this, Home::class.java);
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



