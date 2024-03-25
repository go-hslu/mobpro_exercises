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

    companion object {
        private val jsonContentType = "application/json; charset=UTF8".toMediaType()
    }

    private val _bandCodesFlow: MutableStateFlow<List<BandCode>> = MutableStateFlow(emptyList())
    val bandCodesFlow: Flow<List<BandCode>> = _bandCodesFlow

    val bandCodes: List<BandCode> get() = _bandCodesFlow.value

    private val _currentBandFlow: MutableStateFlow<BandInfo?> = MutableStateFlow(null)
    val currentBandFlow: Flow<BandInfo?> = _currentBandFlow

    val currentBand: BandInfo? get() = _currentBandFlow.value

    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(
            Json.asConverterFactory(jsonContentType))
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()

    private val bandsService = retrofit.create(BandsService::class.java)

    fun loadBands() {
        Log.w("DEBUG", "Loading bands")

        viewModelScope.launch {
            val bands = getBandCodes()
            bands?.let { _bandCodesFlow.emit(bands) }
        }
    }

    fun resetBands() {
        Log.w("DEBUG", "Resetting bands")

        viewModelScope.launch {
            _currentBandFlow.emit(null)
            _bandCodesFlow.tryEmit(emptyList())
        }
    }

    fun selectBand(code: String) {
        Log.w("DEBUG", "Select band #$code")

        viewModelScope.launch {
            val band = getBandInfo(code)

            Log.w("DEBUG", "Selected band ${band?.name} from ${band?.homeCountry}")
            band?.let { _currentBandFlow.emit(band) }
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