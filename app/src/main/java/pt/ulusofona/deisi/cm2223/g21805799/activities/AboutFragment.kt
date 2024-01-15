package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2223.g21805799.R
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Fragment locked in portrait screen orientation
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        binding = FragmentAboutBinding.bind(view)
        return binding.root
    }


}