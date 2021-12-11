package kr.goldenmine.ppcalculatorweb.osuapi.api

import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOsuApiClient {
    val instance: Retrofit
    private val gson = GsonBuilder().setLenient().create()
    private const val BASE_URL = "https://osu.ppy.sh/p/api/"

    init {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

//        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}