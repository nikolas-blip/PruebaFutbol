package com.ejemplo.futbolapp.ui.goleadores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ejemplo.futbolapp.data.repository.JugadorRepository
import com.example.futbolapp.data.model.Jugador
import kotlinx.coroutines.launch

class GoleadoresViewModel(private val repository: JugadorRepository) : ViewModel() {

    private val _goleadores = MutableLiveData<List<Jugador>>()
    val goleadores: LiveData<List<Jugador>> = _goleadores

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadGoleadores(minGoles: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.getGoleadores(minGoles)
                _goleadores.value = list
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}