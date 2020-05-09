package cn.thens.dex_gradle

import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author 7hens
 */
internal object MyLogger {
    private const val LOG_TAG = "@(dex.gradle): "

    fun log(msg: Any?) {
        println(LOG_TAG + msg)
    }

    fun error(msg: Any?) {
        System.err.println(LOG_TAG + when (msg) {
            null -> null
            is Throwable -> getStackTrace(msg)
            else -> msg.toString()
        })
    }

    private fun getStackTrace(error: Throwable): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        error.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}