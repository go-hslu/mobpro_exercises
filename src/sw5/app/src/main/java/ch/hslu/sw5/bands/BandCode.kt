package ch.hslu.sw5.bands

import kotlinx.serialization.Serializable

@Serializable
data class BandCode(
    val name: String,
    val code: String
)
