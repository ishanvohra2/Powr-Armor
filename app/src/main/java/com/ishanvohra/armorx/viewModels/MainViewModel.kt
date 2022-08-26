package com.ishanvohra.armorx.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ishanvohra.armorx.models.ArmorResponse
import com.ishanvohra.armorx.repositories.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    val armorUIState = MutableStateFlow<ArmorUIState>(ArmorUIState.LoadingState)

    fun getArmorPieces() {
        viewModelScope.launch {
            armorUIState.emit(ArmorUIState.LoadingState)

            MainRepository().getArmorPieces().run {
                if(this.isSuccessful && this.body() != null){
                    armorUIState.emit(ArmorUIState.SuccessState(this.body()!!))
                }
                else
                    armorUIState.emit(ArmorUIState.ErrorState)
            }
        }
    }

    sealed class ArmorUIState{
        class SuccessState(val response: ArmorResponse): ArmorUIState()
        object LoadingState: ArmorUIState()
        object ErrorState: ArmorUIState()
    }

}