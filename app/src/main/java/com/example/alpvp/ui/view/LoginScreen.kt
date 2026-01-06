package com.example.alpvp.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(136.dp)) // Placeholder for the logo
        Text(
            text = "Selamat Datang!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Text(
            text = "Masuk untuk melanjutkan quiz",
            fontSize = 16.sp,
            color = Color(0xFF666666)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Column (
            modifier = Modifier
                .padding(horizontal = 40.dp)
        ){
            Text(
                text = "Username",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username", color = Color.Gray) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF8A2BE2),
                    unfocusedIndicatorColor = Color.LightGray,
                    cursorColor = Color(0xFF8A2BE2)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column (
            modifier = Modifier
                .padding(horizontal = 40.dp)
        ){
            Text(
                text = "Password",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Insert Password", color = Color.Gray) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Text(if (passwordVisible) "Hide" else "Show")
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedIndicatorColor = Color(0xFF8A2BE2),
                    unfocusedIndicatorColor = Color.LightGray,
                    cursorColor = Color(0xFF8A2BE2)
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { /* Handle login */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Masuk",
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF8A2BE2), Color(0xFF4169E1))
                        )
                    )
                    .padding(12.dp),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Belum punya akun? ", fontSize = 14.sp, color = Color.Gray)
            Text("Daftar sekarang", fontSize = 14.sp, color = Color(0xFF8A2BE2), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
