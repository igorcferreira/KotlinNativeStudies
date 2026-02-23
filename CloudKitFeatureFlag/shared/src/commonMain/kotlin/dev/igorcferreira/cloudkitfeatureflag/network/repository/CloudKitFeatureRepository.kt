package dev.igorcferreira.cloudkitfeatureflag.network.repository

import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitAppFeaturesResponse
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitIntField
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.headersOf

class CloudKitFeatureRepository(
    private val httpClient: HttpClient,
) {
    suspend fun getFeatures(
        recordName: String,
        container: String
    ): Map<String, CloudKitIntField> {
        val query = """
            {
            "records": [
                {
                    "recordName": "$recordName"
                }
            ]
            }
        """.trimIndent()

        val response = httpClient.post {
            url(urlString = "https://api.apple-cloudkit.com/database/1/$container/production/public/records/lookup")
            contentType(ContentType.Application.Json)
            setBody(query.encodeToByteArray())
            headers {
                headersOf("Accept", "application/json")
            }
        }
        val records: CloudKitAppFeaturesResponse = response.body()
        return records.records.firstOrNull()?.fields ?: emptyMap()
    }
}
