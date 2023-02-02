package com.ishanvohra.powrarmor.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishanvohra.powrarmor.models.ArmorResponseItem
import com.ishanvohra.powrarmor.repositories.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel: ViewModel() {

    val armorUIState = MutableStateFlow<ArmorUIState>(ArmorUIState.LoadingState)
    private var completeList = mutableListOf<ArmorResponseItem>()

    companion object{
        const val TAG = "MainViewModel"
    }

    fun getArmorPieces() {
        viewModelScope.launch {
            if(completeList.isNotEmpty()){
                armorUIState.emit(ArmorUIState.SuccessState(completeList))
                return@launch
            }

            armorUIState.emit(ArmorUIState.LoadingState)

            Log.d(TAG, "Fetching all armor pieces")

            MainRepository().getArmorPieces().run {
                if(this.isSuccessful && this.body() != null){
                    completeList = this.body()!!.toMutableList()
                    armorUIState.emit(ArmorUIState.SuccessState(this.body()!!))
                }
                else
                    armorUIState.emit(ArmorUIState.ErrorState)

                Log.d(TAG, "Fetching all armor pieces response code ${this.code()} ${this.message()}")
            }
        }
    }

    /**
     * Return the list of armor pieces whose name contains the param string
     * @param query
     */
    fun filterList(query: String) {
        if(armorUIState.value is ArmorUIState.LoadingState
            || armorUIState.value is ArmorUIState.ErrorState)
            return
        val state = armorUIState.value as ArmorUIState.SuccessState
        val list = mutableListOf<ArmorResponseItem>()
        val filterPattern = query.lowercase(Locale.ROOT).trim()
        for (item in state.response) {
            if (item.name.lowercase(Locale.ROOT).contains(filterPattern)) {
                list.add(item)
            }
        }
        viewModelScope.launch {
            armorUIState.emit(ArmorUIState.SuccessState(list))
        }
    }

    /**
     * Sealed class containing all the UI states
     */
    sealed class ArmorUIState{
        data class SuccessState(val response: List<ArmorResponseItem>): ArmorUIState()
        object LoadingState: ArmorUIState()
        object ErrorState: ArmorUIState()
    }

}