package com.ejemplo.futbolapp.ui.jugadores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.ejemplo.futbolapp.data.repository.JugadorRepository
import com.example.futbolapp.data.model.Jugador
import kotlinx.coroutines.launch

class JugadoresViewModel(private val repository: JugadorRepository) : ViewModel() {

    private val _jugadores = MutableLiveData<List<Jugador>>()
    val jugadores: LiveData<List<Jugador>> = _jugadores

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadJugadores() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.getJugadores()
                _jugadores.value = list
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadJugadoresByEquipo(equipoId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.getJugadoresByEquipo(equipoId)
                _jugadores.value = list
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addJugador(jugador: Jugador) {
        viewModelScope.launch {
            try {
                repository.createJugador(jugador)
                loadJugadores()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}