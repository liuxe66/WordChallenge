package com.liuxe.wordchallenge.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.databinding.adapters.AdapterViewBindingAdapter
import coil.load
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.base.BaseDialogFragment
import com.liuxe.wordchallenge.databinding.DialogResultBinding

/**
 *  author : liuxe
 *  date : 2023/5/17 16:39
 *  description :
 */
class ResultDialog : BaseDialogFragment() {
    private lateinit var mBinding: DialogResultBinding

    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        score = arguments?.getInt("score") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.dialog_result, container)
        mBinding.apply {

            if (score ==100){
                ivResult.load(resources.getDrawable(R.drawable.ic_success_tip,null))
            } else {
                ivResult.load(resources.getDrawable(R.drawable.ic_fail_tip,null))
            }

            val animator1 = ValueAnimator.ofFloat(2.0f, 1.0f)
            animator1.repeatCount = 0
            animator1.interpolator = LinearInterpolator()
            animator1.duration = 500L
            animator1.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                ivResult.scaleX = progress
                ivResult.scaleY = progress
            }
            animator1.start()

            tvScore.text = score.toString()

            tvStar.text = (score/10).toString()
            tvHua.text = (score/10).toString()
        }
        return mBinding.root
    }

    companion object {
        fun getInstance(score: Int): ResultDialog {
            return ResultDialog().apply {
                arguments = Bundle().apply {
                    putInt("score", score)
                }
            }
        }
    }
}