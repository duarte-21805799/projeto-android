package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21805799.R
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MoviesViewModel


class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: MoviesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        view.setBackgroundColor(Color.GRAY)
        binding = FragmentDashboardBinding.bind(view)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getDashboardInfo {
            CoroutineScope(Dispatchers.Main).launch {
                binding.moviesToday.text = it[0]
                binding.moviesThisWeek.text = it[1]
                binding.moviesThisMonth.text = it[2]
                binding.moviesThisYear.text = it[3]
            }
        }
    }

}