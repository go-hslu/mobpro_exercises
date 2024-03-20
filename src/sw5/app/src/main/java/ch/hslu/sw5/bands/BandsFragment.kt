package ch.hslu.sw5.bands

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ch.hslu.sw5.R

class BandsFragment : Fragment() {

    companion object {
        fun newInstance() = BandsFragment()
    }

    private val viewModel: BandsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bands, container, false)
    }
}