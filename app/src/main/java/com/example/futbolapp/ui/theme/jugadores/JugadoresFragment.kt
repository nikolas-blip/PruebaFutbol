package com.ejemplo.futbolapp.ui.jugadores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.futbolapp.R
import com.ejemplo.futbolapp.data.repository.EquipoRepository
import com.ejemplo.futbolapp.data.repository.JugadorRepository
import com.ejemplo.futbolapp.data.remote.RetrofitClient
import com.example.futbolapp.data.model.Equipo
import com.example.futbolapp.data.model.Jugador
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JugadoresFragment : Fragment() {

    private lateinit var viewModel: JugadoresViewModel
    private lateinit var adapter: JugadorAdapter
    private lateinit var rvJugadores: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var equipoRepository: EquipoRepository
    private var equiposList = listOf<Equipo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_jugadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jugadorRepository = JugadorRepository(RetrofitClient.apiService)
        equipoRepository = EquipoRepository(RetrofitClient.apiService)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return JugadoresViewModel(jugadorRepository) as T
            }
        })[JugadoresViewModel::class.java]

        rvJugadores = view.findViewById(R.id.rvJugadores)
        fabAdd = view.findViewById(R.id.fabAdd)

        setupRecyclerView()
        observeViewModel()
        loadEquiposForFilter()

        viewModel.loadJugadores()

        fabAdd.setOnClickListener {
            showAddJugadorDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = JugadorAdapter { jugador ->
            Toast.makeText(requireContext(), "Detalle de ${jugador.nombre}", Toast.LENGTH_SHORT).show()
        }
        rvJugadores.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.jugadores.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Mostrar/ocultar progressBar
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadEquiposForFilter() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val equipos = equipoRepository.getEquipos()
                withContext(Dispatchers.Main) {
                    equiposList = equipos
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showAddJugadorDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_jugador, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etPosicion = dialogView.findViewById<EditText>(R.id.etPosicion)
        val etDorsal = dialogView.findViewById<EditText>(R.id.etDorsal)
        val etNacionalidad = dialogView.findViewById<EditText>(R.id.etNacionalidad)
        val spinnerEquipo = dialogView.findViewById<Spinner>(R.id.spinnerEquipo)

        // Cargar equipos en el spinner
        val equiposNombres = equiposList.map { it.nombre }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, equiposNombres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEquipo.adapter = adapter

        AlertDialog.Builder(requireContext())
            .setTitle("➕ Nuevo Jugador")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val posicion = etPosicion.text.toString()
                val dorsal = etDorsal.text.toString().toIntOrNull() ?: 0
                val nacionalidad = etNacionalidad.text.toString()
                val equipoSeleccionado = equiposList[spinnerEquipo.selectedItemPosition]

                if (nombre.isNotBlank()) {
                    val nuevoJugador = Jugador(
                        idJugador = 0,
                        nombre = nombre,
                        posicion = posicion,
                        dorsal = dorsal,
                        fechaNac = "",
                        nacionalidad = nacionalidad,
                        equipo = equipoSeleccionado
                    )
                    viewModel.addJugador(nuevoJugador)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}