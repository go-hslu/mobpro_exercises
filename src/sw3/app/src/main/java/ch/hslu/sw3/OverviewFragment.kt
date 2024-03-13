package ch.hslu.sw3

import android.Manifest.permission.READ_SMS
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import ch.hslu.sw3.databinding.FragmentOverviewBinding


class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val COUNTER_KEY = "COUNTER"
    private val TEA_PREFERRED_KEY = "teaPreferred"
    private val TEA_SWEETENER_KEY = "teaSweetener"
    private val TEA_WITH_SUGAR_KEY = "teaWithSugar"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        binding.buttonEditPreferences.setOnClickListener { startTeaPreferenceActivity() }
        binding.buttonSetDefault.setOnClickListener { setDefaultPreference() }
        binding.buttonShowSMS.setOnClickListener { showSMS() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val preferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE)

        val counter = preferences.getInt(COUNTER_KEY, 0) + 1

        preferences.edit()
            .putInt(COUNTER_KEY, counter)
            .apply()

        binding.textViewCounter.text = "MainActivity onResume wurde $counter mal aufgerufen."
        setSweetenerText()
    }

    private fun startTeaPreferenceActivity() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_fragmentContainer, TeaPreferenceFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setDefaultPreference() {
        val preferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE)

        preferences.edit()
            .putString(TEA_PREFERRED_KEY, "Lipton")
            .putString(TEA_SWEETENER_KEY, "natural")
            .putBoolean(TEA_WITH_SUGAR_KEY, true)
            .apply()

        setSweetenerText()
    }

    private fun setSweetenerText() {

        val preferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE)

        val teaPreferred = preferences.getString(TEA_PREFERRED_KEY, "Lipton")
        val teaSweetener = preferences.getString(TEA_SWEETENER_KEY, "natural")
        val teaWithSugar = preferences.getBoolean(TEA_WITH_SUGAR_KEY, true)
        val sweetener = if (teaWithSugar) "mit $teaSweetener gesüsst" else "ungesüsst"

        binding.textViewPreference.text = "Ich trinke am liebsten $teaPreferred, $sweetener."
    }

    private fun showSMS() {
        val permissions = READ_SMS
        val granted = requireActivity().checkSelfPermission(permissions)

        if (granted != PackageManager.PERMISSION_GRANTED) {

            val cursor = requireActivity().contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                arrayOf(Telephony.Sms.Inbox.BODY),
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
            )


            if (cursor != null) {
                cursor.moveToFirst()
                val items = arrayOf(cursor.getString(0))

                AlertDialog.Builder(context)
                    .setTitle("SMS")
                    .setItems(items) { _, pos ->
                        Toast.makeText(activity, items[pos], LENGTH_SHORT).show()
                    }
                    .setNegativeButton(
                        "Cancel", null
                    )
                    .create()
                    .show()

                cursor.close()
            }

        } else {
            Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }
}