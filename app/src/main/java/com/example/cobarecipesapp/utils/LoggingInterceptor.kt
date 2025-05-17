package com.example.cobarecipesapp.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Логирование запроса
        val requestStartTime = System.nanoTime()
        logRequest(request, chain)

        // Выполнение запроса
        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            logError(request, e)
            throw e
        }

        // Логирование ответа
        logResponse(response, requestStartTime)

        return response
    }

    private fun logRequest(request: okhttp3.Request, chain: Interceptor.Chain) {
        Log.d(TAG, """
            |--> Sending ${request.method} request to ${request.url}
            |Headers: ${request.headers}
            |Connection: ${chain.connection()}
            |Body: ${request.body?.contentType()}
            """.trimMargin())
    }

    private fun logResponse(response: Response, startTime: Long) {
        val durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)
        Log.d(TAG, """
            |<-- Received response for ${response.request.url} in $durationMs ms
            |Status: ${response.code} ${response.message}
            |Headers: ${response.headers}
            |Body: ${response.peekBody(1024).string()}
            """.trimMargin())
    }

    private fun logError(request: okhttp3.Request, e: IOException) {
        Log.e(TAG, """
            |<-- HTTP FAILED: ${request.method} ${request.url}
            |Error: ${e.message}
            """.trimMargin())
    }

    companion object {
        private const val TAG = "Network"
    }
}