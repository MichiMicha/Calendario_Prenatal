package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.UserViewModel

@Composable
fun WelcomeScreen(
    viewModel: UserViewModel,
    onNavigateToHome: () -> Unit
) {
    val name by viewModel.userName.collectAsState()
    val isButtonEnabled = name.isNotBlank()
    val terracottaColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Surface(
                shape = CircleShape,
                shadowElevation = 8.dp,
                color = Color.White,
                modifier = Modifier.size(80.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Corazón",
                    tint = terracottaColor,
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Bienvenida",
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Text(
                    text = "Tu viaje personal comienza aquí",
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray
                )
            }
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.onNameChange(it) },
                placeholder = { Text("¿Cómo te llamas?") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = terracottaColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onNavigateToHome()
                },
                enabled = isButtonEnabled,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = terracottaColor,
                    disabledContainerColor = Color.LightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }


    }
}
}