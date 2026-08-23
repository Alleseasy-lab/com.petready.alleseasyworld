package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Eco
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
import com.example.api.getNutritionAdvice
import kotlinx.coroutines.launch

@Composable
fun FeedingScreen() {
    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hallo! Ich bin dein Futter-Experte. Frag mich alles zu Portionsgrößen, Unverträglichkeiten oder Diätplänen!", false)
    )) }
    var isLoading by remember { mutableStateOf(false) }

    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = "ERNÄHRUNG & DIÄT",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Fütterung & Leckereien",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Banner Image
        Image(
            painter = painterResource(id = R.drawable.feeding_banner),
            contentDescription = "Fütterung Beispiel",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        androidx.compose.animation.AnimatedVisibility(
            visible = visible.value,
            enter = androidx.compose.animation.slideInVertically(initialOffsetY = { -50 }) + androidx.compose.animation.fadeIn()
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Täglicher Ernährungs-Tipp", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teile die tägliche Futterration bei erwachsenen Tieren am besten auf 2 Portionen auf, um den Magen zu schonen.")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "GESUNDE LECKEREIEN",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf(
                "Karotten-Snacks" to "Vitaminreich & knackig",
                "Hüttenkäse" to "Gut für die Verdauung",
                "Apfelstücke" to "Ohne Kerngehäuse",
                "Thunfisch-Plätzchen" to "Der Katzen-Favorit"
            )) { (title, subtitle) ->
                TreatCard(title, subtitle)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "GEMINI FRAGE-BOX",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(12.dp)
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
                    placeholder = { Text("Frag Gemini nach Rezepten...") },
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
                                val responseRaw = getNutritionAdvice(userMsg)
                                val finalResponse = if (responseRaw == "429_ERROR") {
                                    "Aktuell sind etwas viele Anfragen da (Limit erreicht). Bitte warte einen Moment, bevor du die nächste Futter-Frage stellst!"
                                } else {
                                    responseRaw
                                }
                                messages = messages + ChatMessage(finalResponse, false)
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

@Composable
fun TreatCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
