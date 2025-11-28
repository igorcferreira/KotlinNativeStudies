@file:OptIn(ExperimentalForeignApi::class)

package dev.igorcferreira.msgraphapi.authentication

import cocoapods.MSAL.*
import dev.igorcferreira.msgraphapi.network.NetworkException
import kotlinx.cinterop.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual class MSAuthenticationProvider actual constructor(
    private val tenantId: String,
    private val clientId: String,
    private val scopes: List<String>
): TokenProvider, UserProvider {
    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    actual override suspend fun getToken(): String {
        val application = makeApplication()
        val currentAccount = application.getCurrentAccountWithParameters()

        if (currentAccount != null) {
            val parameters = MSALSilentTokenParameters(scopes = scopes, account = currentAccount)
            val refreshedToken = application.acquireTokenSilentWithParameters(parameters)
            if (refreshedToken != null) {
                return refreshedToken.accessToken
            }
        }

        val controller = fetchViewController()
            ?: throw AuthenticationError()
        val webViewParameters = MSALWebviewParameters(authPresentationViewController = controller)
        val parameters = MSALInteractiveTokenParameters(scopes = scopes, webviewParameters = webViewParameters)
        val result = application.acquireTokenWithParameters(parameters)

        if (result == null) {
            throw AuthenticationError()
        } else {
            return result.accessToken
        }
    }

    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    actual override suspend fun signOut() {
        val application = makeApplication()
        val account = getCurrentAccount() ?: return

        val controller = fetchViewController()
            ?: throw AuthenticationError()

        val webviewParameters = MSALWebviewParameters(authPresentationViewController = controller)
        val parameters = MSALSignoutParameters(webviewParameters)
        parameters.signoutFromBrowser = true
        parameters.wipeAccount = true
        parameters.wipeAccount = true

        application.signoutWithAccount(account, parameters)
    }

    @Throws(NetworkException::class, AuthenticationError::class, Exception::class)
    actual override suspend fun getUserName(): String? {
        val account = getCurrentAccount() ?: throw AuthenticationError()
        return account.username
    }

    @OptIn(BetaInteropApi::class)
    internal fun makeApplication(): MSALPublicClientApplication {
        val authorityURL = NSURL(string = "https://login.microsoftonline.com/$tenantId")
        val application = memScoped {
            val errorPtr = allocPointerTo<ObjCObjectVar<NSError?>>()
            val authority = MSALAADAuthority(uRL = authorityURL, error = errorPtr.value)

            val authorityError = errorPtr.value?.pointed?.value
            if (authorityError != null) {
                throw AuthenticationError(authorityError.localizedDescription)
            }

            val configuration = MSALPublicClientApplicationConfig(
                clientId = clientId,
                redirectUri = null,
                authority = authority
            )

            val applicationError = errorPtr.value?.pointed?.value
            if (applicationError != null) {
                throw Error(applicationError.localizedDescription)
            }

            MSALPublicClientApplication(configuration=configuration, error=errorPtr.value)
        }
        return application
    }

    private suspend fun getCurrentAccount(): MSALAccount? {
        val application = makeApplication()
        return fetchAccount(application)
    }

    private suspend fun fetchAccount(
        application: MSALPublicClientApplication
    ): MSALAccount? = application.getCurrentAccountWithParameters()

    private companion object {
        suspend fun fetchViewController(): UIViewController? = suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.Main).launch {
                val viewController = UIApplication
                    .sharedApplication
                    .connectedScenes
                    .mapNotNull { it as? UIScene }
                    .filter { it.activationState == UISceneActivationStateForegroundActive }
                    .mapNotNull { it as? UIWindowScene }
                    .flatMap { it.windows }
                    .mapNotNull { it as? UIWindow }
                    .filter { it.keyWindow }
                    .mapNotNull { it.rootViewController }
                    .lastOrNull()
                continuation.resume(viewController)
            }
        }
    }
}

suspend fun MSALPublicClientApplication.signoutWithAccount(
    account: MSALAccount,
    parameters: MSALSignoutParameters
): Boolean = suspendCoroutine { continuation ->
    CoroutineScope(Dispatchers.Main).launch {
        signoutWithAccount(account, parameters) { success, error ->
            if (error != null) {
                continuation.resumeWithException(AuthenticationError(error.localizedDescription))
            } else {
                continuation.resume(success)
            }
        }
    }
}

suspend fun MSALPublicClientApplication.getCurrentAccountWithParameters(
    parameters: MSALParameters? = null
): MSALAccount? = suspendCoroutine { continuation ->
    getCurrentAccountWithParameters(
        parameters = parameters,
        completionBlock = { _, current, error ->
            if (error != null) {
                continuation.resumeWithException(AuthenticationError(error.localizedDescription))
            } else {
                continuation.resume(current)
            }
        }
    )
}

suspend fun MSALPublicClientApplication.acquireTokenWithParameters(
    parameters: MSALInteractiveTokenParameters
): MSALResult? = suspendCoroutine { continuation ->
    CoroutineScope(Dispatchers.Main).launch {
        acquireTokenWithParameters(parameters, completionBlock = { token, error ->
            if (error != null) {
                continuation.resumeWithException(AuthenticationError(error.localizedDescription))
            } else {
                continuation.resume(token)
            }
        })
    }
}

suspend fun MSALPublicClientApplication.acquireTokenSilentWithParameters(
    parameters: MSALSilentTokenParameters
): MSALResult? = suspendCoroutine { continuation ->
    acquireTokenSilentWithParameters(parameters, completionBlock = { token, error ->
        if (error != null) {
            continuation.resumeWithException(AuthenticationError(error.localizedDescription))
        } else {
            continuation.resume(token)
        }
    })
}
