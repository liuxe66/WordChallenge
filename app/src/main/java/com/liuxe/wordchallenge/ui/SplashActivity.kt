package com.liuxe.wordchallenge.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.base.BaseDataBindingActivity
import com.liuxe.wordchallenge.databinding.ActivitySplashBinding
import com.liuxe.wordchallenge.ext.toActivity
import com.liuxe.wordchallenge.ui.vm.MainVM
import com.liuxe.wordchallenge.utils.Preference

class SplashActivity : BaseDataBindingActivity() {

    private val mainVM by createViewModel<MainVM>()
    private var hasLoadBook by Preference(Preference.isFirstLoad,true)

    private val mBinding by binding<ActivitySplashBinding>(R.layout.activity_splash)
    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).keyboardEnable(false)
            .init()
    }
    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {

            ivPersopn.setImageResource(R.drawable.p1anim)

            (ivPersopn.drawable as AnimationDrawable).start()

            // 要实现抖动的view
            var downAnim = ObjectAnimator.ofFloat(ivPersopn, "translationY", 0F, 5F, -5F)
            downAnim.repeatMode = ValueAnimator.REVERSE
            downAnim.repeatCount = ValueAnimator.INFINITE
            downAnim.duration = 200
            downAnim.start()

            if (hasLoadBook){
                mainVM.init().observe(this@SplashActivity, Observer {
                    hasLoadBook = false
                    toActivity<GameMainActivity>()
                })
            } else {
                toActivity<GameMainActivity>()
            }

        }
    }

}