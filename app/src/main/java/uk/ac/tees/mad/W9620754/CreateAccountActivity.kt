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

class CreateAccountActivity : AppCompatActivity() {

    private var authenticationFirebase: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        var email = findViewById<EditText>(R.id.email_input)
        var pass = findViewById<EditText>(R.id.password_input)

        findViewById<TextView>(R.id.login_acc_text).setOnClickListener{
            startActivity(Intent(this,Login::class.java))
        }

        findViewById<Button>(R.id.sigup_button).setOnClickListener{

            var emaill :String = email.text.toString()
            var password :String = pass.text.toString()

            authenticationFirebase.createUserWithEmailAndPassword(emaill, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Account Created", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,Login::class.java))
                    } else {
                        Toast.makeText(this,"Email Already registered",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}