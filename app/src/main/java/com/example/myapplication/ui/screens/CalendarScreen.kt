package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.UserViewModel
import com.example.myapplication.viewmodel.DiaryEntry
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarScreen(viewModel: UserViewModel, onAddEntryClick: () -> Unit) {
    val displayedMonth by viewModel.displayedMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val diaryEntries by viewModel.diaryEntries.collectAsState()

    // Estados locales para el formulario
    var showForm by remember { mutableStateOf(false) }
    var entryType by remember { mutableStateOf("DIARIO") } // "DIARIO" o "MEDICA"
    var notaInput by remember { mutableStateOf("") }
    var sintomaSeleccionado by remember { mutableStateOf("") }

    val bgColor = Color(0xFFF7F2EE)
    val terracotta = Color(0xFFE2725B)
    val textDark = Color(0xFF423F3E)
    val cardBg = Color.White
    val chipBgColor = Color(0xFFF7F2EE)

    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))
    val headerDateStr = monthYearFormat.format(displayedMonth.time).replaceFirstChar { it.uppercase() }

    val exactDateFormat = SimpleDateFormat("d 'DE' MMMM", Locale("es", "ES"))
    val selectedDateStr = exactDateFormat.format(selectedDate.time).uppercase()

    val selectedDateKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selectedDate.time)
    val currentEntry = diaryEntries[selectedDateKey]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Títulos principales
        Text(text = "BIENESTAR", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        Text(text = "Diario & Agenda", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textDark, fontFamily = FontFamily.Serif)

        Spacer(modifier = Modifier.height(24.dp))

        // --- CALENDARIO ---
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.changeMonth(-1) },
                        modifier = Modifier.background(Color(0xFFF0EAE4), CircleShape).size(40.dp)
                    ) { Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Anterior", tint = textDark) }

                    Text(text = headerDateStr, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textDark)

                    IconButton(
                        onClick = { viewModel.changeMonth(1) },
                        modifier = Modifier.background(Color(0xFFF0EAE4), CircleShape).size(40.dp)
                    ) { Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Siguiente", tint = textDark) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val daysOfWeek = listOf("LU", "MA", "MI", "JU", "VI", "SÁ", "DO")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    daysOfWeek.forEach { day ->
                        Text(text = day, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Gray.copy(alpha = 0.7f), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val daysInMonth = getDaysForMonth(displayedMonth)
                val today = Calendar.getInstance()

                Column {
                    for (week in daysInMonth.chunked(7)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            for (day in week) {
                                Box(modifier = Modifier.weight(1f).aspectRatio(1f), contentAlignment = Alignment.Center) {
                                    if (day != null) {
                                        val isSelected = (day == selectedDate.get(Calendar.DAY_OF_MONTH) &&
                                                displayedMonth.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                                                displayedMonth.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR))

                                        val isToday = (day == today.get(Calendar.DAY_OF_MONTH) &&
                                                displayedMonth.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                                                displayedMonth.get(Calendar.YEAR) == today.get(Calendar.YEAR))

                                        val cellCal = displayedMonth.clone() as Calendar
                                        cellCal.set(Calendar.DAY_OF_MONTH, day)
                                        val cellDateKey = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cellCal.time)
                                        val savedEntry = diaryEntries[cellDateKey]

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(
                                                    when {
                                                        isSelected -> terracotta
                                                        isToday -> Color(0xFFF0EAE4)
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .clickable {
                                                    viewModel.selectDate(day)
                                                    showForm = false
                                                }
                                        ) {
                                            Text(
                                                text = day.toString(),
                                                color = if (isSelected) Color.White else textDark,
                                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 15.sp
                                            )

                                            if (savedEntry != null) {
                                                val dotColor = if (isSelected) {
                                                    Color.White
                                                } else {
                                                    if (savedEntry.type == "MEDICA") terracotta else Color(0xFF6A996D)
                                                }
                                                Box(modifier = Modifier.padding(top = 2.dp).size(5.dp).background(dotColor, CircleShape))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color(0xFFF0EAE4), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LegendItem(color = Color(0xFF6A996D), text = "Diario")
                    Spacer(modifier = Modifier.width(16.dp))
                    LegendItem(color = terracotta, text = "Cita médica")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- SECCIÓN DE AGENDA ABAJO ---
        Text(
            text = selectedDateStr,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = terracotta,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (currentEntry != null && !showForm) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (currentEntry.type == "MEDICA") "Cita Médica Registrada" else "Diario Guardado",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textDark,
                            fontFamily = FontFamily.Serif
                        )
                        IconButton(onClick = { viewModel.deleteDiaryEntry(selectedDateKey) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.6f))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentEntry.type == "DIARIO" && currentEntry.mood.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF0EAE4))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = currentEntry.mood, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = if (currentEntry.notes.isNotEmpty()) currentEntry.notes else "Sin detalles guardados.",
                        fontSize = 14.sp,
                        color = textDark,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFFF0EAE4), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            showForm = true
                            notaInput = ""
                            sintomaSeleccionado = ""
                            entryType = "DIARIO"
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = terracotta)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Agregar otra entrada", fontWeight = FontWeight.Bold)
                    }
                }
            }
            else if (currentEntry == null && !showForm) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(color = Color(0xFFF0EAE4), shape = CircleShape, modifier = Modifier.size(56.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = terracotta, modifier = Modifier.size(28.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Sin registros este día", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = textDark)
                    Text(text = "Registra tus síntomas o citas médicas", fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showForm = true
                            notaInput = ""
                            sintomaSeleccionado = ""
                            entryType = "DIARIO"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = terracotta),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                    ) {
                        Text(text = "Agregar entrada", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            else {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Nueva entrada",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textDark,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "TIPO DE ENTRADA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val isDiario = entryType == "DIARIO"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isDiario) Color(0xFF6A996D) else Color(0xFFF0EAE4))
                                .clickable { entryType = "DIARIO" }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📖 Diario", color = if (isDiario) Color.White else textDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        val isMedica = entryType == "MEDICA"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isMedica) terracotta else Color(0xFFF0EAE4))
                                .clickable { entryType = "MEDICA" }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🩺 Cita Médica", color = if (isMedica) Color.White else textDark, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (entryType == "DIARIO") {
                        Text(text = "ESTADO DE ÁNIMO / SÍNTOMAS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))

                        val sintomas = listOf("😊 Bien", "🤢 Náuseas", "😔 Cansada", "😴 Sueño")
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            sintomas.forEach { sintoma ->
                                val esSeleccionado = sintomaSeleccionado == sintoma
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (esSeleccionado) Color(0xFF6A996D) else Color(0xFFF0EAE4))
                                        .clickable { sintomaSeleccionado = if (esSeleccionado) "" else sintoma }
                                        .padding(horizontal = 12.dp, vertical = 8.dp)
                                ) {
                                    Text(text = sintoma, fontSize = 12.sp, color = if (esSeleccionado) Color.White else textDark)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Text(
                        text = if (entryType == "MEDICA") "DETALLES DE LA CITA" else "NOTAS DEL DIARIO",
                        fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray, letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = notaInput,
                        onValueChange = { notaInput = it },
                        placeholder = {
                            Text(
                                text = if (entryType == "MEDICA") "Escribe los detalles médicos, doctor, indicaciones o peso..." else "¿Cómo te sientes hoy? Escribe tus notas aquí...",
                                color = Color.Gray, fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = chipBgColor,
                            unfocusedContainerColor = chipBgColor,
                            focusedBorderColor = if (entryType == "MEDICA") terracotta else Color(0xFF6A996D),
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { showForm = false },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text(text = "Cancelar", fontWeight = FontWeight.Bold, color = Color.Gray) }

                        Button(
                            onClick = {
                                // Ahora llama a la función nativa limpia de 4 parámetros
                                viewModel.saveDiaryEntry(
                                    dateKey = selectedDateKey,
                                    mood = if(entryType == "DIARIO") sintomaSeleccionado else "",
                                    notes = notaInput,
                                    type = entryType
                                )
                                showForm = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = if (entryType == "MEDICA") terracotta else Color(0xFF6A996D)),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text(text = "Guardar", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(7.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, fontSize = 12.sp, color = Color.Gray)
    }
}

fun getDaysForMonth(month: Calendar): List<Int?> {
    val days = mutableListOf<Int?>()
    val calendar = month.clone() as Calendar
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    var firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    if (firstDayOfWeek == 0) firstDayOfWeek = 7
    for (i in 1 until firstDayOfWeek) { days.add(null) }
    val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..maxDays) { days.add(i) }
    while (days.size % 7 != 0) { days.add(null) }
    return days
}