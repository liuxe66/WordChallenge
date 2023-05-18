package com.liuxe.wordchallenge.base

import android.widget.RemoteViews
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.liuxe.wordchallenge.http.api.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 *  author : liuxe
 *  date : 2021/7/12 2:24 下午
 *  description :
 */
abstract class BaseViewModel : ViewModel() {

    //消息提示
    val mMsg: MutableLiveData<String> = MutableLiveData()

    //错误提示
    val mException: MutableLiveData<ApiException> = MutableLiveData()

    //loading
    val mLoadingState: MutableLiveData<Boolean> = MutableLiveData()


}