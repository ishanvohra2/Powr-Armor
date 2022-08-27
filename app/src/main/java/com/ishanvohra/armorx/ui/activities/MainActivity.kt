package com.ishanvohra.armorx.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ishanvohra.armorx.R
import com.ishanvohra.armorx.Utils.WindowSizeClass
import com.ishanvohra.armorx.Utils.getNumberOfColumns
import com.ishanvohra.armorx.Utils.getWindowSizeClass
import com.ishanvohra.armorx.databinding.ActivityMainBinding
import com.ishanvohra.armorx.extensions.gone
import com.ishanvohra.armorx.extensions.pxToDp
import com.ishanvohra.armorx.extensions.show
import com.ishanvohra.armorx.models.ArmorResponse
import com.ishanvohra.armorx.ui.adapters.ArmorPiecesAdapter
import com.ishanvohra.armorx.viewModels.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

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
        binding.swipeRefreshLayout.setOnRefreshListener(this)
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
                binding.swipeRefreshLayout.isRefreshing = false
                when(val state = it){
                    is MainViewModel.ArmorUIState.LoadingState -> { showLoadingState() }
                    is MainViewModel.ArmorUIState.ErrorState -> { showErrorState() }
                    is MainViewModel.ArmorUIState.SuccessState -> {
                        showSuccessState(state.response)
                    }
                }
            }
        }
    }

    private fun showErrorState() {
        binding.shimmerLayout.root.gone()
        binding.resultsRecyclerView.gone()
        binding.errorLayout.root.show()
    }

    private fun showLoadingState() {
        binding.shimmerLayout.root.show()
        binding.resultsRecyclerView.gone()
        binding.errorLayout.root.gone()
    }

    private fun showSuccessState(response: ArmorResponse) {
        binding.shimmerLayout.root.gone()
        binding.errorLayout.root.gone()
        binding.resultsRecyclerView.show()
        armorPiecesAdapter.dataSet = response
    }

    private fun fetchAllArmorPieces() {
        viewModel.getArmorPieces()
    }

    private fun initRecyclerView() {
        binding.resultsRecyclerView.layoutManager = getLayoutManager()
        binding.resultsRecyclerView.adapter = armorPiecesAdapter
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        return when (getWindowSizeClass(this)) {
            WindowSizeClass.COMPACT -> {
                GridLayoutManager(this, 2)
            }
            WindowSizeClass.MEDIUM, WindowSizeClass.EXPANDED -> {
                GridLayoutManager(
                    this,
                    getNumberOfColumns(
                        resources.getDimension(R.dimen.card_max_width).toInt().pxToDp(this),
                        resources.getDimension(R.dimen.card_spacing).toInt().pxToDp(this),
                        this
                    )
                )
            }
        }
    }

    override fun onRefresh() {
        viewModel.getArmorPieces(refresh = true)
    }
}