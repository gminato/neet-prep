# Firebase Authentication Integration Guide

## Setup Steps

### 1. Add google-services.json
- Download `google-services.json` from Firebase Console
- Place it in `app/` directory (already present in your project)

### 2. Update FirebaseAuthManager.kt
In `FirebaseAuthManager.kt`, replace `YOUR_WEB_CLIENT_ID` with your actual Web Client ID:

**To Find Web Client ID:**
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Select your Firebase project from the dropdown
3. Go to **Credentials** (Left sidebar → APIs & Services → Credentials)
4. Look for "OAuth 2.0 Client IDs" section
5. Find the entry with type "Web application" 
6. Click on it to see the Client ID
7. Copy the **Client ID** value

**Alternative Method:**
1. Firebase Console → Project Settings (gear icon)
2. Go to **Integrations** tab (or scroll down)
3. Look for "Google Cloud" section
4. Click on the Google Cloud project link
5. Go to Credentials page (as shown above)

**Then update FirebaseAuthManager.kt:**
```kotlin
.requestIdToken("YOUR_WEB_CLIENT_ID_HERE") // Replace with actual Web Client ID
```

Example of what it looks like:
```
123456789-abcdefghijk.apps.googleusercontent.com
```

### 3. Enable Authentication Methods in Firebase Console
- Go to Firebase Console → Authentication → Sign-in method
- Enable "Phone Number" authentication
- Enable "Google" authentication

### 4. Configure SHA-1 Fingerprint (for Google Sign-In)
```bash
# Get your SHA-1 fingerprint
./gradlew signingReport
```
Add the SHA-1 to Firebase Console:
- Project Settings → Your apps → Android app
- Add SHA-1 under "SHA certificate fingerprints"

### 5. Gradle Dependencies
All dependencies have been added to:
- `gradle/libs.versions.toml` - Firebase, Google Services versions
- `app/build.gradle.kts` - Firebase Auth, Google Play Services Auth

### 6. Manifest Permissions
Added to `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## File Changes Summary

### Created Files:
1. **FirebaseAuthManager.kt** - Core Firebase authentication handler
   - Google Sign-In integration
   - Phone Number OTP verification
   - Credential management

### Modified Files:
1. **LoginScreen.kt** - Enhanced with Firebase integration
   - Phone login with OTP flow
   - Google Sign-In flow
   - Error handling and loading states
   - New composables: `PhoneLoginScreen`, `OTPVerificationScreen`

2. **app/build.gradle.kts**
   - Added Google Services plugin
   - Added Firebase Auth dependency
   - Added Google Play Services Auth dependency

3. **gradle/libs.versions.toml**
   - Added Firebase version 33.1.0
   - Added Google Services plugin version 4.4.2

4. **AndroidManifest.xml**
   - Added INTERNET permission

## Usage Flow

### Phone Authentication:
1. User enters 10-digit phone number
2. Click "Send OTP" → Firebase sends OTP
3. Receive verification ID
4. User enters 6-digit OTP
5. Verify OTP → Sign in successful

### Google Authentication:
1. Click "Continue with Google"
2. Select Google account
3. Firebase receives ID token
4. Sign in successful

## Error Handling
- Network errors during OTP sending
- Invalid OTP verification
- Google sign-in cancellation/failure
- User-friendly error messages displayed

## Important Notes
1. **Web Client ID**: Must be configured in `FirebaseAuthManager.kt`
2. **Phone Auth**: Works best with physical devices/emulators with Google Play Services
3. **SHA-1**: Required for Google Sign-In to work properly
4. **Testing**: Use Firebase emulator for development testing

## Testing Checklist
- [ ] Firebase Console configured with both providers
- [ ] google-services.json in app/ directory
- [ ] Web Client ID updated in FirebaseAuthManager
- [ ] SHA-1 fingerprint added to Firebase Console
- [ ] Build and run on emulator/device
- [ ] Test phone OTP flow
- [ ] Test Google Sign-In flow
