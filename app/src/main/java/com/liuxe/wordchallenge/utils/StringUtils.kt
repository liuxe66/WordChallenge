package com.liuxe.wordchallenge.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Author: liuxe
 * Time: 2018/1/8 0008 16:55
 * Desc: 字符串工具类
 */
object StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    fun isEmpty(value: String?): Boolean {
        return !(value != null && !"".equals(value.trim { it <= ' ' }, ignoreCase = true)
                && !"null".equals(value.trim { it <= ' ' }, ignoreCase = true))
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param email email
     * @return 字符串是否是邮箱
     */
    fun isEmail(email: String?): Boolean {
        val str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(" +
                "([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p = Pattern.compile(str)
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 判断手机号字符串是否合法
     *
     * @param phoneNumber 手机号字符串
     * @return 手机号字符串是否合法
     */
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        var isValid = false
        //        String expression = "^1[3|4|5|6|7|8|9]\\d{9}$";
        val expression = "^[1]([3-9])[0-9]{9}$"
        val inputStr: CharSequence = phoneNumber
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    /**
     * 判断手机号字符串是否合法
     *
     * @param areaCode    区号
     * @param phoneNumber 手机号字符串
     * @return 手机号字符串是否合法
     */
    fun isPhoneNumberValid(areaCode: String?, phoneNumber: String): Boolean {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false
        }
        if (phoneNumber.length < 5) {
            return false
        }
        if (TextUtils.equals(areaCode, "+86") || TextUtils.equals(areaCode, "86")) {
            return isPhoneNumberValid(phoneNumber)
        }
        var isValid = false
        val expression = "^[0-9]*$"
        val inputStr: CharSequence = phoneNumber
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    /**
     * 判断字符串是否是手机号格式
     *
     * @param areaCode    区号
     * @param phoneNumber 手机号字符串
     * @return 字符串是否是手机号格式
     */
    fun isPhoneFormat(areaCode: String?, phoneNumber: String): Boolean {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false
        }
        if (phoneNumber.length < 7) {
            return false
        }
        var isValid = false
        val expression = "^[0-9]*$"
        val inputStr: CharSequence = phoneNumber
        val pattern = Pattern.compile(expression)
        val matcher = pattern.matcher(inputStr)
        if (matcher.matches()) {
            isValid = true
        }
        return isValid
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str 字符串
     * @return 是否纯数字
     */
    fun isNumber(str: String): Boolean {
        for (i in 0 until str.length) {
            if (!Character.isDigit(str[i])) {
                return false
            }
        }
        return true
    }


    fun toChinese(str: String): String {
        val s1 = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
        val s2 = arrayOf("十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千")
        var result = ""
        val n = str.length
        for (i in 0 until n) {
            val num = str[i].code - '0'.code
            result += if (i != n - 1 && num != 0) {
                s1[num] + s2[n - 2 - i]
            } else {
                s1[num]
            }
        }
        return result
    }
}