package com.ejemplo.futbolapp.ui.goleadores

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
import com.ejemplo.futbolapp.data.repository.JugadorRepository
import com.ejemplo.futbolapp.data.remote.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class GoleadoresFragment : Fragment() {

    private lateinit var viewModel: GoleadoresViewModel
    private lateinit var adapter: GoleadorAdapter
    private lateinit var rvGoleadores: RecyclerView
    private lateinit var chipMinGoles: Chip
    private lateinit var btnCambiarFiltro: MaterialButton
    private var currentMinGoles = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_goleadores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = JugadorRepository(RetrofitClient.apiService)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GoleadoresViewModel(repository) as T
            }
        })[GoleadoresViewModel::class.java]

        rvGoleadores = view.findViewById(R.id.rvGoleadores)
        chipMinGoles = view.findViewById(R.id.chipMinGoles)
        btnCambiarFiltro = view.findViewById(R.id.btnCambiarFiltro)

        setupRecyclerView()
        observeViewModel()

        viewModel.loadGoleadores(currentMinGoles)

        btnCambiarFiltro.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun setupRecyclerView() {
        adapter = GoleadorAdapter()
        rvGoleadores.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.goleadores.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Mostrar/ocultar progressBar
        }
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFilterDialog() {
        val input = EditText(requireContext())
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        input.setText(currentMinGoles.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Filtrar Goleadores")
            .setMessage("Mostrar jugadores con más de X goles")
            .setView(input)
            .setPositiveButton("Aceptar") { _, _ ->
                val minGoles = input.text.toString().toIntOrNull() ?: 0
                currentMinGoles = minGoles
                chipMinGoles.text = "Mínimo goles: $minGoles"
                viewModel.loadGoleadores(minGoles)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}