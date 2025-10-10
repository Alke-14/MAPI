package com.example.mapi

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
import com.squareup.picasso.Picasso
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var imageCharacter: ImageView
    private lateinit var textName: TextView
    private lateinit var buttonNext: Button

    private val publicKey = "94a61916b23289f524333a22bfd752e0"
    private val privateKey = "eaee7179d349176214307019a4cc0a42ef93d6e6"
    private var offset = 0
    private var characters = listOf<Character>()
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageCharacter = findViewById(R.id.imageCharacter)
        textName = findViewById(R.id.textName)
        buttonNext = findViewById(R.id.buttonNext)

        loadCharacters()

        buttonNext.setOnClickListener {
            if (characters.isNotEmpty()) {
                index = (index + 1) % characters.size
                displayCharacter(characters[index])
            } else {
                loadCharacters()
            }
        }
    }

    private fun loadCharacters() {
        val ts = System.currentTimeMillis().toString()
        val hash = md5(ts + privateKey + publicKey)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(MarvelApiService::class.java)
        api.getCharacters(ts, publicKey, hash, 10, offset).enqueue(object : Callback<MarvelResponse> {
            override fun onResponse(call: Call<MarvelResponse>, response: Response<MarvelResponse>) {
                if (response.isSuccessful) {
                    characters = response.body()?.data?.results ?: emptyList()
                    if (characters.isNotEmpty()) displayCharacter(characters[0])
                }
            }

            override fun onFailure(call: Call<MarvelResponse>, t: Throwable) {
                textName.text = "Error: ${t.message}"
            }
        })
    }

    private fun displayCharacter(character: Character) {
        textName.text = character.name

        // Construct the image URL correctly
        val imageUrl = "${character.thumbnail.path}.${character.thumbnail.extension}"

        // Marvel sometimes returns http instead of https — fix that
        val secureUrl = imageUrl.replace("http://", "https://")

        // Load it with Picasso
        Picasso.get()
            .load(secureUrl)
            .placeholder(R.drawable.ic_launcher_background) // optional placeholder
            .error(R.drawable.ic_launcher_foreground)       // optional fallback
            .into(imageCharacter)
    }

}
