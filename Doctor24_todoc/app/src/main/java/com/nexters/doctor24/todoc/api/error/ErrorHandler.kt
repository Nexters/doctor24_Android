package com.nexters.doctor24.todoc.api.error

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.nexters.doctor24.todoc.base.Error
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException
/*

object ErrorHandler {

    private const val NETWORK_ERROR_MESSAGE =
        "Please check your internet connectivity and try again!"
    private const val EMPTY_RESPONSE = "Server returned empty response."
    const val NO_SUCH_DATA = "Data not found in the database"
    const val UNKNOWN_ERROR = "An unknown error occurred!"

    fun handleError(
        view: View,
        throwable: Error,
        shouldToast: Boolean = false,
        refreshAction: () -> Unit = {}
    ) {
        if (shouldToast) {
            showLongToast(view.context, throwable.message)
        }
        when (throwable.exception) {
            is IOException -> Timber.e(NETWORK_ERROR_MESSAGE)
            is HttpException -> Timber.e(
                "HTTP Exception: ${throwable.exception.code()}"
            )
            is NoResponseException -> Timber.e(EMPTY_RESPONSE)
            is NoDataException -> Timber.e(NO_SUCH_DATA)
            else -> Timber.e(throwable.message)
        }
    }

    private fun showLongToast(context: Context, message: String) = Toast.makeText(
        context,
        message,
        Toast.LENGTH_LONG
    ).show()

    inline fun <reified T> parseError(responseBody: ResponseBody?): T? {
        val parser = Gson().getAdapter(T::class.java)
        val response = responseBody?.string()
        if (response != null)
            try {
                return parser.fromJson(response)
            } catch (e: JsonParseException) {
                e.printStackTrace()
            }
        return null
    }

}*/
fun handleError(
    code: Int,
    message: String
) : ErrorResponse{
    return ErrorResponse(
        code = code,
        message = message
    )
}