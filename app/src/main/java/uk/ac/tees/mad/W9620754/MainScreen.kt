package uk.ac.tees.mad.W9620754

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
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
    var songname :String = ""

    private lateinit var songsListView: ListView

    val songsList = ArrayList<SongItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        songsListView = findViewById(R.id.songsListView)


        songInputTextView = findViewById(R.id.songinputTextView)




        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line)


        songInputTextView.setAdapter(adapter)

        findViewById<Button>(R.id.searchbutton).setOnClickListener{
            fetchSuggestions(songname)
        }
        songInputTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as SongItem
            Toast.makeText(this, "ID: ${selectedItem.id}", Toast.LENGTH_SHORT).show()
        }

        songInputTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(s.toString().length >6) {
                   songname = s.toString();
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        songsListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as SongItem
            var ii = Intent(applicationContext,SingelSong::class.java)
            ii.putExtra("SONG_ID",selectedItem.id+"")
            startActivity(ii)
         }

    }

    private fun fetchSuggestions(query: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url("https://genius-song-lyrics1.p.rapidapi.com/search/?q=$query&per_page=10&page=1")
                .get()
                .addHeader("X-RapidAPI-Key", "23b40a786fmshfdcfd145b78b713p1828e2jsnb607347f8623")
                .addHeader("X-RapidAPI-Host", "genius-song-lyrics1.p.rapidapi.com")
                .build()

            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody: String = response.body?.string() ?: ""
                    runOnUiThread {
                        populateSuggestions(responseBody)
                        val adapter = ArrayAdapter<SongItem>(applicationContext, android.R.layout.simple_list_item_1, songsList)
                        songsListView.adapter = adapter
                    }
                }
            } catch (e: IOException) {
                Toast.makeText(applicationContext,e.toString(),Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun populateSuggestions(responseBody: String) {
        try {
            val jsonObject = JSONObject(responseBody)
            val hitsArray = jsonObject.getJSONArray("hits")

            for (i in 0 until hitsArray.length()) {
                val hit = hitsArray.getJSONObject(i)
                val result = hit.getJSONObject("result")
                val title = result.getString("full_title")
                val id = result.getString("id")
                songsList.add(SongItem(title,id))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

data class SongItem(val title: String, val id: String){
    override fun toString(): String {
        return title
    }
}

