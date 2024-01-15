package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21805799.*
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentMoviesListBinding
import pt.ulusofona.deisi.cm2223.g21805799.ui.adapter.MoviesListAdapter
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MovieUI
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MoviesViewModel
import kotlin.math.roundToInt


class MoviesListFragment : Fragment() {
    private lateinit var viewModel: MoviesViewModel
    private var adapter = MoviesListAdapter(onClick = ::onMovieClick)
    private lateinit var binding: FragmentMoviesListBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim)}
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.getAllMovies { updateList(it) }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        binding = FragmentMoviesListBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.rvMoviesList.layoutManager = LinearLayoutManager(context)
        binding.rvMoviesList.adapter = adapter

        binding.searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.getSearchFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getSearchFilter().filter(newText)
                return false
            }
        })

        binding.optionsButton.setOnClickListener {
            onOptionsButtonClicked()
        }

        binding.clearFiltersButton.setOnClickListener {
            adapter.clearFilters()
        }

        binding.ascFilterButton.setOnClickListener {
            adapter.getSortAscendingFilter().filter("")
        }

        binding.descFilterButton.setOnClickListener {
            adapter.getSortDescendingFilter().filter("")
        }

        binding.fiveHundredFilterButton.setOnClickListener {
            adapter.getFiveHundredMetersFilter().filter("")
        }

        binding.oneThousandFilterButton.setOnClickListener {
            adapter.getOneThousandMetersFilter().filter("")
        }

        viewModel.getAllMovies { updateList(it) }

    }



    private fun onOptionsButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.ascFilterButton.visibility = View.VISIBLE
            binding.descFilterButton.visibility = View.VISIBLE
            binding.fiveHundredFilterButton.visibility = View.VISIBLE
            binding.oneThousandFilterButton.visibility = View.VISIBLE
        } else {
            binding.ascFilterButton.visibility = View.INVISIBLE
            binding.ascFilterButton.visibility = View.INVISIBLE
            binding.fiveHundredFilterButton.visibility = View.INVISIBLE
            binding.oneThousandFilterButton.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.optionsButton.startAnimation(rotateOpen)
            binding.ascFilterButton.startAnimation(fromBottom)
            binding.descFilterButton.startAnimation(fromBottom)
            binding.fiveHundredFilterButton.startAnimation(fromBottom)
            binding.oneThousandFilterButton.startAnimation(fromBottom)
        } else {
            binding.optionsButton.startAnimation(rotateClose)
            binding.ascFilterButton.startAnimation(toBottom)
            binding.descFilterButton.startAnimation(toBottom)
            binding.fiveHundredFilterButton.startAnimation(toBottom)
            binding.oneThousandFilterButton.startAnimation(toBottom)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding.ascFilterButton.isClickable = true
            binding.descFilterButton.isClickable = true
            binding.fiveHundredFilterButton.isClickable = true
            binding.oneThousandFilterButton.isClickable = true
        } else {
            binding.ascFilterButton.isClickable = false
            binding.descFilterButton.isClickable = false
            binding.fiveHundredFilterButton.isClickable = false
            binding.oneThousandFilterButton.isClickable = false
        }
    }

    private fun onMovieClick(movie: MovieUI) {
        NavigationManager.goToMovieDetail(parentFragmentManager, movie)
    }



    @SuppressLint("MissingPermission")
    private fun updateList(movies: List<MovieUI>) {
        CoroutineScope(Dispatchers.Main).launch {
            (activity as MainActivity).getLocation()
        }
        var lastLatitude: Double = 0.0
        var lastLongitude: Double = 0.0
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    lastLatitude = location.latitude
                    lastLongitude = location.longitude
                }
                val list = movies.map {
                    val result = FloatArray(1)
                    Location.distanceBetween(it.latitude, it.longitude, lastLatitude, lastLongitude, result)
                    val resultM = result[0].roundToInt()
                    Log.i("APP", "Lat: ${it.latitude} Lng: ${it.longitude} Minha lat: $lastLatitude Minha lng: $lastLongitude Distancia: $resultM")
                    MovieUI(it.id, it.name, it.cinema, it.latitude, it.longitude, it.evaluation, it.dateWatched, it.observations, it.watched, it.genre, it.poster, it.synopsis, it.datePremiere,
                        it.avgEvaluation, it.link, resultM)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    showList(list.isNotEmpty())
                    adapter.assignItems(list)
                    adapter.updateItems(list)
                }
            }

    }

    private fun showList(show: Boolean) {
        if (show) {
            binding.rvMoviesList.visibility = View.VISIBLE
            binding.textNoMoviesAvailable?.visibility = View.GONE
        } else {
            binding.rvMoviesList.visibility = View.GONE
            binding.textNoMoviesAvailable?.visibility = View.VISIBLE
        }
    }

}