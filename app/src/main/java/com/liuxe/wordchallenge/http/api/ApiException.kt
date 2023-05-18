package com.liuxe.wordchallenge.http.api

/**
 *  author : liuxe
 *  date : 5/17/21 10:43 AM
 *  description : 网络请求错误的数据
 */
class ApiException(
    var code: Int =800,
    var msg: String = "",
)