package com.example.loldex.core.network.retrofit

import androidx.tracing.trace
import com.example.loldex.core.network.YugiohNetworkDataSource
import com.example.loldex.core.network.model.response.CardListResponse
import com.example.loldex.core.network.model.response.CardPagingListResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

private interface RetrofitYugiohNetworkApi {
    @GET("cardinfo.php")
    suspend fun getYugiohPagingList(
        @Query("num") num: Int,
        @Query("offset") offset: Int,
    ): ApiResponse<CardPagingListResponse>

    @GET("cardinfo.php")
    suspend fun getYugiohCardDataById(
        @Query("id") id: Long,
    ): ApiResponse<CardListResponse>

    @GET("cardinfo.php")
    suspend fun getYugiohCardDataByName(
        @Query("name") name: String,
    ): ApiResponse<CardListResponse>
}

class RetrofitYugiohNetwork @Inject constructor(
    private val networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>
) : YugiohNetworkDataSource {

    private val baseUrl = "https://db.ygoprodeck.com/api/v7/"

    private val networkApi = trace("RetrofitYugiohNetwork") {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                networkJson.asConverterFactory("application/json".toMediaType()),
            )
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(RetrofitYugiohNetworkApi::class.java)
    }

    override suspend fun getYugiohPagingList(
        num: Int,
        offset: Int
    ): ApiResponse<CardPagingListResponse> =
        networkApi.getYugiohPagingList(num = num, offset = offset)

    override suspend fun getYugiohCardDataById(id: Long): ApiResponse<CardListResponse> =
        networkApi.getYugiohCardDataById(id)

    override suspend fun getYugiohCardDataByName(name: String): ApiResponse<CardListResponse> =
        networkApi.getYugiohCardDataByName(name)
}