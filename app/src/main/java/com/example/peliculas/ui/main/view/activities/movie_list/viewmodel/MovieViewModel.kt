package com.example.peliculas.ui.main.view.activities.movie_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.peliculas.data.api.NetworkException
import com.example.peliculas.data.api.TimeOutException
import com.example.peliculas.data.db.database.DatabaseHelper
import com.example.peliculas.data.model.list_movies.response.Movie
import com.example.peliculas.data.model.list_movies.response.MovieResponse
import com.example.peliculas.data.repository.list_movies.MovieRepository
import com.example.peliculas.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch


enum class MoviesStatus { LOADING, ERROR, DONE }

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val dbHelper: DatabaseHelper
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val _movies = MutableLiveData<Resource<MovieResponse>>()
    val movies: LiveData<Resource<MovieResponse>> get() = _movies
    private val _moviesDB = MutableLiveData<Resource<List<Movie>>>()
    val moviesDB: LiveData<Resource<List<Movie>>> get() = _moviesDB
    private val _status = MutableLiveData<MoviesStatus>()
    val status: LiveData<MoviesStatus>
        get() = _status

    /**
     * Método para obtener peliculas
     */
    fun getMovies(page: Int) {
        _movies.postValue(Resource.loading(null))
        compositeDisposable.add(
            movieRepository.getMovies(page = page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    //ERROR EN SERVICIO
                    response?.success?.let {
                        _movies.postValue(Resource.error("Error al obtener movies", null))
                        _status.postValue(MoviesStatus.ERROR)
                    }
                    //NO SE ENCUENTRAN IDS
                    response?.errors?.let {
                        _movies.postValue(Resource.error("Error al obtener movies", null))
                        _status.postValue(MoviesStatus.ERROR)
                    }
                    //RESPUESTA CORRECTA

                    _movies.postValue(Resource.success(response))
                    _status.postValue(MoviesStatus.DONE)

                }, { throwable ->
                    when (throwable) {
                        is NetworkException -> {
                            // TODO handle 'no network'
                            _movies.postValue(Resource.noInternet(throwable.message, null))
                            _status.postValue(MoviesStatus.ERROR)
                        }
                        is TimeOutException -> {
                            // TODO handle 'time out'
                            _movies.postValue(Resource.timeOut(throwable.message, null))
                            _status.postValue(MoviesStatus.ERROR)
                        }
                        else -> {
                            // TODO handle some other error
                            _movies.postValue(Resource.throwError(throwable.message, null))
                            _status.postValue(MoviesStatus.ERROR)
                        }
                    }
                })
        )
    }

    /**
     * @param movie: Objeto que guarda los datos de la pelicula
     * Guarda la pelicula en la base de datos
     */
    fun insertMovieToDB(movies: List<Movie>) {
        viewModelScope.launch {
            dbHelper.insertarMovies(movies)
        }
    }

    /**
     * @param page: paginación de la consulta
     * Obtiene las peliculas en la base de datos
     */
    suspend fun selectMoviesDB(){
        dbHelper.selectMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ response ->
                _moviesDB.postValue(Resource.success(response))
            },{ throwable ->
                _moviesDB.postValue(Resource.throwError(throwable.message, null))
            })?.let {
                compositeDisposable.add(it)
            }
        /*
        viewModelScope.launch {
            _moviesDB.value = dbHelper.selectMovies()
        }
         */
    }

    /**
     * Elimina todas las tablas
     */
    fun clearAllTables() {
        viewModelScope.launch {
            dbHelper.deleteMovies()
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.dispose()
        super.onCleared()
    }

}