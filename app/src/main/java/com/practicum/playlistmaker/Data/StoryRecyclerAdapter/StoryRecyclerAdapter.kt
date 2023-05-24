package com.practicum.playlistmaker.Data.StoryRecyclerAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Data.RecyclerSearch.SearchRecyclerAdapter
import com.practicum.playlistmaker.Data.Track
import com.practicum.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class StoryRecyclerAdapter(
    private val storyTrackList: Array<Track>,
    val trackListener: SearchRecyclerAdapter.TrackListener
): RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecyclerAdapter.SearchViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return SearchRecyclerAdapter.SearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return storyTrackList.size
    }

    override fun onBindViewHolder(holder: SearchRecyclerAdapter.SearchViewHolder, position: Int) {
        holder.trackName.text = storyTrackList[position].trackName
        holder.trackArtist.text = storyTrackList[position].artistName

        val actualTime = storyTrackList[position].trackTimeMillis

        if(actualTime != null){
            holder.trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(actualTime.toInt())}

        holder.bindImage(storyTrackList[position])
        holder.itemView.setOnClickListener {
            trackListener.onClick(storyTrackList[position])
        }
    }
}