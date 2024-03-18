package ch.hslu.sw3.preference

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager

class SharedPreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val TEA_PREFERRED_KEY = "teaPreferred"
    private val TEA_SWEETENER_KEY = "teaSweetener"
    private val TEA_WITH_SUGAR_KEY = "teaWithSugar"

    var preferredTea = "Lipton"
    var preferredTeaSweetener = "natural"
    var preferredTeaWithSugar = true

    fun reset() {
        preferredTea = "Lipton"
        preferredTeaSweetener = "natural"
        preferredTeaWithSugar = true
    }

    fun update() {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication())

        val teaPreferred = preferences.getString(TEA_PREFERRED_KEY, "Lipton")
        val teaSweetener = preferences.getString(TEA_SWEETENER_KEY, "natural")
        val teaWithSugar = preferences.getBoolean(TEA_WITH_SUGAR_KEY, true)

        if (teaPreferred != null) {
            preferredTea = teaPreferred
        }
        if (teaSweetener != null) {
            preferredTeaSweetener = teaSweetener
        }
        preferredTeaWithSugar = teaWithSugar
    }
}