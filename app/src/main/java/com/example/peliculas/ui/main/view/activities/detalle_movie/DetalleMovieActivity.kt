package com.example.peliculas.ui.main.view.activities.detalle_movie

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.peliculas.BuildConfig
import com.example.peliculas.R
import com.example.peliculas.application.DroidApp
import com.example.peliculas.data.model.list_movies.response.Movie
import com.example.peliculas.databinding.ActivitySingleMovieBinding
import javax.inject.Inject


class DetalleMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleMovieBinding

    @Inject
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_single_movie)
        (applicationContext as DroidApp).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_single_movie)
        bindUI()

        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun bindUI() {
        binding.movieTitle.text = movie.title
        binding.movieType.text = movie.mediaType
        binding.movieReleaseDate.text = movie.releaseDate
        binding.movieRating.text = "${movie.popularity}%"
        binding.movieLanguage.text = movie.originalLanguage
        binding.movieOverview.text = movie.overview
        val moviePosterURL: String = BuildConfig.url_base_poster + movie.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(binding.ivMoviePoster)
    }


}
