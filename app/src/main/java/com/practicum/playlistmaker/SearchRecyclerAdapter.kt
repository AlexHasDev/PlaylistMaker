package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SearchRecyclerAdapter(
    private val trackList: ArrayList<Track>
) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trackName = itemView.findViewById<TextView>(R.id.track_name)
        var trackArtist = itemView.findViewById<TextView>(R.id.track_artist)
        var trackTime = itemView.findViewById<TextView>(R.id.track_time)
        var trackImage: ImageView = itemView.findViewById(R.id.song_image)

        fun bind(track: Track) {
            Glide.with(itemView).load(track.artworkUrl100).transform(RoundedCorners(7))
                .into(trackImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.trackName.text = trackList[position].trackName
        holder.trackArtist.text = trackList[position].artistName
        holder.trackTime.text = trackList[position].trackTime
        holder.bind(trackList[position])
    }

}