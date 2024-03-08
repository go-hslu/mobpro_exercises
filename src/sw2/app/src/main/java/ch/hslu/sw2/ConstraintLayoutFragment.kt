package ch.hslu.sw2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ch.hslu.sw2.databinding.FragmentConstraintLayoutBinding

class ConstraintLayoutFragment : Fragment(R.layout.fragment_constraint_layout) {

    private var counter: Int = 0
    private val counterViewModel: CounterViewModel by activityViewModels()

    private var _binding: FragmentConstraintLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstraintLayoutBinding.inflate(inflater, container, false)

        setUpViewModelCounterButton()

        return binding.root
    }

    private fun setUpViewModelCounterButton() {

        binding.buttonIncFrag.text = "Frag ${counter}++"
        binding.buttonIncFrag.setOnClickListener {
            counter++
            binding.buttonIncFrag.text = "Frag ${counter}++"
        }

        binding.buttonIncVm.text = "VM ${counterViewModel.counter}++"
        binding.buttonIncVm.setOnClickListener {
            counterViewModel.inc()
            binding.buttonIncVm.text = "VM ${counterViewModel.counter}++"
        }
    }
}