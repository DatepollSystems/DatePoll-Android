package com.bke.datepoll.ui.login


import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bke.datepoll.Prefs
import com.bke.datepoll.R
import com.bke.datepoll.databinding.FragmentServerInputBinding
import com.bke.datepoll.vm.LoginViewModel
import kotlinx.android.synthetic.main.fragment_server_input.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class ServerInputFragment : Fragment() {

    private val prefs: Prefs by inject()
    private val loginViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentServerInputBinding>(
            inflater, R.layout.fragment_server_input, container, false
        )
        val view = binding.root

        binding.vm = loginViewModel
        binding.lifecycleOwner = this


        return view
    }

    override fun onStart() {
        btnShowMoreServer.setOnClickListener {
            btnShowMoreServer.isEnabled = false
           advancedServerSettings.animateVisibility(true)
        }

        super.onStart()
    }

    fun View.animateVisibility(setVisible: Boolean) {
        if (setVisible) expand(this) else collapse(this)
    }

    private fun expand(view: View) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val initialHeight = 0
        val targetHeight = view.measuredHeight

        // Older versions of Android (pre API 21) cancel animations for views with a height of 0.
        //v.getLayoutParams().height = 1;
        view.layoutParams.height = 0
        view.visibility = View.VISIBLE

        animateView(view, initialHeight, targetHeight)
    }

    private fun collapse(view: View) {
        val initialHeight = view.measuredHeight
        val targetHeight = 0

        animateView(view, initialHeight, targetHeight)
    }

    private fun animateView(v: View, initialHeight: Int, targetHeight: Int) {
        val valueAnimator = ValueAnimator.ofInt(initialHeight, targetHeight)
        valueAnimator.addUpdateListener { animation ->
            v.layoutParams.height = animation.animatedValue as Int
            v.requestLayout()
        }
        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator) {
                v.layoutParams.height = targetHeight
            }

            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        valueAnimator.duration = 1000
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.start()
    }
}
