package com.ejemplo.futbolapp.data.model

import com.example.futbolapp.data.model.Equipo

data class Partido(
    val idPartido: Long,
    val fecha: String,
    val estadio: String,
    val equipoLocal: Equipo,
    val equipoVisita: Equipo,
    val golesLocal: Int,
    val golesVisita: Int
)