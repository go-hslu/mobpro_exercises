package ch.hslu.sw5.bands

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BandsService {

    @GET("all.json")
    suspend fun getBandNames(): Response<List<BandCode>>

    @GET("info/{code}.json")
    suspend fun getBandInfo(@Path("code") code: String): Response<BandInfo>
}