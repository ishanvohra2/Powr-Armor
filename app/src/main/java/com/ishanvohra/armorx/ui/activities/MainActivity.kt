package com.ishanvohra.armorx.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ishanvohra.armorx.databinding.ActivityMainBinding
import com.ishanvohra.armorx.extensions.gone
import com.ishanvohra.armorx.extensions.show
import com.ishanvohra.armorx.models.ArmorResponse
import com.ishanvohra.armorx.ui.adapters.ArmorPiecesAdapter
import com.ishanvohra.armorx.viewModels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val armorPiecesAdapter by lazy {
        ArmorPiecesAdapter(this)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        collectArmorResponse()
        fetchAllArmorPieces()
        addEditTextListener()
    }

    private fun addEditTextListener() {
        binding.searchEditText.addTextChangedListener {
            it?.let {
                if(viewModel.armorUIState.value is  MainViewModel.ArmorUIState.SuccessState){
                    if (it.toString().length > 3) {
                        filterList(it.toString())
                    } else {
                        armorPiecesAdapter.dataSet =
                            (viewModel.armorUIState.value as MainViewModel.ArmorUIState.SuccessState).response
                    }
                }
            }
        }
    }

    private fun filterList(query: String) {
        viewModel.filterList(query)
        armorPiecesAdapter.dataSet = viewModel.filteredList.value
    }

    private fun collectArmorResponse() {
        lifecycleScope.launch{
            viewModel.armorUIState.collect{
                when(val state = it){
                    is MainViewModel.ArmorUIState.LoadingState -> { showLoadingState() }
                    is MainViewModel.ArmorUIState.ErrorState -> {}
                    is MainViewModel.ArmorUIState.SuccessState -> {
                        showSuccessState(state.response)
                    }
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.shimmerLayout.root.show()
        binding.resultsRecyclerView.gone()
    }

    private fun showSuccessState(response: ArmorResponse) {
        binding.shimmerLayout.root.gone()
        binding.resultsRecyclerView.show()
        armorPiecesAdapter.dataSet = response
    }

    private fun fetchAllArmorPieces() {
        viewModel.getArmorPieces()
    }

    private fun initRecyclerView() {
        binding.resultsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.resultsRecyclerView.adapter = armorPiecesAdapter
    }
}