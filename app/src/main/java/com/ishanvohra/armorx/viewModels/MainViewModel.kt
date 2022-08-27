package com.ishanvohra.armorx.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishanvohra.armorx.models.ArmorResponse
import com.ishanvohra.armorx.models.ArmorResponseItem
import com.ishanvohra.armorx.repositories.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel: ViewModel() {

    val armorUIState = MutableStateFlow<ArmorUIState>(ArmorUIState.LoadingState)
    val filteredList = MutableStateFlow(mutableListOf<ArmorResponseItem>())

    companion object{
        const val TAG = "MainViewModel"
    }

    fun getArmorPieces() {
        viewModelScope.launch {
            if(armorUIState.value is ArmorUIState.SuccessState)
                return@launch

            armorUIState.emit(ArmorUIState.LoadingState)

            Log.d(TAG, "Fetching all armor pieces")

            MainRepository().getArmorPieces().run {
                if(this.isSuccessful && this.body() != null){
                    armorUIState.emit(ArmorUIState.SuccessState(this.body()!!))
                }
                else
                    armorUIState.emit(ArmorUIState.ErrorState)

                Log.d(TAG, "Fetching all armor pieces response code ${this.code()} ${this.message()}")
            }
        }
    }

    fun filterList(query: String) {
        val state = armorUIState.value as ArmorUIState.SuccessState
        val list = mutableListOf<ArmorResponseItem>()
        val filterPattern = query.lowercase(Locale.ROOT).trim()
        for (item in state.response) {
            if (item.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                list.add(item)
            }
        }
        filteredList.value = list
    }

    sealed class ArmorUIState{
        class SuccessState(val response: ArmorResponse): ArmorUIState()
        object LoadingState: ArmorUIState()
        object ErrorState: ArmorUIState()
    }

}