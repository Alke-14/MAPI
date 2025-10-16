package com.example.mapi

import android.R.attr.description
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mapi.api.MarvelApiService
import com.example.mapi.model.Character
import com.example.mapi.model.MarvelResponse
import com.example.mapi.util.md5
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.mapi.CharacterAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var imageCharacter: ImageView
    private lateinit var textName: TextView
    private lateinit var description: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    private val allCharacters = mutableListOf<Character>()

    private val publicKey = "94a61916b23289f524333a22bfd752e0"
    private val privateKey = "eaee7179d349176214307019a4cc0a42ef93d6e6"
    private var offset = 0
    private var characters = listOf<Character>()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerCharacters)
        adapter = CharacterAdapter(allCharacters)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)

        loadCharacters()
    }

    private fun loadCharacters() {
        val ts = System.currentTimeMillis().toString()
        val hash = md5(ts + privateKey + publicKey)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MarvelApiService::class.java)
        api.getCharacters(ts, publicKey, hash, 20, offset).enqueue(object : Callback<MarvelResponse> {
            override fun onResponse(call: Call<MarvelResponse>, response: Response<MarvelResponse>) {
                if (response.isSuccessful) {
                    val newCharacters = response.body()?.data?.results ?: emptyList()
                    allCharacters.addAll(newCharacters)
                    adapter.addCharacters(newCharacters)
                }
            }

            override fun onFailure(call: Call<MarvelResponse>, t: Throwable) {
                textName.text = "Error: ${t.message}"
            }
        })
    }


}
