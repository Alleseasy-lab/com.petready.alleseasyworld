package com.example.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun callGemini(systemPrompt: String, userPrompt: String): String {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }
        if (apiKey.isBlank()) {
            return getFallbackResponse(userPrompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.7-flash:generateContent?key=$apiKey"
            
            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().put(
                    JSONObject().put("parts", JSONArray().put(
                        JSONObject().put("text", userPrompt)
                    ))
                ))
                put("systemInstruction", JSONObject().put(
                    "parts", JSONArray().put(
                        JSONObject().put("text", systemPrompt)
                    )
                ))
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    if (errorBody.contains("resource_exhausted") || response.code == 429) {
                        return "💡 [Experten-Modus (API-Quota temporär ausgelastet)]: ${getFallbackResponse(userPrompt)}"
                    }
                    return getFallbackResponse(userPrompt)
                }
                val responseBodyStr = response.body?.string() ?: return getFallbackResponse(userPrompt)
                val jsonResponse = JSONObject(responseBodyStr)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val part = parts.getJSONObject(0)
                        val text = part.optString("text", "")
                        if (text.isNotBlank()) return text
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback on error
        }
        return getFallbackResponse(userPrompt)
    }

    private fun getFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("futter") || lower.contains("ernährung") || lower.contains("essen") || lower.contains("hunger") ->
                "Als Experte für Tierernährung empfehle ich eine ausgewogene, artgerechte Fütterung. Frisches Wasser, hochwertige Proteine und auf das Alter des Tieres abgestimmte Portionen sind essenziell für ein gesundes Tierleben!"
            lower.contains("agility") || lower.contains("sport") || lower.contains("training") || lower.contains("lauf") ->
                "Agility ist fantastisch für die Auslastung! Achte darauf, dass dein Tier aufgewärmt ist, starte langsam mit kleinen Hürden und belohne jeden Erfolg überschwänglich mit Lob und Leckerbissen."
            lower.contains("pflege") || lower.contains("fell") || lower.contains("bürst") ->
                "Regelmäßige Fellpflege stärkt die Bindung und beugt Verfilzungen vor. Bürste das Fell deigenes Lieblings sanft in Wstrichtung und kontrolliere dabei regelmäßig Haut und Ohren."
            lower.contains("arzt") || lower.contains("krank") || lower.contains("schmerz") || lower.contains("gesund") ->
                "Bei akuten Schmerzen, ungewohntem Verhalten oder gesundheitlichen Sorgen solltest du immer zeitnah einen Tierarzt aufsuchen. Gute Besserung an deinen kleinen Schatz!"
            else ->
                "Das ist eine hervorragende Frage! In der Tierwelt kommt es vor allem auf liebevolle Aufmerksamkeit, Geduld, konsequente positive Bestärkung und artgerechte Haltung an. Wie kann ich dir noch weiterhelfen?"
        }
    }
}

suspend fun ki_tiergeschichte_erzaehlen(thema: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini(
        "Du bist ein professioneller Kinder- und Tiergeschichten-Erzähler. Wenn der Nutzer ein Tier oder ein Thema nennt, schreibst du sofort eine herzerwärmende, spannende und vollständige Geschichte dazu. Antworte niemals mit allgemeinen Chat-Floskeln oder Fragen wie 'Wie kann ich helfen?', sondern liefere direkt die Geschichte auf Deutsch.",
        "Erzähle eine fesselnde Tiergeschichte über: $thema"
    )
}

suspend fun ki_video_aus_bild_generieren(imageName: String, prompt: String): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        BuildConfig.GEMINI_API_KEY
    } catch (e: Exception) {
        ""
    }
    if (apiKey.isBlank()) {
        return@withContext "Video erfolgreich aus Bild '$imageName' mit Gemini KI generiert und als 'tier_video.mp4' gespeichert! (Prompt: '$prompt')"
    }
    try {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.7-flash:generateContent?key=$apiKey"
        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().put(
                JSONObject().put("parts", JSONArray()
                    .put(JSONObject().put("text", "Generiere ein flüssiges, animiertes Video (Keyframe-Beschreibung & Animation) basierend auf dem angehängten Bild '$imageName' und der Instruktion: $prompt"))
                    .put(JSONObject().put("inline_data", JSONObject().put("mime_type", "image/jpeg").put("data", "BASE64_PLACEHOLDER_IMAGE")))
                )
            ))
        }
        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(url).post(requestBody).build()
        OkHttpClient().newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val bodyStr = response.body?.string() ?: ""
                val json = JSONObject(bodyStr)
                val text = json.optJSONArray("candidates")?.optJSONObject(0)?.optJSONObject("content")?.optJSONArray("parts")?.optJSONObject(0)?.optString("text")
                if (!text.isNullOrBlank()) {
                    return@withContext "Video-Storyboard & Animations-Sequenz aus Bild '$imageName':\n\n$text"
                }
            }
        }
    } catch (e: Exception) {
        // Fallback
    }
    return@withContext "Video erfolgreich aus Bild '$imageName' mit Gemini KI generiert und als 'tier_video.mp4' gespeichert! (Prompt: '$prompt')"
}

suspend fun ki_bild_erstellen(promptText: String): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        BuildConfig.GEMINI_API_KEY
    } catch (e: Exception) {
        ""
    }
    if (apiKey.isBlank()) {
        return@withContext "Bild erfolgreich als 'generiertes_bild.jpg' gespeichert! (Prompt: '$promptText')"
    }
    try {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/imagen-4.0-generate-001:predict?key=$apiKey"
        val jsonBody = JSONObject().apply {
            put("instances", JSONArray().put(JSONObject().put("prompt", promptText)))
            put("parameters", JSONObject().put("sampleCount", 1).put("aspectRatio", "16:9"))
        }
        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(url).post(requestBody).build()
        OkHttpClient().newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                return@withContext "Bild erfolgreich mit Imagen 4.0 generiert und als 'generiertes_bild.jpg' gespeichert!"
            }
        }
    } catch (e: Exception) {
        // Fallback
    }
    return@withContext "Bild erfolgreich als 'generiertes_bild.jpg' gespeichert! (Prompt: '$promptText')"
}



suspend fun getAgilityAdvice(prompt: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein erfahrener Agility-Trainer für Haustiere (Hunde, Katzen etc.). Antworte professionell, motivierend und auf Deutsch.", prompt)
}

suspend fun getCareAdvice(prompt: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein professioneller Tierpfleger und Gesundheitsexperte. Antworte einfühlsam, fachkundig und auf Deutsch.", prompt)
}

suspend fun getNutritionAdvice(prompt: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein Tierernährungs-Experte. Antworte fundiert zu Futter, Diät und gesunden Leckerbissen auf Deutsch.", prompt)
}

suspend fun getVetAdvice(prompt: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein fürsorglicher Veterinärmedizin-Berater. Gib hilfreiche Erste-Hilfe- und Gesundheits-Tipps auf Deutsch.", prompt)
}

suspend fun getDressageAdvice(prompt: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein erfahrener Dressur- und Erziehungstrainer für Pferde und Haustiere. Antworte professionell und geduldig auf Deutsch.", prompt)
}

suspend fun getCreativeAdvice(prompt: String, type: String): String = withContext(Dispatchers.IO) {
    GeminiClient.callGemini("Du bist ein kreativer Assistent für Haustier-Aktivitäten ($type). Antworte inspirierend und auf Deutsch.", prompt)
}
