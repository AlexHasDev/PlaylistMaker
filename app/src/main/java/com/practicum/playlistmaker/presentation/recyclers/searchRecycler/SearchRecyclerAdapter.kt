package com.practicum.playlistmaker.presentation.recyclers.searchRecycler

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.utils.DateUtils

class SearchRecyclerAdapter(
    private val adapterTrackList: ArrayList<Track>,
    private val trackListener: TrackListener
) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {
    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var trackName: TextView = itemView.findViewById(R.id.track_name)
        var trackArtist: TextView = itemView.findViewById(R.id.track_artist)
        var trackTime: TextView = itemView.findViewById(R.id.track_time)
        var trackImage: ImageView = itemView.findViewById(R.id.song_image)

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.trackName.text = adapterTrackList[position].trackName
        holder.trackArtist.text = adapterTrackList[position].artistName

        val actualTime = adapterTrackList[position].trackTimeMillis
        if (actualTime != null) {
            holder.trackTime.text = DateUtils.changeDateFormat(actualTime)

        }

        holder.bindImage(adapterTrackList[position])
        holder.itemView.setOnClickListener {
            trackListener.onClick(adapterTrackList[position])
        }
    }

    interface TrackListener {
        fun onClick(track: Track)
    }
}