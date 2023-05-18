package com.liuxe.wordchallenge.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.app.AppConfig
import com.liuxe.wordchallenge.base.BaseDataBindingActivity
import com.liuxe.wordchallenge.databinding.ActivityGameMainBinding
import com.liuxe.wordchallenge.ext.launchActivity
import com.liuxe.wordchallenge.ui.adapter.BookAdapter
import com.liuxe.wordchallenge.ui.vm.MainVM

class GameMainActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityGameMainBinding>(R.layout.activity_game_main)

    private val mBookAdapter by lazy { BookAdapter() }

    private val mMainVM by createViewModel<MainVM>()

    var roler = 1

    override fun initStatusBar() {
        ImmersionBar.with(this).fullScreen(true).transparentStatusBar().statusBarDarkFont(false)
            .keyboardEnable(false).init()
    }

    override fun onResume() {
        super.onResume()
        mBinding.apply {
            mMainVM.loadAllBook().observe(this@GameMainActivity, Observer {
                mBookAdapter.submitList(it)
                vpBook.adapter = mBookAdapter
            })

            mMainVM.loadUser().observe(this@GameMainActivity, Observer {
                tvHua.text = it.coin.toString()
                tvStar.text = it.wordNum.toString()
            })
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        mBinding.apply {

            roler = (1 until 5).random()
            ivPersopn.setImageResource(AppConfig.getPreson(roler%6))
            (ivPersopn.drawable as AnimationDrawable).start()

            // 要实现抖动的view

            var downAnim = ObjectAnimator.ofFloat(ivPersopn, "translationY", 0F, 5F, -5F)
            downAnim.repeatMode = ValueAnimator.REVERSE
            downAnim.repeatCount = ValueAnimator.INFINITE
            downAnim.duration = 200
            downAnim.start()

            vpBook.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mMainVM.setCurIndex(position)
                }
            })
            mMainVM.bookIndex.observe(this@GameMainActivity, Observer {
                vpBook.currentItem = it
            })

            ivLeft.throttleClick {
                mMainVM.toLeft()
            }
            ivRight.throttleClick {
                mMainVM.toRight()
            }
            ivStart.throttleClick {
                if (mMainVM.getCurBook().isLock) {
                    toast("请先解锁")
                } else {
                    launchActivity<GameActivity> {
                        putExtra("bookId", mMainVM.getCurBook().bookId)
                    }
                }
            }
            ivRolerChangeBtn.throttleClick {
                roler += 1
                ivPersopn.setImageResource(AppConfig.getPreson(roler%6))
                (ivPersopn.drawable as AnimationDrawable).start()
            }
        }
    }
}