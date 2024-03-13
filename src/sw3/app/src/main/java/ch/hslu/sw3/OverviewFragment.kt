package ch.hslu.sw3

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.hslu.sw3.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment(R.layout.fragment_overview) {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val COUNTER_KEY = "COUNTER"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
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
    }
}