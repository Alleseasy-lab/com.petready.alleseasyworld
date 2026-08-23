package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PostItem(
    val id: Int,
    val author: String,
    val petType: String,
    val title: String,
    val content: String,
    var likes: Int,
    var comments: Int,
    val timeAgo: String
)

@Composable
fun CommunityScreen() {
    var posts by remember {
        mutableStateOf(
            listOf(
                PostItem(1, "Sarah M.", "🐶 Hund", "Tipps gegen Hitze im Sommer?", "Mein Golden Retriever leidet unter den hohen Temperaturen. Habt ihr tolle Abkühlungstipps außer Wassermelone?", 14, 5, "vor 2 Std."),
                PostItem(2, "Thomas K.", "🐈 Katze", "Suche Kratzbaum-Empfehlung", "Welcher deckenhohe Kratzbaum ist stabil genug für zwei temperamentvolle Ragdolls?", 8, 3, "vor 4 Std."),
                PostItem(3, "Laura B.", "🐎 Pferd", "Reitbeteiligung in München gesucht", "Suche für meinen 10-jährigen Wallach eine zuverlässige Reitbeteiligung für 2x pro Woche am Stadtrand.", 22, 11, "vor 1 Tag"),
                PostItem(4, "Max H.", "🐹 Nager", "Selbstgebautes Nagarium", "Habe heute ein neues Gehege für meine Hamster gebaut mit viel Buddelbereich. Fotos folgen!", 19, 7, "vor 2 Tagen")
            )
        )
    }

    var showNewPostDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newContent by remember { mutableStateOf("") }
    var newPetType by remember { mutableStateOf("🐶 Hund") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewPostDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Beitrag erstellen")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            Text(
                text = "COMMUNITY & AUSTAUSCH",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tierhalter-Plattform",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                lineHeight = 40.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(posts) { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = post.author,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = post.petType,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = post.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = post.timeAgo,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            posts = posts.map { if (it.id == post.id) it.copy(likes = it.likes + 1) else it }
                                        }
                                    ) {
                                        Icon(Icons.Default.Favorite, contentDescription = "Like", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "${post.likes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Comment, contentDescription = "Kommentare", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "${post.comments}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNewPostDialog) {
        AlertDialog(
            onDismissRequest = { showNewPostDialog = false },
            title = { Text("Neuen Beitrag erstellen") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Titel") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = { Text("Was möchtest du teilen?") },
                        modifier = Modifier.height(120.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                            val newPost = PostItem(
                                id = posts.size + 1,
                                author = "Du",
                                petType = newPetType,
                                title = newTitle,
                                content = newContent,
                                likes = 0,
                                comments = 0,
                                timeAgo = "Gerade eben"
                            )
                            posts = listOf(newPost) + posts
                            newTitle = ""
                            newContent = ""
                            showNewPostDialog = false
                        }
                    }
                ) {
                    Text("Veröffentlichen")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPostDialog = false }) {
                    Text("Abbrechen")
                }
            }
        )
    }
}
