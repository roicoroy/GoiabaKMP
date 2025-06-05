package com.goiaba.data.networking

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class InsultCensorClient() {

//    suspend fun getPostsÏ() {
//        val response = try {
//            httpClient.get(
//                urlString = straiUrl
//            ).
//        } catch(e: UnresolvedAddressException) {
//
//        } catch(e: SerializationException) {
//
//        } as HttpResponse
//
//        fun printLog(value:String){
//            println("CensoredText::: $value")
//        }
//
//        return when(response.status.value) {
//            in 200..299 -> {
//                val censoredText = response.body<CensoredText>()
//                RequestState.Success(censoredText.result)
//                printLog(censoredText.result)
//            }
//            401 -> {
//                RequestState.Error(NetworkError.UNAUTHORIZED.toString())
//                printLog(NetworkError.UNAUTHORIZED.toString())
//            }
//            409 -> {
//                RequestState.Error(NetworkError.CONFLICT.toString())
//                printLog(NetworkError.CONFLICT.toString())
//            }
//            408 -> {
//                RequestState.Error(NetworkError.REQUEST_TIMEOUT.toString())
//                printLog(NetworkError.REQUEST_TIMEOUT.toString())
//            }
//            413 -> {
//                RequestState.Error(NetworkError.PAYLOAD_TOO_LARGE.toString())
//                printLog(NetworkError.PAYLOAD_TOO_LARGE.toString())
//            }
//            in 500..599 -> {
//                RequestState.Error(NetworkError.SERVER_ERROR.toString())
//                printLog(NetworkError.SERVER_ERROR.toString())
//            }
//            else -> {
//                RequestState.Error(NetworkError.UNKNOWN.toString())
//                printLog(NetworkError.UNKNOWN.toString())
//            }
//        }
//    }
}