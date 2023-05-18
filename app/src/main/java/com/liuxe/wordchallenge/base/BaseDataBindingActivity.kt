package com.liuxe.wordchallenge.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.liuxe.wordchallenge.R
import com.liuxe.wordchallenge.utils.LogUtils
import com.liuxe.wordchallenge.utils.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeUnit

abstract class BaseDataBindingActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }


    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]

        mViewModel.mMsg.observe(this, Observer {
            toast(it)
        })

        mViewModel.mException.observe(this, Observer {
            toast(it.msg)
        })

        mViewModel.mLoadingState.observe(this, Observer {
            if (it) {
                showLoadingDialog()
            } else {
                dissmissLoadingDialog()
            }
        })

        mViewModel
    }

    var loadingDialog: Dialog? = null
    open fun showLoadingDialog() {
        var builder = AlertDialog.Builder(this, R.style.TransparentDialog)

//        val contentView =
//            LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
//
//        builder.setView(contentView)
        builder.setCancelable(false)
        loadingDialog = builder.create()

        loadingDialog?.show()
    }

    open fun dissmissLoadingDialog() {
        runOnUiThread {
            loadingDialog?.dismiss()
        }
    }

    override fun getResources(): Resources {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.getDisplayMetrics())
        return res
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 处理网络请求失败
     */
    open fun onError(errorMsg: String) {

    }

    var mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }

    open fun initStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .statusBarDarkFont(true)
            .fitsSystemWindows(true)
            .keyboardEnable(false)
            .init()
    }

    fun View.throttleClick(
        interval: Long = 200,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        block: View.() -> Unit
    ) {
        setOnClickListener(ThrottleClickListener(interval, unit,  block))
    }


    inner class ThrottleClickListener(
        private val interval: Long = 200,
        private val unit: TimeUnit = TimeUnit.MILLISECONDS,
        private var block: View.() -> Unit
    ) : View.OnClickListener {

        private var lastTime: Long = 0

        override fun onClick(v: View) {

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastTime > unit.toMillis(interval)) {
                lastTime = currentTime

                playSound(R.raw.click)

                v.animate().scaleY(0.9f).scaleX(0.9f).setDuration(100).withEndAction {
                    v.animate().scaleY(1.0f).scaleX(1.0f).setDuration(100).withEndAction {
                        block(v)
                    }
                }
            }

        }
    }

    lateinit var  mediaPlayer:MediaPlayer

    fun playSound(souce: Int, isLooping: Boolean = false) {
        try {
            mediaPlayer = MediaPlayer.create(this@BaseDataBindingActivity, souce)
            mediaPlayer.isLooping = isLooping
            mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
            mediaPlayer.setOnCompletionListener { mediaPlayer.release() }
        } catch (e: Exception) {
            LogUtils.e("e:" + e.message)
        }
    }

    /**
     * 播放单词发音
     * @param soundUrlDict String
     */
    fun playOnlineSound(soundUrlDict: String) {

            try {
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(soundUrlDict)
                mediaPlayer.prepareAsync()
                mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
                mediaPlayer.setOnCompletionListener { mp ->
                    mp?.release()
                }
                mediaPlayer.setOnErrorListener { p0, p1, p2 ->
                    false
                }
            } catch (e: IOException) {

            }


    }

}