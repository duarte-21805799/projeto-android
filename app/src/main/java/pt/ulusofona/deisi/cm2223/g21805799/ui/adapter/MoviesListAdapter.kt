package pt.ulusofona.deisi.cm2223.g21805799.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2223.g21805799.databinding.ItemMovieBinding
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MovieUI
import java.util.*

class MoviesListAdapter(
    private var itemsBackup: List<MovieUI> = listOf(),
    private var items: List<MovieUI> = listOf(),
    private val onClick: (MovieUI) -> Unit,
) : RecyclerView.Adapter<MoviesListAdapter.MoviesListViewHolder>() {
    private var listFull = mutableListOf<MovieUI>()

    class MoviesListViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesListViewHolder {
        listFull = items as MutableList<MovieUI>
        return MoviesListViewHolder(ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesListViewHolder, position: Int) {
        holder.binding.name.text = items[position].name
        holder.binding.cinema.text = items[position].cinema
        holder.binding.dateItem.text = items[position].datePremiere
        holder.binding.genre.text = items[position].genre
        holder.binding.evaluation.text = items[position].evaluation.toString()
        holder.binding.distance.text = items[position].distanceFromMe.toString()

        holder.itemView.setOnClickListener { onClick(items[position]) }
    }

    override fun getItemCount() = items.size

    fun assignItems(items: List<MovieUI>) {
        this.itemsBackup = items
    }

    fun clearFilters() {
        this.items = itemsBackup
        if (itemsBackup.isNotEmpty()) {
            listFull = itemsBackup as MutableList<MovieUI>
        }
        notifyDataSetChanged()
    }

    fun updateItems(items: List<MovieUI>) {
        this.items = items
        if (items.isNotEmpty()) {
            listFull = items as MutableList<MovieUI>
        }
        notifyDataSetChanged()
    }

    fun getSearchFilter(): Filter {
        return searchFilter
    }

    fun getSortAscendingFilter(): Filter {
        return sortAscendingFilter
    }

    fun getSortDescendingFilter(): Filter {
        return sortDescendingFilter
    }

    fun getFiveHundredMetersFilter(): Filter {
        return fiveHundredMeterFilter
    }

    fun getOneThousandMetersFilter(): Filter {
        return oneThousandMeterFilter
    }

    private val sortAscendingFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MovieUI> = ArrayList()
            filteredList.addAll(listFull)
            filteredList.sortByDescending {
                it.evaluation
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<MovieUI>)
            notifyDataSetChanged()
        }
    }

    private val sortDescendingFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MovieUI> = ArrayList()
            filteredList.addAll(listFull)

            filteredList.sortBy {
                it.evaluation
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<MovieUI>)
            notifyDataSetChanged()
        }
    }

    private val searchFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MovieUI> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(listFull)
            } else {
                val filterPattern =
                    constraint.toString().lowercase().trim()
                for (item in listFull) {
                    if (item.name.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<MovieUI>)
            notifyDataSetChanged()
        }
    }

    private val fiveHundredMeterFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MovieUI> = ArrayList()

            //filteredList.addAll(listFull)
            for (item in listFull) {
                if (item.distanceFromMe <= 500) {
                    filteredList.add(item)
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<MovieUI>)
            notifyDataSetChanged()
        }
    }

    private val oneThousandMeterFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<MovieUI> = ArrayList()

            for (item in listFull) {
                if (item.distanceFromMe <= 1000) {
                    filteredList.add(item)
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            updateItems(results.values as List<MovieUI>)
            notifyDataSetChanged()
        }
    }


}

