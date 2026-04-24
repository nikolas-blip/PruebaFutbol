package com.ejemplo.futbolapp.ui.equipos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.futbolapp.R
import com.ejemplo.futbolapp.data.repository.EquipoRepository
import com.ejemplo.futbolapp.data.remote.RetrofitClient
import com.example.futbolapp.data.model.Equipo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquiposFragment : Fragment() {

    private lateinit var viewModel: EquiposViewModel
    private lateinit var adapter: EquipoAdapter
    private lateinit var rvEquipos: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_equipos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar Repository y ViewModel
        val repository = EquipoRepository(RetrofitClient.apiService)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EquiposViewModel(repository) as T
            }
        })[EquiposViewModel::class.java]

        rvEquipos = view.findViewById(R.id.rvEquipos)
        fabAdd = view.findViewById(R.id.fabAdd)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadEquipos()

        fabAdd.setOnClickListener {
            showAddEquipoDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = EquipoAdapter { equipo ->
            Toast.makeText(requireContext(), "Detalle de ${equipo.nombre}", Toast.LENGTH_SHORT).show()
        }
        rvEquipos.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.equipos.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Mostrar/ocultar progressBar
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddEquipoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_equipo, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
        val etCiudad = dialogView.findViewById<EditText>(R.id.etCiudad)
        val etFundacion = dialogView.findViewById<EditText>(R.id.etFundacion)

        AlertDialog.Builder(requireContext())
            .setTitle("➕ Nuevo Equipo")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val ciudad = etCiudad.text.toString()
                val fundacion = etFundacion.text.toString()
                if (nombre.isNotBlank()) {
                    val nuevoEquipo = Equipo(
                        idEquipo = 0,
                        nombre = nombre,
                        ciudad = ciudad,
                        fundacion = fundacion
                    )
                    viewModel.addEquipo(nuevoEquipo)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}