package uk.ac.tees.mad.W9620754

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MainScreen : AppCompatActivity() {

    private lateinit var songInputTextView: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        songInputTextView = findViewById(R.id.songinputTextView)

        // Create an ArrayAdapter to hold the suggestions
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)

        // Set the adapter to the AutoCompleteTextView
        songInputTextView.setAdapter(adapter)

        // Fetch and populate suggestions when the user starts typing
        songInputTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                Toast.makeText(applicationContext,s.toString(),Toast.LENGTH_SHORT).show()
                if(s.toString().length >2) {
                    fetchSuggestions(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun fetchSuggestions(query: String) {
        GlobalScope.launch(Dispatchers.IO) {
            // Make an API request to fetch song suggestions
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("https://genius-song-lyrics1.p.rapidapi.com/search/?q=$query&per_page=10&page=1")
                .get()
                .addHeader("X-RapidAPI-Key", "f6d96b246cmsh9f60d5199ed066dp178283jsn2441bcb10c02")
                .addHeader("X-RapidAPI-Host", "genius-song-lyrics1.p.rapidapi.com")
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody: String = response.body?.string() ?: ""
                    runOnUiThread {
                        populateSuggestions(responseBody)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun populateSuggestions(responseBody: String) {
        try {
            val jsonObject = JSONObject(responseBody)
            val hitsArray = jsonObject.getJSONArray("hits")
            val adapter = songInputTextView!!.adapter as ArrayAdapter<String>
            adapter.clear()
            for (i in 0 until hitsArray.length()) {
                val hit = hitsArray.getJSONObject(i)
                val result = hit.getJSONObject("result")
                val title = result.getString("full_title")
                adapter.add(title)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
