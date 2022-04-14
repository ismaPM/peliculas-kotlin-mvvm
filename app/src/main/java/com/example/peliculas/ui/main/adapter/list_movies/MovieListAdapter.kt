package com.example.peliculas.ui.main.adapter.list_movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.peliculas.BuildConfig
import com.example.peliculas.data.model.list_movies.response.Movie
import com.example.peliculas.databinding.MovieListItemBinding
import com.example.peliculas.databinding.NetworkStateItemBinding
import com.example.peliculas.utils.Constants
import com.example.peliculas.utils.Status

class MovieListAdapter(
    private val arrayList: MutableList<Movie> = arrayListOf(),
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var networkState: Status? = null
    private var filterList: MutableList<Movie> = arrayListOf()
    var onItemClick: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            Constants.MOVIE_VIEW_TYPE -> {
                MovieItemViewHolder(MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            Constants.NETWORK_VIEW_TYPE -> {
                NetworkStateItemViewHolder(NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else->{
                NetworkStateItemViewHolder(NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == Constants.MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(filterList[position], context)
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(filterList[position])
            }
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != Status.LOADING
    }

    override fun getItemCount(): Int {
        return filterList.size + if (hasExtraRow()) 1 else 0
    }


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1)
            Constants.NETWORK_VIEW_TYPE
         else
            Constants.MOVIE_VIEW_TYPE
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == oldItem
        }
    }


    class MovieItemViewHolder(private val view: MovieListItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(movie: Movie?, context: Context) {
            view.cvMovieTitle.text = movie?.title
            view.cvMovieReleaseDate.text = movie?.releaseDate
            val moviePosterURL: String = BuildConfig.url_base_poster + movie?.posterPath
            Glide.with(context)
                .load(moviePosterURL)
                .into(view.cvIvMoviePoster)
            /*
            view.cardView.setOnClickListener {
                val intent = Intent(context, DetalleMovieActivity::class.java)
                //intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
             */
        }
    }

    class NetworkStateItemViewHolder(private val view: NetworkStateItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(networkState: Status?) {
            view.progressBarItem.visibility = if (networkState != null && networkState == Status.LOADING)
                View.VISIBLE else View.GONE
            
            if (networkState != null && networkState == Status.ERROR) {
                view.errorMsgItem.visibility = View.VISIBLE
                //view.errorMsgItem.text = networkState.msg
            } else if (networkState != null && networkState == Status.ENDOFLIST) {
                view.errorMsgItem.visibility = View.VISIBLE
                //view.errorMsgItem.text = networkState.msg
            } else {
                view.errorMsgItem.visibility = View.GONE
            }
        }
    }

    fun setNetworkState(networkState: Status) {
        val previousState: Status? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState=networkState
        val hasExtraRow:Boolean=hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(filterList.size)
            }else{
                notifyItemInserted(filterList.size)
            }
        }else if (hasExtraRow && previousState != networkState){
            notifyItemChanged(itemCount-1)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val listAux = mutableListOf<Movie>()
                arrayList.filter {
                    (it.title.lowercase().contains(charSearch) )
                }.forEach {
                    listAux.add(it)
                }
                filterList = listAux
                return FilterResults().apply {
                    values = filterList
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = if (results?.values == null) {
                    arrayListOf()
                } else {
                    results.values as MutableList<Movie>
                }
                notifyDataSetChanged()
            }

        }
    }

    fun addList(list: MutableList<Movie> = arrayListOf()) {
        //Lista Completa
        arrayList.clear()
        arrayList.addAll(list)

        //Lista filtrada
        filterList.clear()
        filterList.addAll(list)

        notifyDataSetChanged()
    }
}