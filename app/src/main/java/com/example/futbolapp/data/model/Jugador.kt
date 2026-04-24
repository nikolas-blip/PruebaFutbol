package com.example.futbolapp.data.model


data class Jugador(
    val idJugador: Long,
    val nombre: String,
    val posicion: String,
    val dorsal: Int,
    val fechaNac: String,
    val nacionalidad: String,
    val equipo: Equipo
)