package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.api.getCareAdvice
import kotlinx.coroutines.launch

@Composable
fun CareAdvisorScreen() {
    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hallo! Ich bin dein Gemini Pflege-Berater. Hast du Fragen zur Fellpflege, Ernährung oder der allgemeinen Gesundheit deines Haustiers?", false)
    )) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = "PFLEGE & GESUNDHEIT",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Dein KI-Berater",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Banner Image
        Image(
            painter = painterResource(id = R.drawable.care_banner),
            contentDescription = "Pflege Beispiel",
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Chat List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = true
        ) {
            if (isLoading) {
                item {
                    LoadingBubble()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            items(messages.reversed()) { msg ->
                ChatBubble(msg)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Input Area
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Stelle eine Pflege-Frage...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = {
                    if (input.isNotBlank() && !isLoading) {
                        val userMsg = input
                        input = ""
                        messages = messages + ChatMessage(userMsg, true)
                        isLoading = true
                        coroutineScope.launch {
                            val response = getCareAdvice(userMsg)
                            messages = messages + ChatMessage(response, false)
                            isLoading = false
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Senden")
            }
        }
    }
}
