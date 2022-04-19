package com.example.peliculas.ui.main.view.activities.movie_list

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.peliculas.BuildConfig
import com.example.peliculas.R
import com.example.peliculas.data.api.RetrofitHelper
import com.example.peliculas.data.db.database.DatabaseBuilder
import com.example.peliculas.data.db.database.DatabaseHelperImpl
import com.example.peliculas.data.model.list_movies.response.Movie
import com.example.peliculas.data.model.list_movies.response.MovieResponse
import com.example.peliculas.data.repository.list_movies.MovieRepository
import com.example.peliculas.ui.main.view.activities.movie_list.factory.MovieFactory
import com.example.peliculas.ui.main.view.activities.movie_list.viewmodel.MovieViewModel
import com.example.peliculas.ui.main.view.activities.movie_list.viewmodel.MoviesStatus
import com.example.peliculas.utils.Resource
import com.example.peliculas.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit



class MovieComposeActivity : AppCompatActivity() {
    private lateinit var viewModel: MovieViewModel
    private val retrofit = RetrofitHelper.getRetrofitMovies()
    private val listMovies: MutableList<Movie> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_compose)

        val content = findViewById<ComposeView>(R.id.composeContent)
        setUpViewModel()
        viewModel.getMovies(1)
        viewModel.movies.observe(this) {
            it?.let {

                when (it.status) {

                    Status.SUCCESS -> {

                        it.data?.let { movie ->
                            setData(movie)
                        }
                    }

                }
            }
        }
        content.setContent {
            screenMovieList(viewModel,listMovies)
        }

    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(
            this,
            MovieFactory(
                movieRepository = MovieRepository(retrofit),
                dbHelper = DatabaseHelperImpl(DatabaseBuilder.getInstance(this))
            )
        )[MovieViewModel::class.java]
    }

    private fun setData(movieResponse: MovieResponse) {
        if(!movieResponse.results.isNullOrEmpty()){
            listMovies.addAll(movieResponse.results)
            viewModel.clearAllTables()
            viewModel.insertMovieToDB(listMovies)

        }
    }
    private fun launchDBMovies(){
        runBlocking{
            viewModel.selectMoviesDB()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
public fun screenMovieList(viewModel: MovieViewModel,movies: MutableList<Movie>){
    val status by viewModel.status.observeAsState(MoviesStatus.ERROR)
    val listState = rememberLazyListState()
    var page = 1
    var name by remember {
        mutableStateOf("")
    }
    Column {
        Box(modifier = Modifier
            .border(width = 1.dp, color = Gray, shape = RectangleShape)
            .fillMaxWidth()){
            TextField(
                value = name,
                onValueChange = {name = it},
                placeholder = {Text("Buscar pelicula",
                    color = White)
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = White,
                    cursorColor = White,
                    focusedIndicatorColor = Transparent,

                    ),
                leadingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Botón para buscar",
                            tint = White

                        )
                    }
                },
                singleLine = true,
                maxLines = 1,


                )
        }
       when(status){
           MoviesStatus.DONE -> {
               LazyVerticalGrid(cells = GridCells.Fixed(3),state = listState
                   ){
                       itemsIndexed(movies) { index,movi ->
                         itemListMovi(movi)
                           if(index == movies.size-1){
                               viewModel.getMovies(page++)
                           }
                       }


               }
           }
           MoviesStatus.ERROR ->{}
           MoviesStatus.LOADING -> {}
       }

    }
   
}

@Composable
fun itemListMovi(movi : Movie){
    Card(modifier = Modifier.padding(4.dp)) {
        Column {
            Image(
                painter = rememberImagePainter(
                    data = BuildConfig.url_base_poster + movi.posterPath
                ),
                contentDescription = null,
                modifier = Modifier.height(120.dp),
                contentScale = ContentScale.Crop

                )
            Text(text = movi.title)
            Text(text = movi.releaseDate)



        }

    }

}


