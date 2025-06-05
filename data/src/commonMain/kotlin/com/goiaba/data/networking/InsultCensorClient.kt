package com.goiaba.data.networking

import com.goiaba.data.util.NetworkError
import com.goiaba.shared.util.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

import kotlinx.coroutines.runBlocking

class InsultCensorClient(
    private val httpClient: HttpClient
) {

    suspend fun httppp(){
        val client = HttpClient(CIO) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(username = "jetbrains", password = "foobar")
                    }
                    realm = "Access to the '/' path"
                }
            }
        }
        val response: HttpResponse = client.get("http://0.0.0.0:8080/")
        println(response.bodyAsText())
        client.close()
    }


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