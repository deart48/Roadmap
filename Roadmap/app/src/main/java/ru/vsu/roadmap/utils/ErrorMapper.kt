package ru.vsu.roadmap.utils

import android.content.Context
import kotlinx.serialization.SerializationException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import ru.vsu.roadmap.R
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorMapper {

    fun mapAuthError(throwable: Throwable?, context: Context): String {
        return when (throwable) {
            null -> context.getString(R.string.error_unknown)
            is HttpException -> mapHttp(throwable, context, isRegister = false)
            is UnknownHostException -> context.getString(R.string.error_no_internet)
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is IOException -> context.getString(R.string.error_no_internet)
            is SerializationException -> context.getString(R.string.error_server)
            else -> context.getString(R.string.error_unknown)
        }
    }

    fun mapRegisterError(throwable: Throwable?, context: Context): String {
        if (throwable is HttpException) {
            return mapHttp(throwable, context, isRegister = true)
        }
        return mapAuthError(throwable, context)
    }

    private fun mapHttp(e: HttpException, context: Context, isRegister: Boolean): String {
        val serverMessage = readServerMessage(e)
        if (!serverMessage.isNullOrBlank()) return serverMessage

        val code = e.code()
        return when {
            code == 400 || code == 422 -> context.getString(R.string.error_bad_request)
            code == 401 || code == 403 -> context.getString(R.string.error_invalid_credentials)
            code == 409 && isRegister -> context.getString(R.string.error_email_taken)
            code in 500..599 -> context.getString(R.string.error_server)
            else -> context.getString(R.string.error_unknown)
        }
    }

    private fun readServerMessage(e: HttpException): String? {
        val raw = try {
            e.response()?.errorBody()?.string()
        } catch (_: Throwable) {
            null
        } ?: return null

        if (raw.isBlank()) return null

        return try {
            val json = JSONObject(raw)
            firstNonBlank(
                json.optString("message"),
                json.optString("error"),
                json.optString("detail"),
            )
        } catch (_: JSONException) {
            // Body is not JSON, but might be a plain message; only return short ones
            raw.take(200).takeIf { it.length < 200 || it == raw }
        }
    }

    private fun firstNonBlank(vararg candidates: String?): String? {
        for (c in candidates) {
            if (!c.isNullOrBlank()) return c
        }
        return null
    }
}
