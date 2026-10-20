package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EntranceScreen(
    onEnter: () -> Unit,
    onNavigateToYouTube: () -> Unit = {},
    onNavigateToMusic: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedSocialInfo by remember { mutableStateOf<String?>(null) }
    var showAlleseasyDialog by remember { mutableStateOf(false) }

    // Breathing pulse animation for the golden button
    val infiniteTransition = rememberInfiniteTransition(label = "portalPulse")
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.035f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A1A))
    ) {
        // Celestial Star & Orbit Background Canvas
        CosmicBackgroundCanvas(glowAlpha)

        // Main content in a vertical scroll container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Title & Subtitles
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // Glow effect behind title
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "PET READY",
                        color = Color(0xFFFF5252).copy(alpha = 0.25f),
                        fontSize = 37.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 7.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "PET READY",
                        color = Color(0xFFFF5E5B),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 6.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Komm erst mal an.",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Anstand · Respekt · Zwischenmenschlichkeit",
                    color = Color(0xFFB3B9D0),
                    fontSize = 13.sp,
                    letterSpacing = 0.6.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Center: Glowing Golden "Tritt ein" Pill Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .scale(buttonScale)
                        .padding(8.dp)
                ) {
                    // Outer radiant aura
                    Box(
                        modifier = Modifier
                            .width(226.dp)
                            .height(66.dp)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(34.dp),
                                ambientColor = Color(0xFFFFB300).copy(alpha = glowAlpha),
                                spotColor = Color(0xFFFF8F00).copy(alpha = glowAlpha)
                            )
                    )

                    // Button border and body
                    Box(
                        modifier = Modifier
                            .width(220.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .border(
                                BorderStroke(
                                    2.5.dp,
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFFFFEEA0),
                                            Color(0xFFFFB300),
                                            Color(0xFFFF8F00),
                                            Color(0xFFFFE082)
                                        )
                                    )
                                ),
                                shape = RoundedCornerShape(30.dp)
                            )
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF632B7F),
                                        Color(0xFF451A5A),
                                        Color(0xFF28103A)
                                    )
                                )
                            )
                            .clickable(onClick = onEnter)
                            .testTag("portal_enter_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tritt ein",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Branding subtitle: "Ein Produkt von ALLESEASYworld"
                Text(
                    text = "Ein Produkt von ALLESEASYworld",
                    color = Color(0xFF5D7CE6),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { showAlleseasyDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            // Bottom Section: Social Media & Philosophie
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Social Media Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xD910142A)
                    ),
                    border = BorderStroke(1.dp, Color(0x334B5E8A))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Social Media",
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Social Media",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Row of 5 Social Media Icons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SocialMediaIconItem(
                                title = "Spotify",
                                onClick = {
                                    openSpotify(context)
                                }
                            ) {
                                SpotifyIconView()
                            }

                            SocialMediaIconItem(
                                title = "Facebook",
                                onClick = {
                                    selectedSocialInfo = "Facebook"
                                    openUrl(context, "https://facebook.com")
                                }
                            ) {
                                FacebookIconView()
                            }

                            SocialMediaIconItem(
                                title = "Instagram",
                                onClick = {
                                    selectedSocialInfo = "Instagram"
                                    openUrl(context, "https://instagram.com")
                                }
                            ) {
                                InstagramIconView()
                            }

                            SocialMediaIconItem(
                                title = "TikTok",
                                onClick = {
                                    selectedSocialInfo = "TikTok"
                                    openUrl(context, "https://tiktok.com")
                                }
                            ) {
                                TikTokIconView()
                            }

                            SocialMediaIconItem(
                                title = "YouTube",
                                onClick = {
                                    selectedSocialInfo = "YouTube"
                                    onNavigateToYouTube()
                                }
                            ) {
                                YouTubeIconView()
                            }
                        }
                    }
                }

                // Willkommen & Philosophie Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xCC111529)
                    ),
                    border = BorderStroke(1.dp, Color(0x2B4A628B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Willkommen",
                            color = Color(0xFF22C55E),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "ALLESEASY Philosophie:",
                            color = Color(0xFFE2E6F5),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "„ALLESEASY ist Anstand, Respekt, und Zwischenmenschlichkeit.“",
                            color = Color(0xFF9AA4C2),
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sparkling 4-point star at bottom right
        SparkleStar(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 28.dp)
        )
    }

    // Info Dialog for ALLESEASY Philosophie
    if (showAlleseasyDialog) {
        AlertDialog(
            onDismissRequest = { showAlleseasyDialog = false },
            title = {
                Text(
                    text = "ALLESEASYworld & Pet Ready",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Willkommen bei Pet Ready App – kreiert im Geiste von ALLESEASYworld.",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Unsere Leitwerte sind Anstand, Respekt und echte Zwischenmenschlichkeit sowie liebevolle Hingabe für jedes Tier.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showAlleseasyDialog = false
                    onEnter()
                }) {
                    Text("Jetzt eintreten")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAlleseasyDialog = false }) {
                    Text("Schließen")
                }
            }
        )
    }

    // Info Dialog for Social Media item
    if (selectedSocialInfo != null) {
        AlertDialog(
            onDismissRequest = { selectedSocialInfo = null },
            title = { Text(selectedSocialInfo ?: "") },
            text = {
                Text(
                    when (selectedSocialInfo) {
                        "Spotify" -> "Öffne Spotify für entspannende Playlists und beruhigende Klänge für dein Haustier von ALLESEASYworld."
                        "YouTube" -> "Öffne informative Trainings- & Pflegevideos direkt in der Pet Ready App."
                        else -> "Besuche die offizielle $selectedSocialInfo Community von ALLESEASYworld und Pet Ready."
                    }
                )
            },
            confirmButton = {
                Button(onClick = {
                    val target = selectedSocialInfo
                    selectedSocialInfo = null
                    if (target == "YouTube") {
                        onNavigateToYouTube()
                    } else if (target == "Spotify") {
                        openSpotify(context)
                    }
                }) {
                    Text(if (selectedSocialInfo == "YouTube") "In App öffnen" else if (selectedSocialInfo == "Spotify") "Spotify öffnen" else "Verstanden")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedSocialInfo = null }) {
                    Text("Schließen")
                }
            }
        )
    }
}

@Composable
fun CosmicBackgroundCanvas(glowAlpha: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Concentric Orbital Ellipses centered around upper middle
        val center = Offset(w * 0.5f, h * 0.45f)

        // Orbital ring 1
        drawCircle(
            color = Color(0x1A6B8AFD),
            radius = w * 0.38f,
            center = center,
            style = Stroke(width = 1.2f)
        )

        // Orbital ring 2
        drawCircle(
            color = Color(0x14833AB4),
            radius = w * 0.62f,
            center = center,
            style = Stroke(width = 1.2f)
        )

        // Orbital ring 3
        drawCircle(
            color = Color(0x0E5E72E4),
            radius = w * 0.88f,
            center = center,
            style = Stroke(width = 1.5f)
        )

        // Fixed glowing stars & orbital nodes matching the screenshot
        val stars = listOf(
            Triple(0.20f, 0.33f, Color(0xFFF59E0B)), // Orange glow star
            Triple(0.79f, 0.28f, Color(0xFFEC4899)), // Pink star
            Triple(0.18f, 0.56f, Color(0xFF38BDF8)), // Cyan star
            Triple(0.85f, 0.44f, Color(0xFFA855F7)), // Purple star
            Triple(0.35f, 0.40f, Color(0xFFEF4444)), // Red small node
            Triple(0.66f, 0.54f, Color(0xFF10B981)), // Emerald node
            Triple(0.22f, 0.88f, Color(0xFFF97316)), // Warm bottom node
            Triple(0.28f, 0.89f, Color(0xFFEC4899)), // Pink bottom node
            Triple(0.78f, 0.85f, Color(0xFF8B5CF6)), // Lavender bottom node
            Triple(0.68f, 0.80f, Color(0xFF38BDF8))  // Light blue bottom node
        )

        for ((rx, ry, color) in stars) {
            val starPos = Offset(w * rx, h * ry)
            // Soft halo
            drawCircle(
                color = color.copy(alpha = 0.25f * glowAlpha),
                radius = 8f,
                center = starPos
            )
            // Bright core
            drawCircle(
                color = color.copy(alpha = 0.95f),
                radius = 3.2f,
                center = starPos
            )
        }

        // Tiny white distant stars
        val whiteStars = listOf(
            Offset(w * 0.12f, h * 0.18f),
            Offset(w * 0.88f, h * 0.15f),
            Offset(w * 0.55f, h * 0.22f),
            Offset(w * 0.08f, h * 0.45f),
            Offset(w * 0.92f, h * 0.65f),
            Offset(w * 0.42f, h * 0.72f),
            Offset(w * 0.14f, h * 0.78f)
        )

        for (pt in whiteStars) {
            drawCircle(
                color = Color.White.copy(alpha = 0.7f * glowAlpha),
                radius = 1.8f,
                center = pt
            )
        }
    }
}

@Composable
fun SparkleStar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(24.dp)) {
        val path = Path().apply {
            val cx = size.width / 2
            val cy = size.height / 2
            moveTo(cx, 0f)
            quadraticTo(cx, cy, size.width, cy)
            quadraticTo(cx, cy, cx, size.height)
            quadraticTo(cx, cy, 0f, cy)
            quadraticTo(cx, cy, cx, 0f)
            close()
        }
        drawPath(
            path = path,
            brush = Brush.radialGradient(
                listOf(Color.White, Color(0x88FFFFFF), Color.Transparent),
                radius = size.width / 2
            )
        )
    }
}

@Composable
fun SocialMediaIconItem(
    title: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
            .testTag("social_btn_$title")
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = Color(0xFFC0C7DC),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// Custom faithful replicas of the 5 social media badges:

@Composable
fun SpotifyIconView() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1ED760), Color(0xFF128C3E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(26.dp)) {
            val strokeWidth = 2.8f
            // 3 Curved soundwave lines
            drawArc(
                color = Color.Black.copy(alpha = 0.85f),
                startAngle = 200f,
                sweepAngle = 100f,
                useCenter = false,
                topLeft = Offset(2f, 2f),
                size = Size(size.width - 4f, size.height - 4f),
                style = Stroke(width = strokeWidth + 0.8f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Black.copy(alpha = 0.85f),
                startAngle = 202f,
                sweepAngle = 95f,
                useCenter = false,
                topLeft = Offset(5f, 6f),
                size = Size(size.width - 10f, size.height - 8f),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Black.copy(alpha = 0.85f),
                startAngle = 204f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(8f, 10f),
                size = Size(size.width - 16f, size.height - 12f),
                style = Stroke(width = strokeWidth - 0.4f, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun FacebookIconView() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF2384FF), Color(0xFF1456B8))
                )
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = "f",
            color = Color.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
    }
}

@Composable
fun InstagramIconView() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF833AB4),
                        Color(0xFFFD1D1D),
                        Color(0xFFFCB045)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            // Camera outer box
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(2f, 2f),
                size = Size(size.width - 4f, size.height - 4f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
                style = Stroke(width = 2.4f)
            )
            // Lens circle
            drawCircle(
                color = Color.White,
                radius = 4.5f,
                center = Offset(size.width / 2, size.height / 2),
                style = Stroke(width = 2.4f)
            )
            // Flash dot
            drawCircle(
                color = Color.White,
                radius = 1.4f,
                center = Offset(size.width - 6f, 6f)
            )
        }
    }
}

@Composable
fun TikTokIconView() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1A1D2E), Color(0xFF0D0E17))
                )
            )
            .border(1.dp, Color(0x334B5E8A), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(26.dp)) {
            // Dual note offset effect (cyan and magenta)
            val notePathCyan = Path().apply {
                moveTo(size.width * 0.65f, size.height * 0.15f)
                lineTo(size.width * 0.65f, size.height * 0.65f)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        left = size.width * 0.35f,
                        top = size.height * 0.50f,
                        right = size.width * 0.65f,
                        bottom = size.height * 0.85f
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                close()
            }
            drawPath(notePathCyan, color = Color(0xFF00F2FE).copy(alpha = 0.85f))

            val notePathMagenta = Path().apply {
                moveTo(size.width * 0.60f, size.height * 0.18f)
                lineTo(size.width * 0.60f, size.height * 0.68f)
                arcTo(
                    rect = androidx.compose.ui.geometry.Rect(
                        left = size.width * 0.30f,
                        top = size.height * 0.53f,
                        right = size.width * 0.60f,
                        bottom = size.height * 0.88f
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                close()
            }
            drawPath(notePathMagenta, color = Color(0xFFFE2C55).copy(alpha = 0.85f))

            // White center
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = Offset(size.width * 0.46f, size.height * 0.70f)
            )
        }
    }
}

@Composable
fun YouTubeIconView() {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF2424), Color(0xFFB70E0E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "YouTube",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

private fun openUrl(context: android.content.Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
    }
}

private fun openSpotify(context: android.content.Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com")).apply {
            `package` = "com.spotify.music"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.spotify.com")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        } catch (_: Exception) {
        }
    }
}
