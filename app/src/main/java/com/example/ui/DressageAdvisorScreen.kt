package com.example.ui

import androidx.compose.foundation.Image
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
import com.example.api.getDressageAdvice
import kotlinx.coroutines.launch

@Composable
fun DressageAdvisorScreen() {
    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hallo! Ich bin dein Dressur- und Erziehungsberater für Pferde. Wie kann ich dir und deinem Pferd heute helfen?", false)
    )) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = "DRESSUR & ERZIEHUNG",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
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
            painter = painterResource(id = R.drawable.dressage_banner),
            contentDescription = "Dressur Beispiel",
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
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
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Frage zum Training stellen...") },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (input.isNotBlank() && !isLoading) {
                            val userMsg = input
                            input = ""
                            messages = messages + ChatMessage(userMsg, true)
                            isLoading = true
                            coroutineScope.launch {
                                val response = getDressageAdvice(userMsg)
                                messages = messages + ChatMessage(response, false)
                                isLoading = false
                            }
                        }
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Senden", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
