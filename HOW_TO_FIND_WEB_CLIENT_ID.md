# How to Find Web Client ID - Step by Step Guide

## Method 1: Google Cloud Console (Recommended) ⭐

### Step 1: Open Google Cloud Console
- Go to https://console.cloud.google.com/
- Make sure you're logged in with the same Google account as your Firebase project

### Step 2: Select Your Firebase Project
- At the top, click the **Project dropdown**
- Select your project (e.g., "neet-prep-xxxxx")

### Step 3: Navigate to Credentials
- Left sidebar → Click **APIs & Services**
- Click **Credentials**

### Step 4: Find OAuth 2.0 Client IDs
- Look for the section **"OAuth 2.0 Client IDs"**
- Find the client with type **"Web application"** (not Android, iOS, or other types)
- Click on it to open details

### Step 5: Copy the Client ID
- In the modal/page that opens, you'll see:
  - Client ID (This is what you need!) 📋
  - Client Secret
  - Authorized JavaScript origins
  - Authorized redirect URIs
- **Copy the Client ID value**

### Example Client ID Format:
```
123456789-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com
```

---

## Method 2: Firebase Console (Alternate)

### Step 1: Open Firebase Console
- Go to https://console.firebase.google.com/
- Select your project

### Step 2: Project Settings
- Click the **gear icon** (⚙️) next to "Project Overview"
- Click **Project Settings**

### Step 3: Go to Integrations Tab
- Click on the **"Integrations"** tab
- Look for **"Google Cloud"** section

### Step 4: Link to Google Cloud
- Click on the Google Cloud project link
- This takes you to Google Cloud Console
- Follow **Method 1, Step 3-5** from here

---

## Where to Paste the Client ID

Once you have the **Web Client ID**, paste it in:

**File:** `app/src/main/java/com/vayu/neetprep/firebase/FirebaseAuthManager.kt`

**Line:** In the `setupGoogleSignIn()` function:

```kotlin
private fun setupGoogleSignIn() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("PASTE_YOUR_WEB_CLIENT_ID_HERE") ← Replace this!
        .requestEmail()
        .build()

    googleSignInClient = GoogleSignIn.getClient(context, gso)
}
```

**Replace:** `"PASTE_YOUR_WEB_CLIENT_ID_HERE"`

**With:** Your actual Client ID (the long string ending with `.apps.googleusercontent.com`)

---

## ⚠️ Important Notes

1. **Web Client ID only** - Make sure you copy from "Web application" OAuth client, NOT from:
   - Android OAuth 2.0
   - iOS OAuth 2.0
   - Service Account

2. **Keep it safe** - This Client ID is public (used in browser), but keep it secure

3. **One per project** - Usually there's only one Web OAuth 2.0 client. If not, use the one created by Firebase

4. **Don't confuse with:**
   - API Key (different, shorter)
   - Service Account Key (JSON file)
   - Android SHA-1 fingerprint

---

## Troubleshooting

### Q: I don't see "Web application" in OAuth 2.0 Client IDs
**A:** You might need to create one:
1. Go to Google Cloud Console → Credentials
2. Click **Create Credentials** → **OAuth 2.0 Client ID**
3. Application type: **Web application**
4. Name it something like "Firebase Web Client"
5. Click Create
6. Copy the generated Client ID

### Q: I see Service Accounts but no OAuth 2.0 Client IDs
**A:** Service Accounts are for backend/server authentication, NOT for client apps. Look for the "OAuth 2.0 Client IDs" section instead.

### Q: Multiple OAuth 2.0 clients, which one to use?
**A:** Use the one created by Firebase automatically, or the one with "Web application" type.

---

## Quick Reference

```
Google Cloud Console
    ↓
APIs & Services → Credentials
    ↓
OAuth 2.0 Client IDs (Web application)
    ↓
Copy Client ID
    ↓
Paste in FirebaseAuthManager.kt
```

---

## Verification Checklist
- [ ] Logged into Google Cloud Console with correct account
- [ ] Selected correct Firebase project
- [ ] Found "OAuth 2.0 Client IDs" section
- [ ] Selected "Web application" type client
- [ ] Copied the Client ID (ends with .apps.googleusercontent.com)
- [ ] Updated FirebaseAuthManager.kt with the Client ID
- [ ] Saved the file
