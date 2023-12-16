package com.busal.finals.moviewatchlist.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.busal.finals.moviewatchlist.MovieDetailsViewActivity
import com.busal.finals.moviewatchlist.databinding.ActivityHomeListAdapterBinding
import com.busal.finals.moviewatchlist.databinding.RatingDialogLayoutBinding
import com.busal.finals.moviewatchlist.models.MovieDetails
import com.busal.finals.moviewatchlist.storage.LocalStorage
import com.squareup.picasso.Picasso
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter
import java.lang.Exception


class HomeListAdapter(
    private val activity:Activity,
    var movieLists:List<MovieDetails>,
    private val listReloadListener: ListReloadListener
): RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>(){
    interface ListReloadListener {
        fun onListReloaded(source: String)
    }
    class HomeListViewHolder(
        private val activity: Activity,
        private val binding: ActivityHomeListAdapterBinding,
        private val adapter: HomeListAdapter,
    ): RecyclerView.ViewHolder(binding.root){
        private lateinit var updateMovieList: MutableList<MovieDetails>
        fun bind(position: Int){
            val selectedMovie = adapter.movieLists[position]
            updateMovieList = LocalStorage(activity).movieList.toMutableList()
            val posterURL = selectedMovie.poster
            val index = updateMovieList.indexOfFirst { it.id == selectedMovie.id }
            if(index != -1){
                updateMovieList[index]=selectedMovie
            }
            Picasso.get().load(posterURL).into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        // Apply blur to the bitmap
                        val blurredBitmap = applyBlurAndDarken(bitmap)

                        // Set the blurred bitmap to the ImageView
                        binding.moviePoster.setImageBitmap(blurredBitmap)
                    }
                }
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }
            })
            binding.movieTitleText.text = selectedMovie.name
            binding.yearText.text = selectedMovie.releaseDate
            binding.genreText.text = selectedMovie.genre
            binding.watchedButton.setOnClickListener {
                showRatingDialog(selectedMovie)
            }
            selectedMovie.userRating
            binding.removeButton.setOnClickListener {
                showRemoveConfirmationDialog(selectedMovie)
            }
            binding.item.setOnClickListener{
                val intent = Intent(activity,MovieDetailsViewActivity::class.java)
                intent.putExtra("PARAM_ID",selectedMovie.id)
                activity.startActivity(intent)
            }
        }
        private fun showRatingDialog(selectedMovie: MovieDetails) {
            val builder = AlertDialog.Builder(activity)
            val dialogBinding = RatingDialogLayoutBinding.inflate(activity.layoutInflater)
            builder.setView(dialogBinding.root)

            val dialog = builder.create()

            // Set click listeners for rating buttons using the binding
            val buttons = listOf(
                dialogBinding.ratingButton1,
                dialogBinding.ratingButton2,
                dialogBinding.ratingButton3,
                dialogBinding.ratingButton4,
                dialogBinding.ratingButton5
            )
            dialogBinding.rateMovieTitleText.text="Rate: ${selectedMovie.name}"

            // Set a click listener for each rating button
            for (i in 1..5) {
                buttons[i - 1].setOnClickListener {
                    // Set the user rating for the selected movie
                    selectedMovie.userRating = "$i/5"
                    selectedMovie.isWatched = true

                    // Save the updated movie list to local storage
                    LocalStorage(activity).movieList = updateMovieList

                    // Reload the list using the adapter
                    adapter.reloadList("HomeListAdapter")

                    // Dismiss the dialog
                    dialog.dismiss()
                }
            }

            // Set a click listener for the Cancel button
            dialogBinding.cancelRateButton.setOnClickListener {
                // Dismiss the dialog
                dialog.dismiss()
            }

            // Show the dialog
            dialog.show()
        }

        private fun showRemoveConfirmationDialog(selectedMovie: MovieDetails) {
            val builder = AlertDialog.Builder(activity)

            builder.setTitle("${selectedMovie.name}")
            builder.setMessage("Are you sure you want to remove this on the list?")

            builder.setPositiveButton("Yes") { dialog, which ->
                // User clicked Yes, proceed with removal
                selectedMovie.isRemoved = true
                LocalStorage(activity).movieList = updateMovieList
                adapter.reloadList("HomeListAdapter")
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, which ->
                // User clicked No, close the dialog
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
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

    private fun reloadList(source:String){
        listReloadListener.onListReloaded(source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ActivityHomeListAdapterBinding.inflate(
            inflater,
            parent,
            false,
        )
        return HomeListViewHolder(activity, binding,this)
    }

    override fun getItemCount() = movieLists.size

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bind(position)
    }
}