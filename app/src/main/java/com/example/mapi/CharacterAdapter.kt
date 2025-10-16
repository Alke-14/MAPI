package com.example.mapi

import com.example.mapi.model.Character
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class CharacterAdapter(private val characters: MutableList<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageHero: ImageView = itemView.findViewById(R.id.imageCharacter)
        val textHeroName: TextView = itemView.findViewById(R.id.textName)
        val textHeroDescription: TextView = itemView.findViewById(R.id.description)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]

        holder.textHeroName.text = character.name
        holder.textHeroDescription.text = character.description

        val imageUrl = "${character.thumbnail.path}.${character.thumbnail.extension}"
            .replace("http://", "https://")

        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageHero)
    }

    fun addCharacters(newCharacters: List<Character>) {
        val startPosition = characters.size
        characters.addAll(newCharacters)
        notifyItemRangeInserted(startPosition, newCharacters.size)
    }

}