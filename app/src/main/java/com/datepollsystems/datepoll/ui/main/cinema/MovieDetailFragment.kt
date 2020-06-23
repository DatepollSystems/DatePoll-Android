package com.datepollsystems.datepoll.ui.main.cinema

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginBottom
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.datepollsystems.datepoll.databinding.FragmentMovieDetailBinding
import com.datepollsystems.datepoll.vm.CinemaViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie_detail.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    val binding: FragmentMovieDetailBinding
        get() = _binding!!

    private val cinemaViewModel: CinemaViewModel by sharedViewModel()

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
        fun loadImage(view: ImageView, profileImage: String) {
            val circularProgressDrawable = CircularProgressDrawable(view.context)
            circularProgressDrawable.setStyle(CircularProgressDrawable.LARGE)
            circularProgressDrawable.start()

            Glide.with(view.context)
                .load(profileImage)
                .centerCrop()
                .into(view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        binding.vm = cinemaViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


    override fun onStart() {
        super.onStart()
        requireActivity().bottom_navigation?.visibility = View.GONE
    }
}