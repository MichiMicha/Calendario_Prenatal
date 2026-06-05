package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun HomeScreen(viewModel: UserViewModel) {
    val name by viewModel.userName.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val week by viewModel.currentWeek.collectAsState()
    val dueDate by viewModel.dueDate.collectAsState()
    val daysRemaining by viewModel.daysRemaining.collectAsState()

    val babyInfo = viewModel.getBabySizeInfo(week)
    val bgColor = Color(0xFFF7F2EE)
    val terracotta = Color(0xFFE2725B)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(text = viewModel.todayDate, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Hola, $name ✨", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF423F3E))
        Text(text = "Tu embarazo en un vistazo", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(text = "SEMANA GESTACIONAL", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = terracotta)
                        Text(text = "$week", fontSize = 48.sp, fontWeight = FontWeight.Bold)
                        Text(text = "de 40 semanas", fontSize = 14.sp, color = Color.Gray)
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = "Peso del bebé",
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = babyInfo.weight,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { week / 40f },
                    modifier = Modifier.fillMaxWidth().height(12.dp),
                    color = terracotta,
                    trackColor = Color(0xFFEEEEEE)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = terracotta),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Tu bebé tiene el tamaño de", color = Color.White, fontSize = 14.sp)
                    Text(text = "una ${babyInfo.name}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Longitud: ${babyInfo.length}", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Text(text = "🤰", fontSize = 40.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoSmallCard(modifier = Modifier.weight(1f), title = "FECHA PARTO", value = dueDate, subValue = "$daysRemaining días rest.")
            InfoSmallCard(modifier = Modifier.weight(1f), title = "TU PESO", value = "$weight kg", subValue = "Semana $week")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "PRÓXIMA CITA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "No hay citas ingresadas",
                modifier = Modifier.padding(20.dp),
                color = Color.Gray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun InfoSmallCard(modifier: Modifier, title: String, value: String, subValue: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = subValue, fontSize = 11.sp, color = Color.Gray)
        }
    }
}