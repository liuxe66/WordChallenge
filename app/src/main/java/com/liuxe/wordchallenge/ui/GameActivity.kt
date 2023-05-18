package com.liuxe.wordchallenge.ui

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Observer
import coil.load
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.app.AppConfig
import com.liuxe.wordchallenge.base.BaseDataBindingActivity
import com.liuxe.wordchallenge.databinding.ActivityGameBinding
import com.liuxe.wordchallenge.ext.*
import com.liuxe.wordchallenge.ui.vm.GameWordVM
import com.liuxe.wordchallenge.utils.LogUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameActivity : BaseDataBindingActivity() {

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).keyboardEnable(false)
            .init()
    }

    //选择的位置 0 A 1 B
    private var curPosition = 0

    //
    private var bookId: Int = 1

    private val mGameVM: GameWordVM by createViewModel()

    private val mBinding by binding<ActivityGameBinding>(R.layout.activity_game)
    override fun init(savedInstanceState: Bundle?) {
        bookId = intent.getIntExtra("bookId", 1)
        mBinding.apply {

//            playSound(R.raw.bgmusic,true)

            initGame()
            initRoadAnim()
            initPersonAnim()

            gpAb.gone()
            gpResult.gone()

            tvA.throttleClick {
                selectAnswer(0)
            }
            tvB.throttleClick {
                selectAnswer(1)
            }

            ivBack1.throttleClick {
                finish()
            }

            tvAgain.throttleClick {
                gpResult.gone()
                againGame()
            }
        }
    }

    /**
     * 道路动画
     */
    private fun initRoadAnim() {

        mBinding.apply {
            val animator = ValueAnimator.ofFloat(0.0f, -1.0f)
            animator.repeatCount = ValueAnimator.INFINITE
            animator.interpolator = LinearInterpolator()
            animator.duration = 2000L
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                val height: Float = ivBg1.height.toFloat()
                val transY = height * progress
                ivBg1.translationY = transY
                ivBg2.translationY = height + transY
            }
            animator.start()
        }

    }

    /**
     * 人物动画
     */
    private fun initPersonAnim() {
        mBinding.apply {
            ivPersopn.setImageResource(R.drawable.p2anim)
            (ivPersopn.drawable as AnimationDrawable).start()

            // 要实现抖动的view
            var downAnim = ObjectAnimator.ofFloat(ivPersopn, "translationY", 0F, 5F, -5F)
            downAnim.repeatMode = ValueAnimator.REVERSE
            downAnim.repeatCount = ValueAnimator.INFINITE
            downAnim.duration = 200
            downAnim.start()
        }


    }

    /**
     * 选择答案
     * @param position Int
     */
    fun selectAnswer(position: Int) {


        mBinding.apply {

            when (position) {
                0 -> {
                    consSelectRoad.translationX = 0.0f
                }
                1 -> {
                    consSelectRoad.translationX = consSelectRoad.width.toFloat()
                }
            }
            consSelectRoad.visible()

            if (position == curPosition) {
                return
            }
            curPosition = position
            //执行人物平移动画
            val animator = ValueAnimator.ofFloat(0.0f, (ivBg1.width / 2).toFloat() - 30.dp())
            animator.repeatCount = 0
            animator.interpolator = LinearInterpolator()
            animator.duration = 500L
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                if (position == 1) {
                    ivEye.translationX = progress
                    ivPersopn.translationX = progress
                } else {
                    ivPersopn.translationX = (ivBg1.width / 2).toFloat() - 30.dp() - progress
                    ivEye.translationX = (ivBg1.width / 2).toFloat() - 30.dp() - progress
                }

                if (progress > (ivBg1.width / 2).toFloat() - 30.dp() - 1) {
                    animator.pause()
                }
            }
            animator.start()
        }
    }

    /**
     * 加载单词
     */
    fun initGame() {

        mGameVM.initGameWord(bookId)
        //加载单词
        var second = 3
        mBinding.apply {
            ivSecond.load(AppConfig.getPicWithNumber(second))
            second--
            tvQuestion.text = "单词加载中"
            playSound(R.raw.readygo)
            mainScope.launch {
                while (second >= 0) {
                    delay(1000)
                    ivSecond.load(AppConfig.getPicWithNumber(second))
                    if (second == 0) {
                        startCountDown()
                    }
                    second--
                }
            }
        }
    }

    fun againGame(){
        mGameVM.initGameWord(bookId)
        startCountDown()
    }

    fun startCountDown() {
        var second = 8
        mBinding.apply {
            if (mGameVM.curQuestionPosition == 10) {
                handleSuccess()
            } else {
                mGameVM.nextQustion().observe(this@GameActivity, Observer {
                    gpAb.visible()
                    groupQuestion.visible()
                    ivPersopn.scaleX = 1.0f
                    ivPersopn.scaleY = 1.0f
                    ivPersopn.visible()
                    playOnlineSound("https://dict.youdao.com/dictvoice?audio=${it.word.name}&type=2")
                    tvQuestion.text = "${mGameVM.curQuestionPosition+1}.${it.word.name}"
                    if (it.errorAnswerPosition != 0) {
                        tvTrans.text = "A.${it.word.trans}\nB.${it.otherWord.trans}"
                    } else {
                        tvTrans.text = "A.${it.otherWord.trans}\nB.${it.word.trans}"
                    }
                    ivSecond.load(AppConfig.getPicWithNumber(second))
                    second--
                    mainScope.launch {
                        while (second >= 0) {
                            delay(1000)
                            ivSecond.load(AppConfig.getPicWithNumber(second))

                            if (second == 0) {
                                delay(1000)
                                var obs = (0 until 2).random()
                                when (obs) {
                                    0 -> handleObs1Answer(it.errorAnswerPosition)
                                    1 -> handleObs2Answer(it.errorAnswerPosition)
                                }
                            }
                            second--
                        }

                    }
                })
            }


        }
    }

    /**
     * 障碍物1
     * @param errorPosition Int 错误答案的位置
     */
    fun handleObs1Answer(errorPosition: Int) {
        mBinding.apply {
            ivObs1.visible()
            when (errorPosition) {
                0 -> {
                    ivObs1.translationX = 0.0f
                }
                else -> {
                    ivObs1.translationX = ivObs1.width.toFloat()
                }
            }

            if (curPosition == errorPosition){
                ivEye.visible()
            } else {
                ivEye.gone()
            }
            groupQuestion.gone()
            var playFailAnim = true
            val animator = ValueAnimator.ofFloat(0.0f, 0.5f, 1.0f)
            animator.repeatCount = 0
            animator.interpolator = LinearInterpolator()
            animator.duration = 2000L
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                val height: Float = ivBg1.height.toFloat()
                val transY = height - (height * progress)
                ivObs1.translationY = transY
                if (consSelectRoad.isVisible()) {
                    consSelectRoad.invisible()
                }

                if (progress > 0.99f) {
                    ivObs1.invisible()
                    animation.pause()
                    if (curPosition == errorPosition) {
                        playSound(R.raw.fail)
                        handleFail()
                    } else {
                        startCountDown()
                    }
                }
                if (curPosition == errorPosition) {

                    LogUtils.e("Obs1 progress:"+progress)
                    if (progress >= 0.5f) {
                        ivEye.gone()
                        if (playFailAnim){
//                            playSound(R.raw.attach)
                            playFailAnim = false
                            val animator1 =
                                ValueAnimator.ofFloat(1.0f, 0.8f, 0.6f, 0.4f, 0.2f, 0.0f)
                            animator1.repeatCount = 0
                            animator1.interpolator = LinearInterpolator()
                            animator1.duration = 500L
                            animator1.addUpdateListener { animation ->
                                val progress = animation.animatedValue as Float
                                ivPersopn.scaleX = progress
                                ivPersopn.scaleY = progress
                            }
                            animator1.start()
                        }

                    }
                }
            }
            animator.start()
        }

    }

    /**
     * 障碍物2
     * @param errorPosition Int 错误答案的位置
     */
    fun handleObs2Answer(errorPosition: Int) {
        mBinding.apply {
            //障碍物显示
            ivObs2.visible()
            //判断障碍物显示的位置
            when (errorPosition) {
                0 -> {
                    ivObs2.translationX = 0.0f
                }
                else -> {
                    ivObs2.translationX = ivObs2.width.toFloat()
                }
            }
            //隐藏问题内容
            groupQuestion.gone()
            if (curPosition == errorPosition){
                ivEye.visible()
            } else {
                ivEye.gone()
            }

            var playFailAnim = true
            //执行动画
            val animator = ValueAnimator.ofFloat(0.0f, 0.5f, 1.0f)
            animator.repeatCount = 0
            animator.interpolator = LinearInterpolator()
            animator.duration = 2000L
            animator.addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                val height: Float = ivBg1.height.toFloat()
                val transY = height - (height * progress)
                //向前跑的动作
                ivObs2.translationY = transY
                if (consSelectRoad.isVisible()) {
                    consSelectRoad.invisible()
                }
                if (progress > 0.99f) {
                    ivObs2.invisible()
                    animation.pause()
                    if (curPosition == errorPosition) {
                        playSound(R.raw.fail)
                        handleFail()
                    } else {
                        startCountDown()
                    }
                }
                if (curPosition == errorPosition) {
                    LogUtils.e("Obs2 progress:"+progress)

                    if (progress >= 0.5f) {
                        ivEye.gone()
                        if (playFailAnim){
//                            playSound(R.raw.attach)
                            playFailAnim = false
                            val animator1 = ValueAnimator.ofFloat(1.0f, 0.0f)
                            animator1.repeatCount = 0
                            animator1.interpolator = LinearInterpolator()
                            animator1.duration = 500L
                            animator1.addUpdateListener { animation ->
                                val progress = animation.animatedValue as Float
                                val transY = -ivPersopn.top * (1 - progress)
                                ivPersopn.scaleX = progress
                                ivPersopn.scaleY = progress
                                ivPersopn.translationY = transY
                            }

                            var animator2 = ObjectAnimator.ofFloat(ivPersopn, "rotation", 0f, 360f)
                            animator2.repeatCount = 0
                            animator2.interpolator = LinearInterpolator()
                            animator2.duration = 500L
                            animator1.start()
                            animator2.start()
                        }
                    }
                }
            }
            animator.start()
        }
    }

    fun handleFail(){
        mGameVM.handleFail().observe(this@GameActivity, Observer {
            handleResultView(it)
        })
    }

    fun handleSuccess(){
        playSound(R.raw.success)
        mGameVM.handleSuccess().observe(this@GameActivity, Observer {
           handleResultView(it)
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun handleResultView(score:Int){
        mBinding.apply {
            gpResult.visible()
            playSound(R.raw.addresult)
            if (score ==100){
                ivResult.load(resources.getDrawable(R.drawable.ic_success_tip,null))
                ivTxt.load(resources.getDrawable(R.drawable.ic_result_success_tip_txt,null))
                tvStar.text = "10"
                tvHua.text = "10"
            } else {
                ivResult.load(resources.getDrawable(R.drawable.ic_fail_tip,null))
                ivTxt.load(resources.getDrawable(R.drawable.ic_result_fail_tip_txt,null))
                tvStar.text = (score/10).toString()
                tvHua.text = "0"
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

        }

    }
}