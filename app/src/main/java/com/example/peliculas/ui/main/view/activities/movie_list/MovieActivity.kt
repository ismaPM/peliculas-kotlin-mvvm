package com.example.peliculas.ui.main.view.activities.movie_list

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.peliculas.R
import com.example.peliculas.application.DroidApp
import com.example.peliculas.data.api.RetrofitHelper
import com.example.peliculas.data.db.database.DatabaseBuilder
import com.example.peliculas.data.db.database.DatabaseHelperImpl
import com.example.peliculas.data.model.list_movies.response.Movie
import com.example.peliculas.data.model.list_movies.response.MovieResponse
import com.example.peliculas.data.repository.list_movies.MovieRepository
import com.example.peliculas.databinding.ActivityMovieBinding
import com.example.peliculas.ui.main.adapter.list_movies.MovieListAdapter
import com.example.peliculas.ui.main.view.activities.detalle_movie.DetalleMovieActivity
import com.example.peliculas.ui.main.view.activities.movie_list.factory.MovieFactory
import com.example.peliculas.ui.main.view.activities.movie_list.viewmodel.MovieViewModel
import com.example.peliculas.utils.Constants
import com.example.peliculas.utils.Status
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieBinding
    private val retrofit = RetrofitHelper.getRetrofitMovies()
    private lateinit var viewModel: MovieViewModel
    private lateinit var adapter: MovieListAdapter
    private val listMovies: MutableList<Movie> = arrayListOf()
    private var movieResponse: MovieResponse? = null
    private var page = 1
    private var isSearch = false

    @Inject
    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_movie)
        (applicationContext as DroidApp).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_movie)
        setUpViewModel()
        setRecycler()
        observers()
        launchMovies()
        super.onCreate(savedInstanceState)
    }


    /**
     * Inicializa el ViewModel
     */
    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            MovieFactory(
                movieRepository = MovieRepository(retrofit),
                dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(this))
            )
        )[MovieViewModel::class.java]
    }

    /**
     * @param page: es la paginación de carga
     * Consume el servicio de obtener lista de peliculas
     */
    private fun launchMovies(){
        viewModel.getMovies(page)
    }

    /**
     * @param page: es la paginación de carga
     * Consulta la BD para obtener lista de peliculas
     */
    private fun launchDBMovies(){
        runBlocking{
            viewModel.selectMoviesDB()
        }
    }

    /**
     * Observers movies
     */
    private fun observers() {
        viewModel.moviesDB.observe(this) {
            it?.let {
                binding.resource = it
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBarPopular.visibility = VISIBLE
                        binding.lnMovies.visibility = GONE
                        binding.txtErrorPopular.visibility = GONE
                    }
                    Status.SUCCESS -> {
                        binding.lnMovies.visibility = VISIBLE
                        binding.progressBarPopular.visibility = GONE
                        binding.txtErrorPopular.visibility = GONE
                        it.data?.let { movies ->
                            listMovies.clear()
                            listMovies.addAll(movies)
                            adapter.addList(listMovies.toMutableList())
                        }

                    }
                    else -> {
                        binding.txtErrorPopular.visibility = VISIBLE
                        binding.progressBarPopular.visibility = GONE
                        binding.lnMovies.visibility = GONE
                    }
                }
            }
        }

        viewModel.movies.observe(this) {
            it?.let {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.resource = it
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBarPopular.visibility = VISIBLE
                        binding.lnMovies.visibility = GONE
                        binding.txtErrorPopular.visibility = GONE
                    }
                    Status.SUCCESS -> {
                        binding.lnMovies.visibility = VISIBLE
                        binding.progressBarPopular.visibility = GONE
                        binding.txtErrorPopular.visibility = GONE
                        it.data?.let { movie ->
                            movieResponse = movie
                            setData(movie)
                        }
                    }
                    Status.ERROR -> {
                        movieResponse = null
                        binding.txtErrorPopular.visibility = VISIBLE
                        binding.progressBarPopular.visibility = GONE
                        binding.lnMovies.visibility = GONE
                    }
                    Status.NOINTERNET,Status.TIMEOUT ->{
                        movieResponse = null
                        launchDBMovies()
                    }
                    Status.THROW ->{
                        movieResponse = null
                        binding.txtErrorPopular.visibility = VISIBLE
                        binding.txtErrorPopular.text = it.message
                        binding.progressBarPopular.visibility = GONE
                        binding.lnMovies.visibility = GONE
                    }
                    else -> {
                        movieResponse = null
                        binding.txtErrorPopular.visibility = VISIBLE
                        binding.progressBarPopular.visibility = GONE
                        binding.lnMovies.visibility = GONE
                    }
                }
            }
        }
    }


    /**
     * Inicializa el recycler
     */
    private fun setRecycler() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = adapter.getItemViewType(position)
                return if (viewType == Constants.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }
        adapter = MovieListAdapter(listMovies,this)
        binding.rvMovieList.layoutManager = gridLayoutManager
        binding.rvMovieList.setHasFixedSize(true)
        binding.rvMovieList.adapter = adapter
        //REFRESH LAYOUT
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMovies(page)
        }
        //LISTENER SEARCH
        binding.edSearchFilter.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        //LISTENER SEARCH ENABLE
        binding.edSearchFilter.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            isSearch = hasFocus
        }
        //LISTENER ITEM CLICK
        adapter.onItemClick = { item ->
            movie.posterPath = item.posterPath
            movie.adult = item.adult
            movie.backdropPath = item.backdropPath
            movie.id = item.id
            movie.mediaType = item.mediaType
            movie.originalLanguage = item.originalLanguage
            movie.originalTitle = item.originalTitle
            movie.overview = item.overview
            movie.popularity = item.popularity
            movie.releaseDate = item.releaseDate
            movie.title = item.title
            movie.video = item.video
            movie.voteAverage = item.voteAverage
            movie.voteCount = item.voteCount
            val intent = Intent(this, DetalleMovieActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Muestra items
     */
    private fun setData(movieResponse: MovieResponse) {
        if(!movieResponse.results.isNullOrEmpty()){
            listMovies.addAll(movieResponse.results)
            viewModel.clearAllTables()
            viewModel.insertMovieToDB(listMovies)
            adapter.addList(listMovies.toMutableList())
            //SCROLL MORE PAGE
            binding.rvMovieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        binding.edSearchFilter.clearFocus()
                        movieResponse?.let {
                            if(page <= it.totalPages && !isSearch){
                                page++
                                viewModel.getMovies(page)
                            }
                        }

                    }
                }
            })
        }
    }

}