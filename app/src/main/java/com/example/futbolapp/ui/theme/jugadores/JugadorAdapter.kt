package com.ejemplo.futbolapp.ui.jugadores

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.futbolapp.R

import com.example.futbolapp.data.model.Jugador

class JugadorAdapter(
    private val onItemClick: (Jugador) -> Unit
) : RecyclerView.Adapter<JugadorAdapter.ViewHolder>() {

    private var items = listOf<Jugador>()

    fun submitList(list: List<Jugador>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jugador, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvPosicion: TextView = itemView.findViewById(R.id.tvPosicion)
        private val tvEquipo: TextView = itemView.findViewById(R.id.tvEquipo)
        private val tvGoles: TextView = itemView.findViewById(R.id.tvGoles)

        fun bind(jugador: Jugador, onItemClick: (Jugador) -> Unit) {
            tvNombre.text = jugador.nombre
            tvPosicion.text = "${jugador.posicion} | Dorsal ${jugador.dorsal}"
            tvEquipo.text = jugador.equipo.nombre
            tvGoles.text = "0 goles" // Los goles se cargarán aparte
            itemView.setOnClickListener { onItemClick(jugador) }
        }
    }
}