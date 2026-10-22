package com.example.anesthpro.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.anesthpro.data.PatientEntity
import com.example.anesthpro.ui.theme.BluePrimary
import com.example.anesthpro.ui.theme.CardDark
import com.example.anesthpro.ui.theme.Gold
import com.example.anesthpro.ui.theme.GreenSoft
import com.example.anesthpro.ui.theme.Navy900
import com.example.anesthpro.ui.theme.TextPrimary
import com.example.anesthpro.ui.theme.TextSecondary
import com.example.anesthpro.ui.theme.White
import com.example.anesthpro.util.ClinicalUtils
import com.example.anesthpro.viewmodel.AnesthesiaViewModel
import java.util.Locale

@Composable
fun AssessmentScreen(
    patientId: Long,
    navController: NavController,
    viewModel: AnesthesiaViewModel
) {
    val patient by viewModel.selectedPatient.collectAsState()

    LaunchedEffect(patientId) {
        viewModel.loadPatient(patientId)
    }

    if (patient == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Navy900, CardDark))),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando paciente...", color = White)
        }
        return
    }

    val current = patient!!
    val bmi = ClinicalUtils.calculateBMI(current.weightKg, current.heightCm)
    val riskLabel = ClinicalUtils.anestheticRiskLabel(current.asaClass)
    val checklist = ClinicalUtils.preOpChecklist(current.asaClass)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Navy900, CardDark)))
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = White
                        )
                    }
                    Text(
                        text = "Resumen anestésico",
                        color = TextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                PatientSummaryCard(current)
            }

            item {
                EvaluationCard(
                    bmi = bmi,
                    risk = riskLabel
                )
            }

            item {
                MedicationCard(current)
            }

            item {
                ChecklistCard(checklist)
            }
        }
    }
}

@Composable
private fun PatientSummaryCard(patient: PatientEntity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = patient.name,
                color = White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Edad: ${patient.age} años", color = TextSecondary)
            Text("Peso: ${patient.weightKg} kg", color = TextSecondary)
            Text("Altura: ${patient.heightCm} cm", color = TextSecondary)
            Text("Cirugía: ${patient.surgeryType}", color = TextSecondary)
            Text("ASA: ${patient.asaClass}", color = TextSecondary)
            if (patient.allergies.isNotBlank()) {
                Text("Alergias: ${patient.allergies}", color = TextSecondary)
            }
            if (patient.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Notas: ${patient.notes}", color = TextSecondary)
            }
        }
    }
}

@Composable
private fun EvaluationCard(
    bmi: Double,
    risk: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Evaluación clínica",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "IMC: ${"%.1f".format(Locale.US, bmi)}",
                color = TextSecondary
            )
            Text(
                text = "Riesgo anestésico: $risk",
                color = if (risk.contains("bajo")) GreenSoft else Gold
            )
        }
    }
}

@Composable
private fun MedicationCard(patient: PatientEntity) {
    val propofol = ClinicalUtils.estimatePropofolDose(patient.weightKg)
    val fentanyl = ClinicalUtils.estimateFentanylDose(patient.weightKg)
    val rocuronium = ClinicalUtils.estimateRocuroniumDose(patient.weightKg)

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Dosis estimadas",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Propofol: ${"%.1f".format(Locale.US, propofol)} mg", color = TextSecondary)
            Text("Fentanilo: ${"%.2f".format(Locale.US, fentanyl)} mcg/kg", color = TextSecondary)
            Text("Rocuronio: ${"%.2f".format(Locale.US, rocuronium)} mg/kg", color = TextSecondary)
        }
    }
}

@Composable
private fun ChecklistCard(items: List<String>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Checklist preoperatoria",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(BluePrimary, shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("✓", color = White)
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(item, color = TextSecondary)
                }
            }
        }
    }
}
