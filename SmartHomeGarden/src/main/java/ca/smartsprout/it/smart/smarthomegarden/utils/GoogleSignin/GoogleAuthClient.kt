/**
 * Smart Sprout
 * Members:
 * 1. Aditi Patel, n01525570, CENG322-RCB
 * 2. Birava Prajapati, n01579924, CENG322-RCA
 * 3. Darshankumar Prajapati, n01574247, CENG322-RCB
 * 4. Zeel Patel, n01526282, CENG322-RCB
 */

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
import com.google.firebase.firestore.FirebaseFirestore
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

                // Check if sign-in was successful
                val user = authResult.user
                if (user != null) {
                    val firestore = FirebaseFirestore.getInstance()
                    val userId = user.uid
                    val userDocRef = firestore.collection("users").document(userId)

                    // Check if user data already exists in Firestore
                    val userDocSnapshot = userDocRef.get().await()
                    if (!userDocSnapshot.exists()) {
                        // If user does not exist, store data
                        val newUser = hashMapOf(
                            "name" to (user.displayName ?: ""),
                            "email" to (user.email ?: ""),
                            "phoneNumber" to (user.phoneNumber ?: ""),
                            "password" to "",  // Leave blank as it's Google Sign-In
                            "confirmPassword" to ""  // Leave blank as it's Google Sign-In
                        )

                        // Store user data in Firestore
                        userDocRef.set(newUser).await()
                        Log.d("GoogleAuthClient", "User data stored in Firestore")
                    } else {
                        Log.d("GoogleAuthClient", "User already exists in Firestore")
                    }

                    return true
                } else {
                    Log.e("GoogleAuthClient", "Authentication failed: User is null")
                    return false
                }
            } catch (e: GoogleIdTokenParsingException) {
                Log.e("GoogleAuthClient", "Google ID Token Parsing Exception: ${e.message}")
                return false
            } catch (e: Exception) {
                Log.e("GoogleAuthClient", "Firestore Exception: ${e.message}")
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
                    .setServerClientId("204065205787-vk2dpcn1aj0tn12rtslrcg9nkc8vdjh4.apps.googleusercontent.com") // Replace with your Server Client ID
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(request = request, context = context)
    }
}
