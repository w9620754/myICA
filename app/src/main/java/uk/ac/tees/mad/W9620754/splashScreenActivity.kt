package uk.ac.tees.mad.W9620754

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class splashScreenActivity : AppCompatActivity() {

    private var count: Int = 0;
    private var textloder: String ="Loading"
    private lateinit var text:TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        text =  findViewById<TextView>(R.id.loading)
        loader()

    }
    private fun loader()
    {
        Handler().postDelayed({
            if(count<2000) {
                loader()
            }else{
                startActivity(Intent(this,Login::class.java))
            }
            count += 300;
            textloder += "."
            text.text = textloder
        }, 300)
    }
}