package com.nammashaale.ai

import com.nammashaale.data.local.AssetEntity
import com.nammashaale.data.local.AuditEntity
import com.nammashaale.data.local.RepairEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class GeminiAiService(private val apiKey: String) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json".toMediaType()
    private val apiUrl = "https://openrouter.ai/api/v1/chat/completions"
    private val model = "google/gemini-2.0-flash-001" // Verified working model

    private suspend fun callOpenRouter(userMessage: String): String = withContext(Dispatchers.IO) {
        try {
            val trimmedKey = apiKey.trim().removePrefix("Bearer ").trim()
            val messages = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userMessage)
                })
            }

            val body = JSONObject().apply {
                put("model", model)
                put("messages", messages)
                put("temperature", 0.7)
            }.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer $trimmedKey")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("HTTP-Referer", "https://nammashaale.app")
                .addHeader("X-Title", "Namma Shaale Inventory")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: "Empty response"

            if (!response.isSuccessful) {
                val errorDetail = try {
                    JSONObject(responseBody).getJSONObject("error").getString("message")
                } catch (e: Exception) {
                    responseBody.take(150)
                }
                
                if (response.code == 401) {
                    return@withContext "API Key Error (401): $errorDetail"
                }
                return@withContext "AI Error ${response.code}: $errorDetail"
            }

            val json = JSONObject(responseBody)
            val choices = json.optJSONArray("choices")
            if (choices == null || choices.length() == 0) {
                return@withContext "No response from AI. Response: $responseBody"
            }
            
            choices.getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
        } catch (e: Exception) {
            "AI Error: ${e.localizedMessage ?: "Unknown error"}"
        }
    }

    /**
     * Test the API key with a simple message.
     */
    suspend fun testApiKey(): String {
        return callOpenRouter("Hello, respond with 'API Connection Successful'.")
    }

    /**
     * Predict maintenance needs for an asset based on its history.
     */
    suspend fun predictMaintenance(
        asset: AssetEntity,
        repairs: List<RepairEntity>,
        audits: List<AuditEntity>
    ): String {
        val purchasedDate = if (asset.purchaseDate > 0)
            SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(asset.purchaseDate))
        else "Unknown"

        val repairHistory = if (repairs.isEmpty()) "No repair history."
        else repairs.take(5).joinToString("\n") { "- ${it.issue} (${it.status})" }

        val auditSummary = if (audits.isEmpty()) "No audit history."
        else audits.groupBy { it.status }.mapValues { it.value.size }
            .entries.joinToString(", ") { "${it.value}x ${it.key}" }

        val prompt = """
You are an AI assistant for a school asset management system. Analyze this asset and give a concise, actionable maintenance prediction in 2-3 sentences.

Asset: ${asset.assetName}
Category: ${asset.category}
Current Condition: ${asset.condition}
Purchase Date: $purchasedDate
Location: ${asset.location}
Recent Repairs: $repairHistory
Audit History: $auditSummary

Provide:
1. Risk level (Low / Medium / High)
2. Likely next maintenance need
3. Recommended action

Keep it short and practical for a school administrator.
        """.trimIndent()

        return callOpenRouter(prompt)
    }

    /**
     * Generate a natural language audit summary report.
     */
    suspend fun generateAuditSummary(
        assets: List<AssetEntity>,
        auditResults: Map<Int, String>
    ): String {
        val working = auditResults.values.count { it == "Working" }
        val needsRepair = auditResults.values.count { it == "Needs Repair" }
        val broken = auditResults.values.count { it == "Broken" }
        val missing = auditResults.values.count { it == "Missing" }

        // Only include assets that were actually part of this audit to save tokens
        val auditedAssetsInfo = assets.filter { it.assetId in auditResults.keys }
            .take(30) // Limit to first 30 to avoid huge prompts
            .joinToString("\n") { "- ${it.assetName}: ${auditResults[it.assetId]}" }

        val prompt = """
You are an AI assistant for a school. Generate a friendly, concise audit summary report in 3-4 sentences for the school principal.

Audit Stats:
- Working: $working
- Needs Repair: $needsRepair
- Broken: $broken
- Missing: $missing

Audited Items Detail:
$auditedAssetsInfo

Write in a professional tone. Highlight urgent items and end with a recommendation.
        """.trimIndent()

        return callOpenRouter(prompt)
    }

    /**
     * Suggest a repair solution for a reported issue.
     */
    suspend fun suggestRepairSolution(
        assetName: String,
        category: String,
        issue: String
    ): String {
        val prompt = """
You are a helpful assistant for a school maintenance team.
An issue has been reported for a school asset. Suggest a practical repair solution.

Asset: $assetName
Category: $category
Reported Issue: $issue

Provide:
1. Likely cause (1 line)
2. Recommended fix (2-3 lines, simple and practical)
3. Urgency: Low / Medium / High

Keep it simple enough for a non-technical school administrator to understand.
        """.trimIndent()

        return callOpenRouter(prompt)
    }
}
