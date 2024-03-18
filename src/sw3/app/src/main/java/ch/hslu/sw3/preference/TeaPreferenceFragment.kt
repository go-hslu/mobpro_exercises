package ch.hslu.sw3.preference

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import ch.hslu.sw3.R

class TeaPreferenceFragment : PreferenceFragmentCompat() {

    private val counterViewModel: SharedPreferencesViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPause() {
        super.onPause()
        counterViewModel.update()
    }
}