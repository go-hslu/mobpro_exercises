package ch.hslu.sw5.bands

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {

    private val _bandsFlow: MutableStateFlow<List<BandCode>> = MutableStateFlow(emptyList())
    val bandsFlow: Flow<List<BandCode>> = _bandsFlow

    // val currentBand: Flow<BandInfo?> = Flow<BandInfo>(null);

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        /*.addConverterFactory(
            Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()))*/
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()

    private val bandsService = retrofit.create(BandsService::class.java)

    fun resetBandsData() {
        _bandsFlow.tryEmit(emptyList())
    }

    suspend fun getBands() {
        val response = bandsService.getBandNames()
        if (response.code() == HttpURLConnection.HTTP_OK) {
            response.body().orEmpty()
        }

    }
}