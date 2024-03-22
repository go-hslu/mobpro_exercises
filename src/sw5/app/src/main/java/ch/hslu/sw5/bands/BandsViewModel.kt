package ch.hslu.sw5.bands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {

    private val _bands: MutableStateFlow<List<BandCode>> = MutableStateFlow(emptyList())
    val bands: Flow<List<BandCode>> = _bands

    private val _currentBand: MutableStateFlow<BandInfo?> = MutableStateFlow(null)
    val currentBand: Flow<BandInfo?> = _currentBand

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(
            Json.asConverterFactory(
                "application/json; charset=UTF8".toMediaType()))
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()

    private val bandsService = retrofit.create(BandsService::class.java)

    fun loadBands() {
        Log.w("DEBUG", "Loading bands")

        viewModelScope.launch {
            val bands = getBandCodes()
            bands?.let { _bands.emit(bands) }
        }
    }

    fun resetBands() {
        Log.w("DEBUG", "Resetting bands")

        viewModelScope.launch {
            _currentBand.emit(null)
            _bands.tryEmit(emptyList())
        }
    }

    fun selectBand(code: String) {
        Log.w("DEBUG", "Select band #$code")

        viewModelScope.launch {
            val band = getBandInfo(code)

            Log.w("DEBUG", "Selected band ${band?.name} from ${band?.homeCountry}")
            band?.let { _currentBand.emit(band) }
        }
    }

    private suspend fun getBandCodes(): List<BandCode>? {
        return withContext(Dispatchers.IO) {
            val response = bandsService.getBandNames()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                Log.i("DEBUG", "Bands count: ${response.body().orEmpty().count()}")
                response.body().orEmpty()
            } else {
                null
            }
        }
    }

    private suspend fun getBandInfo(code: String): BandInfo? {
        return withContext(Dispatchers.IO) {
            val response = bandsService.getBandInfo(code)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                response.body()
            } else {
                null
            }
        }
    }
}