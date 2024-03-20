package ch.hslu.sw5.bands

import kotlinx.serialization.Serializable

@Serializable
data class BandInfo(
    val name: String,
    val members: List<String>,
    val foundingYear: Int,
    val homeCountry: String,
    val bestOfCdCoverImageUrl: String?
)
