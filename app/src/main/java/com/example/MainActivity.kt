package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.PetProfileRepository
import com.example.data.ActivityRepository
import androidx.activity.compose.BackHandler
import com.example.ui.EntranceScreen
import com.example.ui.ActivityScreen
import com.example.ui.ActivityViewModel
import com.example.ui.ActivityViewModelFactory
import com.example.ui.AgilityAdvisorScreen
import com.example.ui.CareAdvisorScreen
import com.example.ui.DressageAdvisorScreen
import com.example.ui.FeedingScreen
import com.example.ui.PetProfileViewModel
import com.example.ui.PetProfileViewModelFactory
import com.example.ui.ProfileScreen
import com.example.ui.VolunteerScreen
import com.example.ui.YouTubeScreen
import com.example.ui.VetFinderScreen
import com.example.ui.CommunityScreen
import com.example.ui.MonetizationScreen
import com.example.ui.CreativeStudioScreen
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        
        val petRepository = PetProfileRepository(database.petProfileDao())
        val petProfileViewModel = ViewModelProvider(this, PetProfileViewModelFactory(petRepository))[PetProfileViewModel::class.java]
        
        val activityRepository = ActivityRepository(database.activityEntryDao())
        val activityViewModel = ViewModelProvider(this, ActivityViewModelFactory(activityRepository))[ActivityViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            
            CompositionLocalProvider(
                LocalThemeToggle provides { isDark -> isDarkTheme = isDark },
                LocalIsDarkTheme provides isDarkTheme
            ) {
                MyApplicationTheme(darkTheme = isDarkTheme) {
                    var hasEntered by remember { mutableStateOf(false) }
                    var selectedTab by remember { mutableStateOf(0) }
                    var addProfileForSpecies by remember { mutableStateOf<String?>(null) }

                    BackHandler(enabled = hasEntered) {
                        if (selectedTab != 0) {
                            selectedTab = 0
                        } else {
                            hasEntered = false
                        }
                    }

                    if (!hasEntered) {
                        EntranceScreen(
                            onEnter = { hasEntered = true },
                            onNavigateToYouTube = {
                                hasEntered = true
                                selectedTab = 7
                            },
                            onNavigateToMusic = {
                                hasEntered = true
                                selectedTab = 12
                            }
                        )
                    } else {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = { BottomNav(selectedTab) { selectedTab = it } },
                            containerColor = MaterialTheme.colorScheme.background
                        ) { innerPadding ->
                            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                                if (selectedTab == 0) {
                                    CosmicDashboard(
                                        onNavigate = { tabIndex -> selectedTab = tabIndex },
                                        onBackToEntrance = { hasEntered = false },
                                        onCreatePet = { species ->
                                            addProfileForSpecies = species
                                            selectedTab = 3
                                        }
                                    )
                                } else if (selectedTab == 1) {
                                    ActivityScreen(viewModel = activityViewModel)
                                } else if (selectedTab == 2) {
                                    VetFinderScreen()
                                } else if (selectedTab == 3) {
                                    ProfileScreen(
                                        viewModel = petProfileViewModel,
                                        initialSpecies = addProfileForSpecies,
                                        onDialogDismissed = { addProfileForSpecies = null }
                                    )
                                } else if (selectedTab == 4) {
                                    AgilityAdvisorScreen()
                                } else if (selectedTab == 5) {
                                    CareAdvisorScreen()
                                } else if (selectedTab == 6) {
                                    VolunteerScreen()
                                } else if (selectedTab == 7) {
                                    YouTubeScreen()
                                } else if (selectedTab == 8) {
                                    FeedingScreen()
                                } else if (selectedTab == 9) {
                                    DressageAdvisorScreen()
                                } else if (selectedTab == 10) {
                                    CommunityScreen()
                                } else if (selectedTab == 11) {
                                    MonetizationScreen()
                                } else if (selectedTab == 12) {
                                    CreativeStudioScreen()
                                } else {
                                    ComingSoonScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNav(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp
    ) {
        val tabs = listOf(
            Triple("Portal", Icons.Default.Home, 0),
            Triple("Aktivität", Icons.Default.EventNote, 1),
            Triple("Medizin", Icons.Default.Favorite, 2),
            Triple("Community", Icons.Default.Forum, 10),
            Triple("VIP Club", Icons.Default.Star, 11)
        )
        tabs.forEach { (title, icon, tabId) ->
            NavigationBarItem(
                selected = selectedTab == tabId,
                onClick = { onTabSelected(tabId) },
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun ComingSoonScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Modul wird initialisiert...", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
    }
}

data class DashboardItem(
    val title: String, 
    val icon: String, 
    val color: Color, 
    val description: String = "", 
    val actionTab: Int? = null,
    val actionPetType: String? = null
)

@Composable
fun CosmicDashboard(
    onNavigate: (Int) -> Unit,
    onBackToEntrance: () -> Unit = {},
    onCreatePet: (String) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val error = MaterialTheme.colorScheme.error

    val petTypes = listOf(
        DashboardItem("Hunde", "🐺", primary, actionPetType = "Hund"),
        DashboardItem("Katzen", "🐈‍⬛", primary, actionPetType = "Katze"),
        DashboardItem("Nager", "🐹", primary, "Hamster, Hasen, Meerschweinchen", actionPetType = "Nager"),
        DashboardItem("Vögel", "🦅", primary, "Sittiche, Kanarienvögel", actionPetType = "Vogel"),
        DashboardItem("Reptilien", "🐍", primary, actionPetType = "Reptil"),
        DashboardItem("Pferde", "🐎", primary, actionPetType = "Pferd")
    )
    val careAndTraining = listOf(
        DashboardItem("Pflege", "✨", secondary, actionTab = 5),
        DashboardItem("Fütterung", "🥩", secondary, actionTab = 8),
        DashboardItem("Agility", "⚡", secondary, actionTab = 4),
        DashboardItem("Dressur", "🎯", secondary, actionTab = 9)
    )
    val medicalAndRescue = listOf(
        DashboardItem("Tierarzt & Klinik", "👨‍⚕️", primary, "In deiner Nähe", actionTab = 2),
        DashboardItem("Tiernotruf", "🚨", error, "24/7 Notfall", actionTab = 2),
        DashboardItem("Tierrettung", "🚁", error, "Schnelle Hilfe", actionTab = 2)
    )
    val charity = listOf(
        DashboardItem("Ehrenamt", "🤝", secondary, "Tierheime & Spenden", actionTab = 6),
        DashboardItem("Community", "💬", primary, "Austausch & Tipps", actionTab = 10)
    )
    val vipClub = listOf(
        DashboardItem("VIP Club & Pro", "⭐", primary, "Werbefrei & exklusive AI", actionTab = 11)
    )
    val aiTools = listOf(
        DashboardItem("KI-Bildstudio", "🖼️", primary, "Bilder generieren", actionTab = 12),
        DashboardItem("Video-Generator", "🎥", primary, "Trainings-Videos", actionTab = 12),
        DashboardItem("Entspannungs-Musik", "🎵", primary, "Beruhigende Klänge", actionTab = 12),
        DashboardItem("Tiergeschichten", "📖", primary, "Einschlafgeschichten", actionTab = 12)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { HeaderSection(onNavigate = onNavigate, onBackToEntrance = onBackToEntrance) }
        item { EntrancePortalBanner(onBackToEntrance) }
        item { AppSymbolButtonCard(onNavigate) }
        item {
            SectionTitle("Lebensformen", primary)
            GridSection(petTypes, onNavigate, onCreatePet)
        }
        item {
            SectionTitle("Training & Pflege", secondary)
            GridSection(careAndTraining, onNavigate)
        }
        item {
            SectionTitle("Medizin & Notfall", error)
            GridSection(medicalAndRescue, onNavigate)
        }
        item {
            SectionTitle("KI-Kreativstudio", primary)
            GridSection(aiTools, onNavigate)
        }
        item {
            SectionTitle("Community & Missionen", secondary)
            GridSection(charity, onNavigate)
        }
        item {
            SectionTitle("VIP Club", primary)
            GridSection(vipClub, onNavigate)
        }
    }
}

@Composable
fun EntrancePortalBanner(onBackToEntrance: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBackToEntrance),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xD910142A)),
        border = BorderStroke(1.5.dp, Brush.linearGradient(listOf(Color(0xFFFFEEA0), Color(0xFFFFB300), Color(0xFFFF5E5B))))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(Color(0xFFFFB300), Color(0xFF632B7F)))),
                contentAlignment = Alignment.Center
            ) {
                Text("🌌", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "PET READY PORTAL",
                    color = Color(0xFFFFEEA0),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Zum Eingang & Social Media",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ALLESEASY Philosophie · Spotify · YouTube · Instagram",
                    color = Color(0xFF9AA4C2),
                    fontSize = 11.sp
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Zum Eingang",
                tint = Color(0xFFFFB300)
            )
        }
    }
}

@Composable
fun HeaderSection(onNavigate: (Int) -> Unit, onBackToEntrance: () -> Unit = {}) {
    val isDark = LocalIsDarkTheme.current
    val toggleTheme = LocalThemeToggle.current
    
    val bgStart = if (isDark) Color(0xFF1A1A2E) else MaterialTheme.colorScheme.primaryContainer
    val bgEnd = MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.verticalGradient(listOf(bgStart, bgEnd)))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "PET READY APP",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onBackToEntrance,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Stars,
                        contentDescription = "Portal Eingang",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = { toggleTheme(!isDark) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Theme Toggle",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Anstand. Respekt.\nTierliebe.",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            lineHeight = 40.sp
        )
    }
}

@Composable
fun SectionTitle(title: String, glowColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(20.dp)
                .background(glowColor, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title.uppercase(),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun GridSection(items: List<DashboardItem>, onNavigate: (Int) -> Unit, onCreatePet: (String) -> Unit = {}) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        val chunked = items.chunked(2)
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (item in rowItems) {
                    GlowCard(item, modifier = Modifier.weight(1f)) {
                        if (item.actionTab != null) onNavigate(item.actionTab)
                        else if (item.actionPetType != null) onCreatePet(item.actionPetType)
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun GlowCard(item: DashboardItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .aspectRatio(1.2f)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, item.color.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(item.color.copy(alpha = 0.15f), Color.Transparent),
                        radius = 300f
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(item.icon, fontSize = 40.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if (item.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AppSymbolButtonCard(onNavigate: (Int) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app_symbol),
                contentDescription = "App Symbol",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "★ App-Symbol & Begleiter",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Dein tierisches Lieblingsemblem. Tippe hier für exklusive Grüße und VIP-Features!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Tierwelt App-Symbol") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_symbol),
                        contentDescription = null,
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Dieses liebevolle Symbol repräsentiert deine App und all deine geliebten Haustiere. Viel Freude beim Pflegen und Entdecken!",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onNavigate(11) // Jump to VIP Club
                }) {
                    Text("Zum VIP Club")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Schließen")
                }
            }
        )
    }
}


