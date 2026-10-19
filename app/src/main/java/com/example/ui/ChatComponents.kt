package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun ChatBubble(msg: ChatMessage) {
    val alignment = if (msg.isUser) Alignment.CenterEnd else Alignment.CenterStart
    val bgColor = if (msg.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (msg.isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (msg.isUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    var displayedText by remember(msg.text) { 
        mutableStateOf(if (msg.isUser) msg.text else "") 
    }

    LaunchedEffect(msg.text) {
        if (!msg.isUser) {
            for (i in 1..msg.text.length) {
                displayedText = msg.text.substring(0, i)
                delay(12)
            }
        } else {
            displayedText = msg.text
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .clip(shape)
                .background(bgColor)
                .padding(16.dp)
        ) {
            Text(
                text = displayedText,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun LoadingBubble() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "translate"
    )

    val dot1Scale by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Scale by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Scale by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(130.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(x = translateAnimation - 100f, y = 0f),
                        end = Offset(x = translateAnimation + 100f, y = 100f)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot1Scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot2Scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(dot3Scale)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}

/**
 * Subtile und hochmoderne Ladeanimation mit Fortschrittsbalken & interaktiven Zwischenschritten
 * für KI-Bilder, Tiergeschichten, Videos und Musik.
 */
@Composable
fun AiCreativeLoadingProgress(
    toolIndex: Int,
    modifier: Modifier = Modifier
) {
    val steps = remember(toolIndex) {
        when (toolIndex) {
            0 -> listOf(
                "🎨 Analysiere Bildidee & Komposition...",
                "✨ Berechne Felltexturen, Licht & Schatten...",
                "🖌️ Imagen-Engine vollendet das Tierporträt...",
                "🌟 Letzte Farboptimierung & Bildfinalisierung..."
            )
            3 -> listOf(
                "📖 Erwecke Tier-Charaktere zum Leben...",
                "🐾 Erschaffe eine herzerwärmende Spielszene...",
                "✨ Verfasse liebevolle & spannende Details...",
                "🌟 Setze das wunderbare Geschichten-Ende..."
            )
            1 -> listOf(
                "🎬 Analysiere Bildattribute & Bewegungsszenen...",
                "⚡ Berechne Keyframes & Animationsphasen...",
                "🎞️ Erzeuge flüssiges Video-Storyboard...",
                "🌟 Video-Rendering wird abgeschlossen..."
            )
            else -> listOf(
                "🎵 Wähle harmonische Wohlfühlfrequenzen...",
                "🌿 Komponiere beruhigende Natur- & Ambientklänge...",
                "🐾 Stimme Akustik für entspannte Haustiere ab...",
                "🌟 Sound-Arrangement wird finalisiert..."
            )
        }
    }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0.12f) }

    LaunchedEffect(toolIndex) {
        currentStepIndex = 0
        progress = 0.15f
        // Stufenweiser Fortschritt bis 95%, um sofortiges Feedback zu vermitteln
        val delays = listOf(800L, 1200L, 1400L, 1600L)
        val progressTargets = listOf(0.38f, 0.65f, 0.86f, 0.94f)
        for (i in delays.indices) {
            delay(delays[i])
            if (i + 1 < steps.size) {
                currentStepIndex = i + 1
            }
            progress = progressTargets[i]
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "animatedProgress"
    )

    // Sanfte Puls-Animation für das Icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val toolIcon = when (toolIndex) {
        0 -> Icons.Default.Image
        1 -> Icons.Default.Videocam
        2 -> Icons.Default.MusicNote
        else -> Icons.Default.MenuBook
    }

    val toolColor = when (toolIndex) {
        0 -> Color(0xFF2962FF)
        1 -> Color(0xFFAA00FF)
        2 -> Color(0xFF00BFA5)
        else -> Color(0xFFFF6D00)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = toolColor.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .scale(pulseScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(toolColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = toolIcon,
                            contentDescription = null,
                            tint = toolColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = when (toolIndex) {
                                    0 -> "KI-Bild wird gemalt"
                                    1 -> "Video wird animiert"
                                    2 -> "Melodie wird komponiert"
                                    else -> "Geschichte wird geschrieben"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = toolColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "Gemini KI arbeitet für dich...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Prozentanzeige
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = toolColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtiler, eleganter Fortschrittsbalken
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = toolColor,
                trackColor = toolColor.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Wechselnder Statusschritt mit sanftem Übergang
            AnimatedContent(
                targetState = steps.getOrElse(currentStepIndex) { steps.first() },
                transitionSpec = {
                    fadeIn(animationSpec = tween(350)) togetherWith fadeOut(animationSpec = tween(250))
                },
                label = "stepTextAnimation"
            ) { stepText ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}

