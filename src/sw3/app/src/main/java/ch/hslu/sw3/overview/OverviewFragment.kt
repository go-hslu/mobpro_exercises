package ch.hslu.sw3.overview

import android.Manifest.permission.READ_SMS
import android.app.AlertDialog
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import ch.hslu.sw3.R
import ch.hslu.sw3.databinding.FragmentOverviewBinding
import ch.hslu.sw3.preference.SharedPreferencesViewModel
import ch.hslu.sw3.preference.TeaPreferenceFragment


class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val prefViewModel: SharedPreferencesViewModel by activityViewModels()

    private val COUNTER_KEY = "COUNTER"

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
        /*val preferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            MODE_PRIVATE)*/

        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
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
        prefViewModel.reset()
        setSweetenerText()
    }

    private fun setSweetenerText() {
        val sweetener = if (prefViewModel.preferredTeaWithSugar) "mit ${prefViewModel.preferredTeaSweetener} gesüsst" else "ungesüsst"
        binding.textViewPreference.text = "Ich trinke am liebsten ${prefViewModel.preferredTea}, $sweetener."
    }

    private fun showSMS() {
        val permissions = READ_SMS
        val granted = requireActivity().checkSelfPermission(permissions)

        if (granted == PackageManager.PERMISSION_GRANTED) {
            alertSMS()
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            object : ActivityResultCallback<Boolean> {
                override fun onActivityResult(result: Boolean) {
                    if (!result) {
                        Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
                        return
                    } else {
                        alertSMS()
                    }
                }
            }
        )

    private fun alertSMS() {
        val cursor = requireActivity().contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            null,
            null,
            null,
            null
        )


        if (cursor != null) {
            val items = ArrayList<String>()

            cursor.moveToFirst()

            do {
                val content = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                items.add(content)
            } while (cursor.moveToNext() && items.count() < 5)

            val smss: Array<String> = items.toTypedArray()

            AlertDialog.Builder(context)
                .setTitle("SMS")
                .setItems(smss) { _, pos ->
                    Toast.makeText(activity, items[pos], LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    "Close", null
                )
                .create()
                .show()

            cursor.close()
        }
    }
}