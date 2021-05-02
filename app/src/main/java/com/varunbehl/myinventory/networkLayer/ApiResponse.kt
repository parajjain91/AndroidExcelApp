package com.varunbehl.myinventory.networkLayer

import retrofit2.Response
import java.net.UnknownHostException
import java.util.regex.Pattern

/**
 * Common class used by API responses.
 * @param <T> the type of the exhibitorModel object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            error.printStackTrace()
            return if (error is UnknownHostException) {
                ApiErrorResponse("", "400")
            } else {
                ApiErrorResponse(
                    error.message ?: ""
                )
            }
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            if (response.isSuccessful) {
                val body = response.body()
                return if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body = body,
                        linkHeader = response.headers()["link"]
                    )
                }
            } else {
                return ApiEmptyResponse()

//                val msg = response.errorBody()?.string()
//                return if (!msg.isNullOrEmpty()) {
////                    try {
//////                        val errorParsed = ErrorParser.parseErrorJson(msg)
//////                        ApiErrorResponse(
//////                            errorParsed?.response?.error?.errorDescription ?: "",
//////                            errorParsed?.response?.error?.code ?: "-1"
//////                        )
////                    } catch (e: Exception) {
////                        e.printStackTrace()
////                    ApiErrorResponse("", response.code().toString())
////                }
//            } else {
//                    ApiErrorResponse("", response.code().toString())
//                }
            }
//        }
        }
    }

    /**
     * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
     */
    class ApiEmptyResponse<T> : ApiResponse<T>()

    data class ApiSuccessResponse<T>(
        val body: T,
        val links: Map<String, String>
    ) : ApiResponse<T>() {
        constructor(body: T, linkHeader: String?) : this(
            body = body,
            links = linkHeader?.extractLinks() ?: emptyMap()
        )

        companion object {
            private val LINK_PATTERN =
                Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
            private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
            private const val NEXT_LINK = "next"

            private fun String.extractLinks(): Map<String, String> {
                val links = mutableMapOf<String, String>()
                val matcher = LINK_PATTERN.matcher(this)

                while (matcher.find()) {
                    val count = matcher.groupCount()
                    if (count == 2) {
                        links[matcher.group(2)] = matcher.group(1)
                    }
                }
                return links
            }

        }
    }

    data class ApiErrorResponse<T>(val errorMessage: String, val errorCode: String = "-1") :
        ApiResponse<T>()
}

