package com.ejemplo.futbolapp.data.model

import com.example.futbolapp.data.model.Equipo

data class Entrenador(
    val idEntrenador: Long,
    val nombre: String,
    val especialidad: String,
    val equipo: Equipo
)