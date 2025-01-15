package ro.pub.cs.systems.eim.testpracticaltest02v1
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var prefixInput: EditText
    private lateinit var resultView: TextView
    private lateinit var fetchButton: Button

    private val autocompleteBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val autocompleteResults = intent?.getStringExtra("autocomplete_results")
            resultView.text = autocompleteResults
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefixInput = findViewById(R.id.prefixInput)
        resultView = findViewById(R.id.resultView)
        fetchButton = findViewById(R.id.fetchButton)

        fetchButton.setOnClickListener {
            val prefix = prefixInput.text.toString()
            if (prefix.isNotEmpty()) {
                thread { fetchAutocompleteResults(prefix) }
            }
        }

        // Register the broadcast receiver
        registerReceiver(autocompleteBroadcastReceiver, IntentFilter("AUTOCOMPLETE_RESULTS"),
            RECEIVER_NOT_EXPORTED
        )
    }

    private fun fetchAutocompleteResults(prefix: String) {
        val urlString = "https://www.google.com/complete/search?client=chrome&q=$prefix"
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val response = connection.inputStream.bufferedReader().readText()
                Log.d("AutocompleteResponse", response)

                // Parse the response
                val jsonArray = JSONArray(response)
                val suggestions = jsonArray.getJSONArray(1)
                val parsedSuggestions = mutableListOf<String>()

                for (i in 0 until suggestions.length()) {
                    parsedSuggestions.add(suggestions.getString(i))
                }

                // Log the third suggestion
                if (parsedSuggestions.size >= 3) {
                    Log.d("ThirdSuggestion", parsedSuggestions[2])
                }

                // Send results via Broadcast
                val intent = Intent("AUTOCOMPLETE_RESULTS")
                intent.putExtra("autocomplete_results", parsedSuggestions.joinToString(",\n"))
                sendBroadcast(intent)
            }
        } catch (e: Exception) {
            Log.e("AutocompleteError", e.message ?: "Unknown error")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(autocompleteBroadcastReceiver)
    }
}