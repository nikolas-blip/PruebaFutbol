package com.ejemplo.futbolapp.data.remote


import com.example.futbolapp.data.model.Equipo
import com.example.futbolapp.data.model.Jugador
import retrofit2.http.*

interface ApiService {

    // Equipos
    @GET("/api/equipos")
    suspend fun getEquipos(): List<Equipo>

    @POST("/api/equipos")
    suspend fun createEquipo(@Body equipo: Equipo): Equipo

    @PUT("/api/equipos/{id}")
    suspend fun updateEquipo(@Path("id") id: Long, @Body equipo: Equipo): Equipo

    @DELETE("/api/equipos/{id}")
    suspend fun deleteEquipo(@Path("id") id: Long)

    // Jugadores
    @GET("/api/jugadores")
    suspend fun getJugadores(): List<Jugador>

    @GET("/api/jugadores/equipo/{equipoId}")
    suspend fun getJugadoresByEquipo(@Path("equipoId") equipoId: Long): List<Jugador>

    @GET("/api/jugadores/goleadores")
    suspend fun getGoleadores(@Query("minGoles") minGoles: Int): List<Jugador>

    @POST("/api/jugadores")
    suspend fun createJugador(@Body jugador: Jugador): Jugador
}