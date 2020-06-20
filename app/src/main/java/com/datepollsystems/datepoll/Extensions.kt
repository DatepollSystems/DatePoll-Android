package com.datepollsystems.datepoll

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import com.datepollsystems.datepoll.database.model.event.EventDateDbModel
import com.datepollsystems.datepoll.database.model.event.EventDateDto
import com.datepollsystems.datepoll.database.model.event.EventDecisionDbModel
import com.datepollsystems.datepoll.database.model.event.EventDecisionDto
import com.datepollsystems.datepoll.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

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

fun List<EventDecisionDto>.transformInDbModelList(): List<EventDecisionDbModel> {
    return if (this.isNotEmpty()) {
        val l = ArrayList<EventDecisionDbModel>()
        for (i in this) {
            l.add(
                EventDecisionDbModel(
                    id = i.id,
                    eventId = i.eventId,
                    shownInCalendar = i.showInCalendar,
                    decision = i.decision
                )
            )
        }

        l
    } else {
        ArrayList()
    }
}

fun List<EventDateDto>.transformInDbModelList(eventId: Int): List<EventDateDbModel> {
    return if (this.isNotEmpty()) {
        val l = ArrayList<EventDateDbModel>()
        for(i in this){
            l.add(
                EventDateDbModel(
                    id = i.id,
                    eventId = eventId,
                    date = i.date,
                    description = i.description,
                    x = i.x,
                    y = i.y
                )
            )
        }

        l
    } else {
        ArrayList()
    }
}

fun Date.toCalendar(): Calendar{
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}

fun showMainSnack(view: View, t: String, i: Int){
    val s = Snackbar.make(view, t, i)
    s.setAnchorView(R.id.bottom_navigation)
    s.show()
}