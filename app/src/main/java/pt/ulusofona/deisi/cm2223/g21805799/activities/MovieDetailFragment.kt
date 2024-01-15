package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ulusofona.deisi.cm2223.g21805799.R
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentMovieDetailBinding
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MovieUI
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat


private const val ARG_MOVIE = "ARG_MOVIE"
class MovieDetailFragment : Fragment() {
    private var movie: MovieUI? = null
    private lateinit var binding: FragmentMovieDetailBinding
    var bmp: Bitmap? = null
    private lateinit var poster : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { movie = it.getParcelable(ARG_MOVIE) }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        val view = inflater.inflate(R.layout.fragment_movie_detail, container, false)
        binding = FragmentMovieDetailBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        movie?.let {
            poster = it.poster
            binding.name.text = it.name
            binding.cinema.text = it.cinema
            binding.evaluation.text = it.evaluation.toString()
            binding.observations.text = it.observations
            binding.genre.text = it.genre
            binding.synopsis.text = it.synopsis
            binding.datePremiere.text = it.datePremiere
            binding.avgEvaluation.text = it.avgEvaluation.toString()
            binding.link.text = it.link
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
            val dateString = simpleDateFormat.format(it.dateWatched)
            binding.date.text = dateString
        }
        CoroutineScope(Dispatchers.IO).launch {
            val `in`: InputStream = URL(poster).openStream()
            bmp = BitmapFactory.decodeStream(`in`)
            withContext (Dispatchers.Main) {
                //update the ImageView
                binding.imageView.setImageBitmap(bmp)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(movie: MovieUI) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MOVIE, movie)
                }
            }
    }
}