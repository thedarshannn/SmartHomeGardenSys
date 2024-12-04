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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GoogleSignInHelper(private val context: Context) {

    private val googleAuthClient = GoogleAuthClient(context)

    // Function to trigger Sign-In from Java
    fun signIn(onSignInResult: (Boolean) -> Unit) {
        Log.d("GoogleSignInHelper", "Starting Google Sign-In process")
        CoroutineScope(Dispatchers.Main).launch {
            val result = googleAuthClient.signIn()
            Log.d("GoogleSignInHelper", "Sign-In result: $result")
            onSignInResult(result)
        }
    }

    // Function to check if the user is already signed in
    fun isSignedIn(): Boolean {
        return googleAuthClient.isSignedIn()
    }

    // Function to trigger Sign-Out from Java
    fun signOut() {
        Log.d("GoogleSignInHelper", "Starting sign-out process")
        runBlocking {
            googleAuthClient.signOut()
        }
    }
}
