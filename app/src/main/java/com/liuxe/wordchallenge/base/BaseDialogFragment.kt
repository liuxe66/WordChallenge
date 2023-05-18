package com.liuxe.wordchallenge.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.liuxe.wordchallenge.R

/**
 *  author : liuxe
 *  date : 2022/5/26 3:34 下午
 *  description :
 */
open class BaseDialogFragment:DialogFragment() {
    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes resId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BaseDialogStyle);
    }

    fun toast(str: String) {
        Toast.makeText(requireActivity(),str, Toast.LENGTH_SHORT).show()
    }

    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]
        //监听网络处理情况
        mViewModel.mException.observe(this, Observer {
            toast(it.msg)
        })

        mViewModel.mMsg.observe(this, Observer {
            toast(it)
        })

        mViewModel
    }
}