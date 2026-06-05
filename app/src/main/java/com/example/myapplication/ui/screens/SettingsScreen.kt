package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: UserViewModel) {
    val currentWeight by viewModel.weight.collectAsState()
    val formattedDate = viewModel.formattedPregnancyDate
    var showDatePicker by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }

    val bgColor = Color(0xFFF7F2EE)
    val terracotta = Color(0xFFE2725B)
    val textDark = Color(0xFF423F3E)
    val cardBg = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = "CONFIGURACIÓN", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
        Text(
            text = "Preferencias",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = textDark,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Configuración de Embarazo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = terracotta, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingItem(
                icon = Icons.Default.DateRange,
                title = "Fecha de último período",
                subtitle = if (formattedDate.isNotEmpty()) formattedDate else "Seleccionar fecha",
                onClick = { showDatePicker = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Información", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = terracotta, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingItem(
                icon = Icons.Default.Edit,
                title = "Peso",
                subtitle = if (currentWeight.isNotEmpty()) "$currentWeight kg" else "Registrar peso actual",
                onClick = { showWeightDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val adjustedMillis = millis + java.util.TimeZone.getDefault().getOffset(millis)
                        viewModel.onDateSelected(adjustedMillis)
                        viewModel.saveAllData {}
                    }
                    showDatePicker = false
                }) { Text("Guardar", color = terracotta, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar", color = Color.Gray) }
            },
            colors = DatePickerDefaults.colors(containerColor = cardBg)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = terracotta,
                    todayDateBorderColor = terracotta,
                    todayContentColor = terracotta
                )
            )
        }
    }

    if (showWeightDialog) {
        var tempWeight by remember { mutableStateOf(currentWeight) }
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            containerColor = cardBg,
            title = { Text("Registra tu peso actual", fontWeight = FontWeight.Bold, color = textDark, fontSize = 18.sp) },
            text = {
                OutlinedTextField(
                    value = tempWeight,
                    onValueChange = { tempWeight = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("Ej. 65", color = Color.Gray) },
                    suffix = { Text("kg", color = textDark) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = terracotta,
                        unfocusedBorderColor = Color(0xFFE0D8D0),
                        focusedContainerColor = bgColor,
                        unfocusedContainerColor = bgColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onWeightChange(tempWeight)
                        viewModel.saveAllData {}
                        showWeightDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = terracotta),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Guardar peso", fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) {
                    Text("Cancelar", color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(Color(0xFFF7F2EE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFE2725B), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontSize = 16.sp, color = Color(0xFF423F3E), fontWeight = FontWeight.Medium)
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}