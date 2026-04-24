package com.ejemplo.futbolapp.data.repository


import com.ejemplo.futbolapp.data.remote.ApiService
import com.example.futbolapp.data.model.Equipo

class EquipoRepository(private val api: ApiService) {
    suspend fun getEquipos() = api.getEquipos()
    suspend fun createEquipo(equipo: Equipo) = api.createEquipo(equipo)
}