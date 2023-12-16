package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter
import jp.co.cyberagent.android.gpuimage.GPUImage
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivityWatchedListAdapterBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.squareup.picasso.Picasso
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import java.lang.Exception

class WatchedListAdapter(
    private val activity: Activity,
    private var movieList:List<MovieDetails>,
): RecyclerView.Adapter<WatchedListAdapter.WatchedListViewHolder>(){

    class WatchedListViewHolder(
        private val activity: Activity,
        private val binding: ActivityWatchedListAdapterBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(movieDetails: MovieDetails){
            val posterURL = movieDetails.poster
            Picasso.get().load(posterURL).into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        // Apply blur to the bitmap
                        val blurredBitmap = applyBlurAndDarken(bitmap)

                        // Set the blurred bitmap to the ImageView
                        binding.moviePosterWatched.setImageBitmap(blurredBitmap)
                    }
                }
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }
            })
            binding.movieTitleText2.text = movieDetails.name
            binding.yearText2.text = movieDetails.releaseDate
            binding.genreText2.text = movieDetails.genre
            binding.userRatingText.text = movieDetails.userRating
            if(movieDetails.isWatched){
                binding.statusText2.text = "Watched"
            }
            if(movieDetails.isRemoved){
                binding.statusText2.text = "Removed"
            }

            binding.item2.setOnClickListener{
                val intent = Intent(activity, MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",movieDetails.id)
                activity.startActivity(intent)
            }
        }
        private fun applyBlurAndDarken(originalBitmap: Bitmap): Bitmap {
            val gpuImage = GPUImage(activity)
            gpuImage.setImage(originalBitmap)

            // Apply blur
            val blurFilter = GPUImageGaussianBlurFilter(0.8f)
            gpuImage.setFilter(blurFilter)
            val blurredBitmap = gpuImage.bitmapWithFilterApplied

            // Apply darken
            val darkenFilter = GPUImageBrightnessFilter(-0.2f) // Adjust the brightness value as needed
            gpuImage.setImage(blurredBitmap)
            gpuImage.setFilter(darkenFilter)

            return gpuImage.bitmapWithFilterApplied
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchedListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityWatchedListAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return WatchedListViewHolder(activity, binding)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: WatchedListViewHolder, position: Int) {
        holder.bind(movieList[position])
    }
}