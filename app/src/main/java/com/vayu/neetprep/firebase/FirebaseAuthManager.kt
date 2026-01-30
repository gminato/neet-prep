package com.vayu.neetprep.firebase

import android.app.Activity
import android.content.Context
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class FirebaseAuthManager(private val context: Context) {

    private val auth: FirebaseAuth = Firebase.auth

    /**
     * Get Google ID option for sign-in
     */
    fun getGoogleIdOption(): GetGoogleIdOption {
        // Get the Web Client ID from resources (auto-generated from google-services.json)
        val webClientId = context.getString(com.vayu.neetprep.R.string.default_web_client_id)

        return GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(true)  // Only show accounts previously used to sign in
            .setAutoSelectEnabled(false)  // Let user choose account
            .build()
    }

    /**
     * Handle Google Sign-In result with modern approach
     */
    suspend fun handleGoogleSignInResult(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val userId = authResult.user?.uid ?: return Result.failure(Exception("No user found"))
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Start phone number verification
     */
    suspend fun startPhoneNumberVerification(
        phoneNumber: String,
        activity: Activity
    ): Result<String> = suspendCancellableCoroutine { continuation ->
        try {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    signInWithCredentialAndContinue(credential, continuation)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    continuation.resume(Result.failure(e))
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    continuation.resume(Result.success(verificationId))
                }
            }

            PhoneAuthProvider.getInstance(auth).verifyPhoneNumber(
                phoneNumber,
                60, // timeout duration
                TimeUnit.SECONDS,
                activity,
                callbacks
            )
        } catch (e: Exception) {
            continuation.resume(Result.failure(e))
        }
    }

    private fun signInWithCredentialAndContinue(
        credential: PhoneAuthCredential,
        continuation: CancellableContinuation<Result<String>>
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""
                    continuation.resume(Result.success(userId))
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception("Sign in failed")))
                }
            }
    }

    /**
     * Verify OTP and sign in
     */
    suspend fun verifyPhoneNumberWithCode(
        verificationId: String,
        code: String
    ): Result<String> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val authResult = auth.signInWithCredential(credential).await()
            val userId = authResult.user?.uid ?: return Result.failure(Exception("No user found"))
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign out
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * Get current user
     */
    fun getCurrentUser() = auth.currentUser

    /**
     * Check if user is authenticated
     */
    fun isUserAuthenticated(): Boolean = auth.currentUser != null
}
