package com.ejemplo.futbolapp.ui.equipos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.futbolapp.R

import com.example.futbolapp.data.model.Equipo

class EquipoAdapter(
    private val onVerMasClick: (Equipo) -> Unit
) : RecyclerView.Adapter<EquipoAdapter.ViewHolder>() {

    private var items = listOf<Equipo>()

    fun submitList(list: List<Equipo>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], onVerMasClick)
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvCiudad: TextView = itemView.findViewById(R.id.tvCiudad)
        private val tvFundacion: TextView = itemView.findViewById(R.id.tvFundacion)
        private val btnVerMas: Button = itemView.findViewById(R.id.btnVerMas)

        fun bind(equipo: Equipo, onVerMasClick: (Equipo) -> Unit) {
            tvNombre.text = equipo.nombre
            tvCiudad.text = equipo.ciudad
            tvFundacion.text = "Fundado: ${equipo.fundacion}"
            btnVerMas.setOnClickListener { onVerMasClick(equipo) }
        }
    }
}