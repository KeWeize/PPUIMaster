package com.pilaipiwang.pui

/**
 * @author:  vitar
 * @date:    2019/11/5
 */
internal object PUILog {

    private var sDelegete: PUILogDelegate? = null

    @JvmStatic
    fun setDelegete(delegete: PUILogDelegate) {
        sDelegete = delegete
    }

    @JvmStatic
    fun e(tag: String, msg: String, vararg obj: Any) {
        sDelegete?.e(tag, msg, *obj)
    }

    @JvmStatic
    fun w(tag: String, msg: String, vararg obj: Any) {
        sDelegete?.w(tag, msg, *obj)
    }

    @JvmStatic
    fun i(tag: String, msg: String, vararg obj: Any) {
        sDelegete?.i(tag, msg, *obj)
    }

    @JvmStatic
    fun d(tag: String, msg: String, vararg obj: Any) {
        sDelegete?.d(tag, msg, *obj)
    }

    @JvmStatic
    fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any) {
        sDelegete?.printErrStackTrace(tag, tr, format, *obj)
    }

}

interface PUILogDelegate {

    fun e(tag: String, msg: String, vararg obj: Any)
    fun w(tag: String, msg: String, vararg obj: Any)
    fun i(tag: String, msg: String, vararg obj: Any)
    fun d(tag: String, msg: String, vararg obj: Any)
    fun printErrStackTrace(tag: String, tr: Throwable, format: String, vararg obj: Any)

}