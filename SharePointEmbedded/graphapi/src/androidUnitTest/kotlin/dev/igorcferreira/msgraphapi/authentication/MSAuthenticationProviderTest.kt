package dev.igorcferreira.msgraphapi.authentication

import android.app.Activity
import android.content.Context
import dev.mokkery.MockMode
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

class MSAuthenticationProviderTest {
    class TestRuntimeException : RuntimeException()
    class TestCaseActivity : Activity() {
        override fun getApplicationContext(): Context = mockk()
        override fun getBaseContext(): Context = mockk()
    }

    @Test
    fun `when the application is not created, no other call is made`() = runTest {
        val authenticator = mock<MSAuthenticationProvider>(MockMode.original)

        try {
            authenticator.getToken()
            fail("The authentication should have failed without an application")
        } catch (e: Exception) {
            assertIs<RuntimeException>(e)
            assertEquals("Authentication requires an attached activity", e.message)
        }
    }

    @Test
    fun `with an application, the inner methods are called`() = runTest {
        val activity = TestCaseActivity()
        val authenticator = mock<MSAuthenticationProvider>(MockMode.original) {
            everySuspend { makeApplication(any()) } throws TestRuntimeException()
        }

        try {
            authenticator.attachedToActivity(activity)
            authenticator.getToken()
        } catch (e: Exception) {
            assertIs<TestRuntimeException>(e)
        }
    }
}
