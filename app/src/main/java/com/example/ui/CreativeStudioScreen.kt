package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.api.getCreativeAdvice
import com.example.api.ki_bild_erstellen
import com.example.api.ki_tiergeschichte_erzaehlen
import com.example.api.ki_video_aus_bild_generieren
import kotlinx.coroutines.launch

@Composable
fun CreativeStudioScreen() {
    val coroutineScope = rememberCoroutineScope()
    var selectedTool by remember { mutableIntStateOf(0) } // 0: Bildstudio, 1: Video, 2: Musik, 3: Geschichten
    var prompt by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf("hund_im_park.jpg") }
    var isLoading by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }
    val clipboardManager = LocalClipboardManager.current
    var copiedToClipboard by remember { mutableStateOf(false) }

    val tools = listOf(
        Triple("KI-Bildstudio", Icons.Default.Image, "Generiere fotorealistische Tierporträts und Kunstwerke"),
        Triple("Video-Generator", Icons.Default.Videocam, "Erstelle animierte Trainings- und Agility-Videos"),
        Triple("Entspannungs-Musik", Icons.Default.MusicNote, "Beruhigende Ambient-Klänge für Vierbeiner"),
        Triple("Tiergeschichten", Icons.Default.MenuBook, "Magische Einschlaf- und Abenteuergeschichten")
    )

    val activeColor = when (selectedTool) {
        0 -> Color(0xFF2962FF)
        1 -> Color(0xFFAA00FF)
        2 -> Color(0xFF00BFA5)
        else -> Color(0xFFFF6D00)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .imePadding()
    ) {
        Text(
            text = "PETREADY KI-STUDIO",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Kreative KI-Werkzeuge",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Grid for the 4 AI Tools
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AIногдаFeatureCard(
                title = tools[0].first,
                icon = tools[0].second,
                color = Color(0xFF2962FF),
                isSelected = selectedTool == 0,
                modifier = Modifier.weight(1f),
                onClick = { selectedTool = 0; resultText = null }
            )
            AIногдаFeatureCard(
                title = tools[1].first,
                icon = tools[1].second,
                color = Color(0xFFAA00FF),
                isSelected = selectedTool == 1,
                modifier = Modifier.weight(1f),
                onClick = { selectedTool = 1; resultText = null }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AIногдаFeatureCard(
                title = tools[2].first,
                icon = tools[2].second,
                color = Color(0xFF00BFA5),
                isSelected = selectedTool == 2,
                modifier = Modifier.weight(1f),
                onClick = { selectedTool = 2; resultText = null }
            )
            AIногдаFeatureCard(
                title = tools[3].first,
                icon = tools[3].second,
                color = Color(0xFFFF6D00),
                isSelected = selectedTool == 3,
                modifier = Modifier.weight(1f),
                onClick = { selectedTool = 3; resultText = null }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active Tool Workspace
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = tools[selectedTool].first,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tools[selectedTool].third,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text(
                            when(selectedTool) {
                                0 -> "Beschreibe das Bild (z.B. Golden Retriever im Sonnenuntergang)..."
                                1 -> "Beschreibe die Videoszene (z.B. Agility Hürdensprung)..."
                                2 -> "Stimmung für Musik (z.B. Tiefenentspannung bei Gewitter)..."
                                else -> "Thema der Geschichte (z.B. Kleine Katze entdeckt den Garten)..."
                            }
                        ) 
                    },
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (prompt.isNotBlank() && !isLoading) {
                            isLoading = true
                            resultText = null
                            copiedToClipboard = false
                            coroutineScope.launch {
                                val res = when (selectedTool) {
                                    0 -> ki_bild_erstellen(prompt)
                                    1 -> ki_video_aus_bild_generieren(selectedImage, prompt)
                                    3 -> ki_tiergeschichte_erzaehlen(prompt)
                                    else -> getCreativeAdvice(prompt, tools[selectedTool].first)
                                }
                                resultText = res
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = activeColor),
                    enabled = !isLoading && prompt.isNotBlank()
                ) {
                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = when (selectedTool) {
                                    0 -> "Bild wird gemalt..."
                                    1 -> "Video wird animiert..."
                                    2 -> "Melodie wird komponiert..."
                                    else -> "Geschichte wird geschrieben..."
                                },
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generieren mit Gemini KI",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Subtile Ladeanimation und Fortschrittsbalken während der KI-Anfrage
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            AiCreativeLoadingProgress(
                toolIndex = selectedTool,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (resultText != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = activeColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "${tools[selectedTool].first} Ergebnis:",
                                        fontWeight = FontWeight.Bold,
                                        color = activeColor,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(resultText!!))
                                        copiedToClipboard = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (copiedToClipboard) Icons.Default.CheckCircle else Icons.Default.ContentCopy,
                                        contentDescription = "Kopieren",
                                        tint = if (copiedToClipboard) activeColor else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = resultText!!,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AIногдаFeatureCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, color) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
