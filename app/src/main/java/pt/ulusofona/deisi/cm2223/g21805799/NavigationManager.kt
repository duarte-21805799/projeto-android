package pt.ulusofona.deisi.cm2223.g21805799

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import pt.ulusofona.deisi.cm2223.g21805799.activities.*
import pt.ulusofona.deisi.cm2223.g21805799.ui.viewModels.MovieUI

object NavigationManager {

    private fun placeFragment(fm: FragmentManager, fragment: Fragment) {
        val transition = fm.beginTransaction()
        transition.replace(R.id.frame, fragment)
        transition.addToBackStack(null)
        transition.commit()
    }

    fun goToAboutFragment(fm: FragmentManager) {
        placeFragment(fm, AboutFragment())
    }

    fun goToMovieFormFragment(fm: FragmentManager) {
        placeFragment(fm, MovieFormFragment())
    }

    fun goToMovieDetail(fm: FragmentManager, movie: MovieUI) {
        placeFragment(fm, MovieDetailFragment.newInstance(movie))
    }

    fun goToMoviesListFragment(fm: FragmentManager) {
        placeFragment(fm, MoviesListFragment())
    }

    fun goToMapFragment(fm: FragmentManager) {
        placeFragment(fm, MapsFragment())
    }


    fun goToDashboardFragment(fm: FragmentManager) {
        placeFragment(fm, DashBoardFragment())
    }



}