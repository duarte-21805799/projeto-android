package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import pt.ulusofona.deisi.cm2223.g21805799.NavigationManager
import pt.ulusofona.deisi.cm2223.g21805799.R
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentMapsBinding
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MovieUI
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MoviesViewModel
import java.util.*
import kotlin.collections.HashMap


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MoviesViewModel
    val movies: ArrayList<MovieUI> = ArrayList()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.getAllMovies { it.map {
            movies.add(it)
        } }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        binding = FragmentMapsBinding.bind(view)

        // Use this to get MovieUI when clicking on a marker so it can retrieve the correct movie to be passed to movieDetailFragment
        val markerMap = HashMap<Marker, MovieUI>()

        // Initialize map fragment
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        // Add markers when Map has finished loading
        supportMapFragment!!.getMapAsync { googleMap ->

            for (movie in movies) {
                Log.i("APP", "Adding marker at lat: ${movie.latitude} lng: ${movie.longitude}")
                // Initialize marker options
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(movie.latitude, movie.longitude))
                markerOptions.title(movie.name)
                markerOptions.snippet(movie.genre)

                // Different type of markers according to movies' evaluations
                when (movie.evaluation) {
                    in 1..2 -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.rate1)) // Muito fraco
                    in 3..4 -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.rate2))
                    in 5..6 -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.rate3))
                    in 7..8 -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.rate4))
                    in 9..10 -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.rate5))
                }
                val marker = googleMap.addMarker(markerOptions)
                if (marker != null)
                    markerMap.put(marker, movie)
            }



            // Zoom in on user's location
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 10f))
                    }
                }

            // Click on Marker opens Movie Detail, just like clicking on a movie on the list
            googleMap.setOnMarkerClickListener { marker ->
                markerMap.get(marker)
                    ?.let { NavigationManager.goToMovieDetail(parentFragmentManager, it) }
                true
            }


        }

        return binding.root
    }



}