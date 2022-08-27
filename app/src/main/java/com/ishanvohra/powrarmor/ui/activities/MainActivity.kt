package com.ishanvohra.powrarmor.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ishanvohra.powrarmor.R
import com.ishanvohra.powrarmor.utils.WindowSizeClass
import com.ishanvohra.powrarmor.utils.getNumberOfColumns
import com.ishanvohra.powrarmor.utils.getWindowSizeClass
import com.ishanvohra.powrarmor.databinding.ActivityMainBinding
import com.ishanvohra.powrarmor.extensions.gone
import com.ishanvohra.powrarmor.extensions.pxToDp
import com.ishanvohra.powrarmor.extensions.show
import com.ishanvohra.powrarmor.models.ArmorResponse
import com.ishanvohra.powrarmor.ui.adapters.ArmorPiecesAdapter
import com.ishanvohra.powrarmor.viewModels.MainViewModel
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

    /**
     * Observe UI State returned by view model
     */
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

    /**
     * Define layout manager for resultsRecyclerView
     * If the device is compact, then only two columns will be displayed
     * If device width is greater, then column number would be vary based on screen width
     */
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

    /**
     * Fetch armor pieces again when pulled to refresh
     */
    override fun onRefresh() {
        viewModel.getArmorPieces(refresh = true)
    }
}