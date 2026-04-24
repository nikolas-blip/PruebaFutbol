package com.ejemplo.futbolapp.data.repository


import com.ejemplo.futbolapp.data.remote.ApiService
import com.example.futbolapp.data.model.Jugador

class JugadorRepository(private val api: ApiService) {
    suspend fun getJugadores() = api.getJugadores()
    suspend fun getJugadoresByEquipo(equipoId: Long) = api.getJugadoresByEquipo(equipoId)
    suspend fun getGoleadores(minGoles: Int) = api.getGoleadores(minGoles)
    suspend fun createJugador(jugador: Jugador) = api.createJugador(jugador)
}