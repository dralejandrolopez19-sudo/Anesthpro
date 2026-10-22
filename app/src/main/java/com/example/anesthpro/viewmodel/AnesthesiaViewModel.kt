package com.example.anesthpro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.anesthpro.data.AppDatabase
import com.example.anesthpro.data.PatientEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnesthesiaViewModel(
    db: AppDatabase
) : ViewModel() {

    private val dao = db.patientDao()

    val patients: Flow<List<PatientEntity>> = dao.getAll()

    private val _selectedPatient = MutableStateFlow<PatientEntity?>(null)
    val selectedPatient: StateFlow<PatientEntity?> = _selectedPatient.asStateFlow()

    fun savePatient(
        name: String,
        age: Int,
        weightKg: Double,
        heightCm: Double,
        surgeryType: String,
        asaClass: String,
        allergies: String,
        notes: String
    ) {
        viewModelScope.launch {
            val patient = PatientEntity(
                name = name.trim(),
                age = age,
                weightKg = weightKg,
                heightCm = heightCm,
                surgeryType = surgeryType.trim(),
                asaClass = asaClass.trim(),
                allergies = allergies.trim(),
                notes = notes.trim()
            )
            dao.insert(patient)
        }
    }

    fun loadPatient(id: Long) {
        viewModelScope.launch {
            _selectedPatient.value = dao.getById(id)
        }
    }
}

class AnesthesiaViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnesthesiaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AnesthesiaViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
