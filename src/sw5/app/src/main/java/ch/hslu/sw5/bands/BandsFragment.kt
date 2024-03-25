package ch.hslu.sw5.bands

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ch.hslu.sw5.databinding.FragmentBandsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BandsFragment : Fragment() {

    /*companion object {
        fun newInstance() = BandsFragment()
    }*/

    private val viewModel: BandsViewModel by viewModels()

    private var _binding: FragmentBandsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w("DEBUG", "Fragment ready")

        _binding = FragmentBandsBinding.inflate(inflater, container, false)
        val view = binding.root

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bandCodesFlow.collect { bandCodes ->
                    binding.textViewBandsCount.text = "#Bands = ${if (bandCodes.isEmpty()) 0 else bandCodes.size}"

                    binding.textViewCurrentBandName.text = ""
                    binding.textViewCurrentBandInfo.text = ""
                    binding.imageViewCurrentBandImage.setImageResource(android.R.color.transparent);

                    if (bandCodes.isNotEmpty()) {
                        showBandsDialog(bandCodes)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentBandFlow.collect { bandInfo ->

                    if (bandInfo != null) {
                        binding.textViewCurrentBandName.text = bandInfo.name
                        binding.textViewCurrentBandInfo.text = "Aus ${bandInfo.homeCountry}, seit ${bandInfo.foundingYear}"
                        binding.textViewCurrentBandImage.visibility = View.VISIBLE

                        Picasso.get()
                            .load(bandInfo.bestOfCdCoverImageUrl)
                            .into(binding.imageViewCurrentBandImage)
                    }
                }
            }
        }

        binding.buttonLoadBands.setOnClickListener { _ ->
            viewModel.loadBands()
        }

        binding.buttonResetBands.setOnClickListener { _ ->
            viewModel.resetBands()
        }

        binding.buttonShowBands.setOnClickListener { _ ->
            lifecycleScope.launch {
                showBandsDialog(viewModel.bandCodes)
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBandsDialog(bandCodes: List<BandCode>) {

        val bandCodeMap = bandCodes.associateBy({ it.code }, { it.name })
        val items = bandCodes.map { bandCode -> bandCode.name }.toTypedArray()

        AlertDialog.Builder(context)
            .setTitle("Welche Band?")
            .setItems(items) { _, bandName ->
                val name = items[bandName]
                val code = bandCodeMap.filterValues { it == name }.keys.first()
                val text = "Band $name ($code) gewählt"
                viewModel.selectBand(code)
                Toast.makeText(activity, text, LENGTH_SHORT).show()
            }
            .setNegativeButton(
                "Close", null
            )
            .create()
            .show()
    }
}