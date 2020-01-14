package com.anthony.foodmap.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthony.foodmap.data.Venue
import com.anthony.foodmap.databinding.VenueItemBinding
import com.anthony.foodmap.ui.VenuesAdapter.ViewHolder

/**
 * Adapter for the venue list. Has a reference to the [VenuesViewModel] to send actions back to it.
 */
class VenuesAdapter(private val viewModel: VenuesViewModel, private val mapViewModel: MapViewModel) :
    ListAdapter<Venue, ViewHolder>(TaskDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, mapViewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: VenueItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: VenuesViewModel, mapViewModel: MapViewModel, item: Venue) {

            binding.viewmodel = viewModel
            binding.mapviewmodel = mapViewModel
            binding.venue = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VenueItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<Venue>() {
    override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
        return oldItem == newItem
    }
}
