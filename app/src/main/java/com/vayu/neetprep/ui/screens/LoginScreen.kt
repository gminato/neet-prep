package com.vayu.neetprep.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.vayu.neetprep.firebase.FirebaseAuthManager
import com.vayu.neetprep.ui.theme.BackgroundGray
import com.vayu.neetprep.ui.theme.TealPrimary
import com.vayu.neetprep.ui.theme.TextDark
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSkip: () -> Unit) {
    var mobileNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showOTPScreen by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authManager = remember { FirebaseAuthManager(context) }
    val credentialManager = remember { CredentialManager.create(context) }

    fun findActivity(context: Context): Activity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is Activity) return currentContext
            currentContext = currentContext.baseContext
        }
        return null
    }

    /**
     * Authenticate with Firebase using Google ID Token
     */
    fun firebaseAuthWithGoogle(idToken: String) {
        scope.launch {
            val authResult = authManager.handleGoogleSignInResult(idToken)
            authResult.onSuccess {
                Log.d("LoginScreen", "Firebase sign-in successful")
                onLoginSuccess()
            }.onFailure {
                Log.e("LoginScreen", "Firebase sign-in failed: ${it.message}", it)
                errorMessage = "Firebase Error: ${it.message}"
                isLoading = false
            }
        }
    }

    /**
     * Handle the sign-in credential
     */
    fun handleSignInCredential(credential: androidx.credentials.Credential) {
        when {
            // Handle Google ID Token credential from GetSignInWithGoogleOption or GetGoogleIdOption
            credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    // Create Google ID Token Credential from the data
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    Log.d("LoginScreen", "Google ID Token retrieved successfully from CustomCredential")
                    firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Error creating GoogleIdTokenCredential", e)
                    errorMessage = "Error processing Google credential: ${e.message}"
                    isLoading = false
                }
            }
            // Direct GoogleIdTokenCredential (can happen with some credential flows)
            credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    Log.d("LoginScreen", "Google ID Token retrieved successfully")
                    firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Error creating GoogleIdTokenCredential", e)
                    errorMessage = "Error processing Google credential: ${e.message}"
                    isLoading = false
                }
            }
            else -> {
                Log.w("LoginScreen", "Credential is not of type Google ID Token! Type: ${credential.type}")
                errorMessage = "Unsupported credential type: ${credential.type}"
                isLoading = false
            }
        }
    }

    fun performGoogleSignIn() {
        val activity = findActivity(context)
        if (activity == null) {
            errorMessage = "Error: Activity not found"
            return
        }

        isLoading = true
        errorMessage = "" // Clear previous error
        
        scope.launch {
            try {
                Log.d("LoginScreen", "Starting Google Sign-In request...")

                // Get web client ID from resources (auto-generated from google-services.json)
                val webClientId = context.getString(com.vayu.neetprep.R.string.default_web_client_id)
                Log.d("LoginScreen", "Using Web Client ID: $webClientId")

                // Use GetSignInWithGoogleOption - shows the Google Sign-In button directly
                // This is more reliable than GetGoogleIdOption for first-time sign-ins
                val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(webClientId)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(signInWithGoogleOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )

                val credential = result.credential
                Log.d("LoginScreen", "Received credential type: ${credential.type}")

                // Handle the credential using proper type checking
                handleSignInCredential(credential)
            } catch (e: NoCredentialException) {
                Log.e("LoginScreen", "NoCredentialException - no Google accounts found", e)
                errorMessage = "No Google accounts found on this device.\n\nPlease:\n1. Add a Google account in Settings\n2. Ensure you're connected to the internet\n3. Try again"
                isLoading = false
            } catch (e: GetCredentialCancellationException) {
                Log.e("LoginScreen", "User cancelled sign-in", e)
                errorMessage = "Sign-in cancelled."
                isLoading = false
            } catch (e: GetCredentialException) {
                Log.e("LoginScreen", "GetCredentialException", e)
                Log.e("LoginScreen", "Error type: ${e.type}")
                Log.e("LoginScreen", "Error message: ${e.message}")
                e.printStackTrace()

                val detailedMessage = when {
                    e.message?.contains("16") == true -> "Configuration error. Please check SHA-1 fingerprint in Firebase Console."
                    e.message?.contains("10") == true -> "Developer error. Check Web Client ID configuration."
                    else -> e.message ?: "Unknown error"
                }

                errorMessage = "Google Sign-In Error:\n$detailedMessage\n\nTroubleshooting:\n1. Add SHA-1 to Firebase\n2. Download fresh google-services.json\n3. Rebuild app"
                isLoading = false
            } catch (e: Exception) {
                Log.e("LoginScreen", "Unexpected error during Google Sign-In", e)
                e.printStackTrace()
                errorMessage = "Unexpected error: ${e.message}\n\nPlease check logcat for details."
                isLoading = false
            }
        }
    }


    if (showOTPScreen) {
        OTPVerificationScreen(
            phoneNumber = mobileNumber,
            verificationId = verificationId,
            onOTPEntered = { otpCode ->
                isLoading = true
                scope.launch {
                    val result = authManager.verifyPhoneNumberWithCode(verificationId, otpCode)
                    result.onSuccess {
                        onLoginSuccess()
                    }.onFailure {
                        errorMessage = "OTP verification failed: ${it.message}"
                        isLoading = false
                    }
                }
            },
            onBack = { showOTPScreen = false },
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    } else {
        PhoneLoginScreen(
            mobileNumber = mobileNumber,
            onMobileNumberChange = { mobileNumber = it },
            onSendOTP = {
                val activity = findActivity(context)
                if (activity == null) {
                    errorMessage = "Error: Activity not found"
                    return@PhoneLoginScreen
                }
                isLoading = true
                val phoneNumberFull = "+91$mobileNumber"
                scope.launch {
                    val result = authManager.startPhoneNumberVerification(
                        phoneNumberFull,
                        activity
                    )
                    result.onSuccess { verificationIdResult ->
                        verificationId = verificationIdResult
                        showOTPScreen = true
                        isLoading = false
                    }.onFailure {
                        errorMessage = "Failed to send OTP: ${it.message}"
                        isLoading = false
                    }
                }
            },
            onGoogleSignIn = {
                performGoogleSignIn()
            },
            onSkip = onSkip,
            isLoading = isLoading,
            errorMessage = errorMessage
        )
    }
}

@Composable
fun PhoneLoginScreen(
    mobileNumber: String,
    onMobileNumberChange: (String) -> Unit,
    onSendOTP: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onSkip: () -> Unit,
    isLoading: Boolean,
    errorMessage: String
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Top Skip Button
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable { onSkip() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Skip for now",
                        color = TealPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Waving Hand Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFE0F2F1), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WavingHand,
                    contentDescription = null,
                    tint = TealPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter your mobile number to access your personalized learning plan.",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = "Mobile Number",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                // Country Code
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🇮🇳", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+91", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Mobile Input
                OutlinedTextField(
                    value = mobileNumber,
                    onValueChange = { if (it.length <= 10) onMobileNumberChange(it) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = { Text("98765 43210", color = Color.LightGray) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    trailingIcon = {
                        if (mobileNumber.isNotEmpty()) {
                            IconButton(onClick = { onMobileNumberChange("") }) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.LightGray)
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = TealPrimary,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                    ),
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onSendOTP,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp),
                enabled = mobileNumber.length == 10 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Send OTP",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                Text(
                    text = "OR",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Google Sign In Button
            OutlinedButton(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark),
                enabled = !isLoading
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Google Icon Placeholder (could use a Painter if we had the resource)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.White)
                    ) {
                        Text("G", fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Continue with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Terms and Privacy
            Text(
                text = buildAnnotatedString {
                    append("By continuing, you agree to our ")
                    withStyle(style = SpanStyle(color = TealPrimary, textDecoration = TextDecoration.Underline)) {
                        append("Terms of Service")
                    }
                    append(" and ")
                    withStyle(style = SpanStyle(color = TealPrimary, textDecoration = TextDecoration.Underline)) {
                        append("Privacy Policy")
                    }
                },
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun OTPVerificationScreen(
    phoneNumber: String,
    verificationId: String,
    onOTPEntered: (String) -> Unit,
    onBack: () -> Unit,
    isLoading: Boolean,
    errorMessage: String
) {
    var otpValue by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Verify Phone",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We've sent a 6-digit code to +91 $phoneNumber",
                fontSize = 16.sp,
                color = Color.Gray,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 6) otpValue = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onOTPEntered(otpValue) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(12.dp),
                enabled = otpValue.length == 6 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verify & Continue", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = !isLoading
            ) {
                Text("Change Phone Number", color = TealPrimary)
            }
        }
    }
}
