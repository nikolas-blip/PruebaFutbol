package com.ejemplo.futbolapp.data.model

import com.example.futbolapp.data.model.Jugador

data class EstadisticaJugador(
    val idEstadistica: Long,
    val jugador: Jugador,
    val partido: Partido,
    val minutosJugados: Int,
    val goles: Int,
    val asistencias: Int,
    val tarjetasAmarillas: Int,
    val tarjetasRojas: Int
)