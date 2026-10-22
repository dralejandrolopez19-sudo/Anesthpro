package com.example.anesthpro.util

object ClinicalUtils {
    fun calculateBMI(weightKg: Double, heightCm: Double): Double {
        val heightM = heightCm / 100.0
        return if (heightM <= 0.0) 0.0 else weightKg / (heightM * heightM)
    }

    fun bmiLabel(bmi: Double): String = when {
        bmi < 18.5 -> "Bajo peso"
        bmi < 25 -> "Normal"
        bmi < 30 -> "Sobrepeso"
        else -> "Obesidad"
    }

    fun estimatePropofolDose(weightKg: Double): Double = weightKg * 2.0
    fun estimateFentanylDose(weightKg: Double): Double = weightKg * 0.5 / 10.0
    fun estimateRocuroniumDose(weightKg: Double): Double = weightKg * 0.6 / 10.0

    fun anestheticRiskLabel(asa: String): String = when (asa) {
        "ASA I" -> "Riesgo bajo"
        "ASA II" -> "Riesgo moderado"
        "ASA III" -> "Riesgo alto"
        "ASA IV" -> "Riesgo crítico"
        else -> "Sin clasificación"
    }

    fun preOpChecklist(asa: String): List<String> = when (asa) {
        "ASA I" -> listOf(
            "Ayuno verificado",
            "Consentimiento informado",
            "Signos vitales registrados",
            "Alergias revisadas"
        )
        "ASA II" -> listOf(
            "Ayuno verificado",
            "Historia médica revisada",
            "Medicación actual documentada",
            "Laboratorios o ECG revisados"
        )
        "ASA III" -> listOf(
            "Riesgo reforzado evaluado",
            "Monitoreo avanzado preparado",
            "Coordinación con especialista",
            "Plan de rescate disponible"
        )
        else -> listOf(
            "Evaluación compleja requerida",
            "Monitoreo avanzado solicitado",
            "Equipo de rescate preparado",
            "Plan anestésico revisado por especialista"
        )
    }
}
