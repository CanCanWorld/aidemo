package com.zrq.aidemo.common.network

import android.util.Log
import com.google.gson.Gson
import com.zrq.aidemo.type.DataType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val TAG = "HTTP"

    // 请求拦截器
    private val requestInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        // 创建一个新的请求并添加自定义头部
        val request = originalRequest.newBuilder()
            .method(originalRequest.method, originalRequest.body)
            .header("Authorization", "f26b87427320b67b13d446677e7540e4.2EXRWUo8VzhjmHuV")
        // 继续执行链中的下一个拦截器或发送请求
        chain.proceed(request.build())
    }

    // okHttp单例
    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor { message -> Log.d(TAG, message) }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(requestInterceptor)
            .build()
    }

    // retrofit单例
    val retrofit = Retrofit.Builder()
        .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService by lazy { retrofit.create(ApiService::class.java) }


    /**
     * 网络请求封装
     */
    suspend fun <T> request(
        block: suspend () -> Response<Any>,
        then: suspend (Any) -> Unit = {},
        catch: suspend (e: Exception) -> Unit = {},
        finally: suspend () -> Unit = {}
    ) {
        try {
            val response = block()
            val body = response.body()
            Log.d(TAG, "response: $response")
            when (response.code()) {
                200 -> {
                    if (body == null) {
                        catch(Exception("请求失败"))
                    } else {
                        then(body)
                    }
                }

                401 -> {
                    catch(Exception("登录过期"))
                }

                500 -> {
                    try {
                        val err = response.errorBody()?.string() ?: ""
                        val error = Gson().fromJson(err, DataType::class.java)
                        catch(Exception("服务器异常: $error"))
                    } catch (e: Exception) {
                        catch(Exception("服务器异常: ${response.message()}"))

                    }
                }

                else -> {
                    catch(Exception("${response.code()}: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            catch(e)
            e.printStackTrace()
        } finally {
            finally()
        }
    }

}