package ch.hslu.sw2

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import ch.hslu.sw2.databinding.FragmentMainBinding


class MainFragment : Fragment(R.layout.fragment_main), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        setupRadioLayout()
        setupRatingBar()
        setupSpinner()
        setupButtonAlert()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   private fun setupRadioLayout() {
        binding.radioGroupLayout.setOnCheckedChangeListener { _, checkedId ->
            var childFragment: Fragment = LinearLayoutFragment()

            when (checkedId) {
                R.id.radioButton_linear -> { childFragment = LinearLayoutFragment() }
                R.id.radioButton_constraint -> { childFragment = ConstraintLayoutFragment() }
            }

            childFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout_fragmentContainer, childFragment)
                .commit()
        }
    }

    private fun setupRatingBar() {
        binding.ratingBarMain.setOnRatingBarChangeListener { _, fl, _ ->
            binding.textViewRatingBarOut.text = "Deine Wahl: $fl"
        }
        binding.ratingBarMain.rating = 0.0F
    }

    private fun setupSpinner() {
        binding.spinnerMain.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val text = "Du hast ${resources.getStringArray(R.array.courses)[id.toInt()]} ausgewählt"
        Toast.makeText(context, text, LENGTH_SHORT).show()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setupButtonAlert() {
        val items = arrayOf("Alles", "Solala", "Nichts")

        binding.buttonShowAlert.setOnClickListener { _ ->
            AlertDialog.Builder(context)
                .setTitle("Was willst du?")
                .setItems(items) { _, pos ->
                    val text = "Du hast ${items[pos]} gewählt"
                    Toast.makeText(activity, text, LENGTH_SHORT).show()
                }
                .setNegativeButton(
                    "Cancel", null
                )
                .create()
                .show()
        }
    }
}