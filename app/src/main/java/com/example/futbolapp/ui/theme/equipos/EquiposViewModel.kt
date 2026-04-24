package com.ejemplo.futbolapp.ui.equipos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.futbolapp.data.repository.EquipoRepository
import com.example.futbolapp.data.model.Equipo
import kotlinx.coroutines.launch

class EquiposViewModel(private val repository: EquipoRepository) : ViewModel() {

    private val _equipos = MutableLiveData<List<Equipo>>()
    val equipos: LiveData<List<Equipo>> = _equipos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadEquipos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.getEquipos()
                _equipos.value = list
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addEquipo(equipo: Equipo) {
        viewModelScope.launch {
            try {
                repository.createEquipo(equipo)
                loadEquipos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}