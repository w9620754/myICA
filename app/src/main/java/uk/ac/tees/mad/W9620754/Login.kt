package uk.ac.tees.mad.W9620754

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Login : AppCompatActivity() {

    private var authenticationFirebase: FirebaseAuth =FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var email = findViewById<EditText>(R.id.email_input)
        var pass = findViewById<EditText>(R.id.password_input)

        if(authenticationFirebase.currentUser != null)
        {
            startActivity(Intent(this,AllFeatures::class.java))
            finish()
        }

        findViewById<TextView>(R.id.create_acc_text).setOnClickListener{
            startActivity(Intent(this,CreateAccountActivity::class.java))
        }

        findViewById<Button>(R.id.sigin_button).setOnClickListener{
            var emaill :String = email.text.toString()
            var password :String = pass.text.toString()
            authenticationFirebase.signInWithEmailAndPassword(emaill,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                      startActivity(Intent(this,AllFeatures::class.java))
                        Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this,"Unable to Login",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}