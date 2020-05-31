package com.example.bulkemailapp.utils

import android.R
import android.content.Context
import android.view.View
import android.view.animation.*


object Animations {
    fun setScaleAnimation(view: View, duration: Int) {
        val anim = ScaleAnimation(
            0.0f,
            1.0f,
            0.0f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        anim.duration = duration.toLong()
        view.startAnimation(anim)
    }

    fun setFadeAnimation(view: View, duration: Int) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = duration.toLong()
        view.startAnimation(anim)
    }

    fun setFadeOutAnimation(view: View, duration: Int) {
        val anim = AlphaAnimation(1.0f, 0.0f)
        anim.duration = duration.toLong()
        view.startAnimation(anim)
    }

    fun setScrollUpAnimation(context: Context?, view: View, duration: Int) {
        val set = AnimationSet(true)
        val trAnimation: Animation = TranslateAnimation(0f, 0f, 20f, 0f)
        trAnimation.duration = duration.toLong()
        trAnimation.repeatMode =
            Animation.ABSOLUTE // This will make the view translate in the reverse direction
        set.addAnimation(trAnimation)
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = duration.toLong()
        set.addAnimation(anim)
        set.setInterpolator(context, R.anim.decelerate_interpolator)
        view.startAnimation(set)
    }

    fun setScrollDownAnimation(context: Context?, view: View, duration: Int) {
        val set = AnimationSet(true)
        val trAnimation: Animation = TranslateAnimation(0f, 0f, 0f, -20f)
        trAnimation.duration = duration.toLong()
        trAnimation.repeatMode =
            Animation.ABSOLUTE // This will make the view translate in the reverse direction
        set.addAnimation(trAnimation)
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = duration.toLong()
        set.addAnimation(anim)
        set.setInterpolator(context, R.anim.decelerate_interpolator)
        view.startAnimation(set)
    }

    fun setScrollRightAnimation(context: Context?, view: View, duration: Int) {
        val set = AnimationSet(true)
        val trAnimation: Animation = TranslateAnimation(-10f, 0f, 0f, 0f)
        trAnimation.duration = duration.toLong()
        trAnimation.repeatMode =
            Animation.ABSOLUTE // This will make the view translate in the reverse direction
        set.addAnimation(trAnimation)
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = duration.toLong()
        set.addAnimation(anim)
        set.setInterpolator(context, R.anim.decelerate_interpolator)
        view.startAnimation(set)
    }

    fun setScrollLeftAnimation(context: Context?, view: View, duration: Int) {
        val set = AnimationSet(true)

        val trAnimation: Animation = TranslateAnimation(0f, -10f, 0f, 0f)
        trAnimation.duration = duration.toLong()
        trAnimation.repeatMode =
            Animation.ABSOLUTE // This will make the view translate in the reverse direction

        set.addAnimation(trAnimation)

        val anim: Animation = AlphaAnimation(1.0f, 0.0f)
        anim.duration = duration.toLong()

        set.addAnimation(anim)

        set.setInterpolator(context, R.anim.decelerate_interpolator)

        view.startAnimation(set)
    }
}