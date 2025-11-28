package dev.igorcferreira.msgraphapi.authentication

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.io.encoding.Base64

actual open class MSAuthenticationProvider actual constructor(
    private val tenantId: String,
    private val clientId: String,
    private val scopes: List<String>
): TokenProvider, UserProvider {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private var activity: WeakReference<Activity> = WeakReference(null)
    private val context: Context?
        get() = activity.get()?.applicationContext

    open fun attachedToActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    actual override suspend fun getToken(): String = withContext(coroutineScope.coroutineContext) {
        val context = context ?: throw RuntimeException("Authentication requires an attached activity")

        val application = makeApplication(context)
        val account = application.accounts.firstOrNull()
        if (account != null) {
            val result = refresh(account, scopes, application)
            if (result != null) {
                return@withContext result.accessToken
            }
        }

        val result = application.acquireToken {
            startAuthorizationFromActivity(activity.get())
            withScopes(scopes)
            fromAuthority(
                AzureCloudInstance.AzurePublic,
                AadAuthorityAudience.AzureAdMyOrg,
                tenantId
            )
        }
        result.accessToken
    }

    actual override suspend fun signOut() = withContext(coroutineScope.coroutineContext) {
        val context = context ?: return@withContext
        val application = makeApplication(context)
        val account = application.accounts.firstOrNull()
        when(application) {
            is ISingleAccountPublicClientApplication -> application.signOut()
            is IMultipleAccountPublicClientApplication -> application.removeAccount(account)
        }
    }
    actual override suspend fun getUserName(): String? = withContext(coroutineScope.coroutineContext) {
        val context = context ?: return@withContext null
        val application = makeApplication(context)
        val account = application.accounts.firstOrNull()
        account?.username
    }

    private fun refresh(
        account: IAccount,
        scopes: List<String>,
        application: IPublicClientApplication
    ): IAuthenticationResult? = try {
        val builder = AcquireTokenSilentParameters.Builder()
            .fromAuthority(
                AzureCloudInstance.AzurePublic,
                AadAuthorityAudience.AzureAdMyOrg,
                tenantId
            )
            .withScopes(scopes)
            .forAccount(account)
        val parameter = AcquireTokenSilentParameters(builder)
        application.acquireTokenSilent(parameter)
    } catch (_: Exception) {
        null
    }

    internal open suspend fun makeApplication(
        context: Context
    ): IPublicClientApplication = suspendCoroutine { continuation ->
        val signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            URLEncoder.encode(context.fetchSignature(), Charsets.UTF_8)
        } else {
            URLEncoder.encode(context.fetchSignature(), StandardCharsets.UTF_8.toString())
        }

        val authority = "${AzureCloudInstance.AzurePublic.cloudInstanceUri}/$tenantId"
        val redirectUri = "msauth://${context.packageName}/$signature"
        PublicClientApplication.create(
            context,
            clientId,
            authority,
            redirectUri,
            object : IPublicClientApplication.ApplicationCreatedListener {
                override fun onCreated(application: IPublicClientApplication?) {
                    if (application != null) {
                        continuation.resume(application)
                    } else {
                        continuation.resumeWithException(Exception("Unable to create application"))
                    }
                }

                override fun onError(exception: MsalException?) {
                    Log.e("MSAL", "Unable to get MSAL application", exception)
                    continuation.resumeWithException(exception ?: Exception("Unable to create application"))
                }
            }
        )
    }
}

suspend fun IPublicClientApplication.acquireToken(
    builder: AcquireTokenParameters.Builder.() -> Unit
): IAuthenticationResult = suspendCoroutine { continuation ->
    val paramBuilder = AcquireTokenParameters.Builder()
    builder(paramBuilder)
    val parameters = paramBuilder.withCallback(object : AuthenticationCallback {
        override fun onCancel() {
            continuation.resumeWithException(Exception("Unable to get token"))
        }

        override fun onSuccess(authenticationResult: IAuthenticationResult?) {
            if (authenticationResult?.accessToken != null) {
                continuation.resume(authenticationResult)
            } else {
                continuation.resumeWithException(Exception("Unable to get token"))
            }
        }

        override fun onError(exception: MsalException?) {
            Log.e("MSAL", "Unable to get token", exception)
            continuation.resumeWithException(exception ?: Exception("Unable to get token"))
        }
    })
    .build()
    acquireToken(parameters)
}

val IPublicClientApplication.accounts: List<IAccount>
    get() = when(this) {
        is IMultipleAccountPublicClientApplication -> accounts
        is ISingleAccountPublicClientApplication -> listOf(currentAccount.currentAccount)
        else -> listOf()
    }

fun Context.fetchSignature(
    algorithm: String = "SHA-1",
): String {
    val digest = MessageDigest.getInstance(algorithm)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val sig = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNING_CERTIFICATES
        ).signingInfo ?: return ""

        val signatures = if (sig.hasMultipleSigners()) {
            sig.apkContentsSigners.map {
                digest.update(it.toByteArray())
                Base64.encode(digest.digest())
            }
        } else {
            sig.signingCertificateHistory.map {
                digest.update(it.toByteArray())
                Base64.encode(digest.digest())
            }
        }
        signatures.first()
    } else {
        @Suppress("DEPRECATION")
        val sig = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_SIGNATURES
        ).signatures ?: return ""

        val signatures = sig.map {
            digest.update(it.toByteArray())
            Base64.encode(digest.digest())
        }
        signatures.first()
    }
}
