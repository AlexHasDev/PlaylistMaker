package com.practicum.playlistmaker.RecyclerSearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.retrofitSearch.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchRecyclerAdapter(
    private val adapterTrackList: ArrayList<Track>
) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var trackName: TextView = itemView.findViewById(R.id.track_name)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        var trackTime: TextView = itemView.findViewById(R.id.track_time)
        private var trackImage: ImageView = itemView.findViewById(R.id.song_image)

        fun bindImage(track: Track) {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.snake)
                .transform(RoundedCorners(2))
                .into(trackImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return adapterTrackList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

            holder.trackName.text = adapterTrackList[position].trackName
            holder.trackArtist.text = adapterTrackList[position].artistName

            val actualTime = adapterTrackList[position].trackTimeMillis

            holder.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(actualTime.toInt())

            holder.bindImage(adapterTrackList[position])
    }

}