package ca.smartsprout.it.smart.smarthomegarden.ui.GoogleSignin

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthClient(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // Suspend functions to be called with CoroutineScope from Java
    suspend fun signIn(): Boolean {
        try {
            Log.d("GoogleAuthClient", "Building credential request")
            val result = buildCredentialRequest()
            return handleSignIn(result)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GoogleAuthClient", "Exception during sign-in: ${e.message}")
            if (e is CancellationException) throw e
            return false
        }
    }

    suspend fun signOut() {
        Log.d("GoogleAuthClient", "Signing out")
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        firebaseAuth.signOut()
    }

    private suspend fun handleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                Log.d("GoogleAuthClient", "Parsing Google ID Token")
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                return authResult.user != null
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("GoogleAuthClient", "Google ID Token Parsing Exception: ${e.message}")
                return false
            }
        } else {
            Log.e("GoogleAuthClient", "Invalid credential type: ${credential?.type}")
        }
        return false
    }

    private suspend fun buildCredentialRequest(): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("204065205787-1g7a97om3768e9lp66bopohr43mia4pr.apps.googleusercontent.com") // Replace with your Server Client ID
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(request = request, context = context)
    }
}
