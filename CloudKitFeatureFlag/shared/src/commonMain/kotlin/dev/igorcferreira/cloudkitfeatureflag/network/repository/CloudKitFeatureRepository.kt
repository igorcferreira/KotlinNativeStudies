package dev.igorcferreira.cloudkitfeatureflag.network.repository

import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitAppFeatureRecord
import dev.igorcferreira.cloudkitfeatureflag.network.model.CloudKitAppFeaturesResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.*
import io.ktor.http.headers

class CloudKitFeatureRepository(
    private val httpClient: HttpClient,
) {
    suspend fun getFeatures(
        recordName: String,
        container: String
    ): CloudKitAppFeatureRecord {
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
        return records.records.first()
    }
}
