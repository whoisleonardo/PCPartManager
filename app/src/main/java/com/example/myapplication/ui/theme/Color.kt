package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Cores reutilizadas pelos componentes e telas.
fun corStatus(status: String): Color = when (status) {
    "Disponível" -> Color(0xFF6FA37B)
    "Instalada" -> Color(0xFF6E8FBF)
    "Manutenção" -> Color(0xFFCFA76A)
    "Reservada" -> Color(0xFFA290C4)
    else -> Color.Gray
}

fun corPrioridade(prioridade: String): Color = when (prioridade) {
    "Alta" -> Color(0xFFC5726B)
    "Média" -> Color(0xFFCFA76A)
    else -> Color(0xFF8A857D)
}
