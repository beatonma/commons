package org.beatonma.commons.app.ui.recyclerview.itemanimator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.app.ui.Interpolation

/**
 * Created by Michael on 24/06/2016.
 */
class FadeItemAnimator(
        private val itemDelayMs: Int = 30
) : DefaultItemAnimator() {

    companion object {
        private const val TAG = "SlideInItemAnimator"
    }

    private val pendingAdds = ArrayList<RecyclerView.ViewHolder>()
    private val pendingRemoves = ArrayList<RecyclerView.ViewHolder>()

    private val enterInterpolator = Interpolation.enter
    private val exitInterpolator = Interpolation.exit

    init {
        addDuration = 160L
        removeDuration = 120L
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        pendingAdds.add(holder)
        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        pendingRemoves.add(holder)
        return true
    }

    override fun runPendingAnimations() {
        super.runPendingAnimations()
        if (!pendingAdds.isEmpty()) {
            for (i in pendingAdds.indices.reversed()) {
                val holder = pendingAdds[i]
                ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0F, 0F, 1F).apply {
                    duration = addDuration
                    startDelay = (holder.bindingAdapterPosition * itemDelayMs).toLong()
                    addListener(object: AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            dispatchAddStarting(holder)
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            animation.listeners.remove(this)
                            dispatchAddFinished(holder)
                            dispatchFinishedWhenDone()
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            clearAnimatedValues(holder.itemView)
                        }
                    })
                }.start()
                pendingAdds.removeAt(i)
            }
        }
        if (pendingRemoves.isNotEmpty()) {
            for (i in pendingRemoves.indices.reversed()) {
                val holder = pendingRemoves[i]
                holder.itemView.animate()
                        .alpha(0f)
                        .setDuration(removeDuration)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator) {
                                dispatchRemoveStarting(holder)
                            }

                            override fun onAnimationEnd(animation: Animator) {
                                animation.listeners.remove(this)
                                dispatchRemoveFinished(holder)
                                dispatchFinishedWhenDone()
                            }

                            override fun onAnimationCancel(animation: Animator) {
                                clearAnimatedValues(holder.itemView)
                            }
                        })
                        .setInterpolator(exitInterpolator)
                        .start()
                pendingRemoves.removeAt(i)
            }
        }
    }

    override fun endAnimation(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().cancel()
        if (pendingAdds.remove(holder)) {
            dispatchAddFinished(holder)
            clearAnimatedValues(holder.itemView)
        }
        if (pendingRemoves.remove(holder)) {
            dispatchRemoveFinished(holder)
            clearAnimatedValues(holder.itemView)
        }
        super.endAnimation(holder)
    }

    override fun endAnimations() {
        for (i in pendingAdds.indices.reversed()) {
            val holder = pendingAdds[i]
            clearAnimatedValues(holder.itemView)
            dispatchAddFinished(holder)
            pendingAdds.removeAt(i)
        }
        for (i in pendingRemoves.indices.reversed()) {
            val holder = pendingRemoves[i]
            clearAnimatedValues(holder.itemView)
            dispatchRemoveFinished(holder)
            pendingRemoves.removeAt(i)
        }
        super.endAnimations()
    }

    override fun isRunning(): Boolean {
        return !pendingAdds.isEmpty() || super.isRunning()
    }

    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    private fun clearAnimatedValues(view: View) {
        view.alpha = 1f
    }
}
