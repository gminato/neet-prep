# Quick Setup - Just Copy & Paste Your Client ID

## Your Web Client ID Location

You have the **correct client** ready:
```
Name: Web client (auto created by Google Service)
Type: Web application ✅
Client ID: 1060511695703-1d9...
```

## Next Steps:

### 1. Click on the Web Client Entry
In Google Cloud Console → Credentials, click on the **"Web client (auto created by Google Service)"** row

### 2. Copy the Full Client ID
In the modal that opens, you'll see the complete Client ID. It looks like:
```
1060511695703-1d9XXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com
```
Copy this **entire string**

### 3. Replace in FirebaseAuthManager.kt
Open: `app/src/main/java/com/vayu/neetprep/firebase/FirebaseAuthManager.kt`

Find this line (around line 27):
```kotlin
.requestIdToken("1060511695703-1d9...") // Replace this
```

Replace `1060511695703-1d9...` with your **full Client ID**

So it becomes:
```kotlin
.requestIdToken("1060511695703-1d9XXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com")
```

### 4. Save the File
That's it! Your Firebase authentication is now configured!

---

## ✅ Verification Checklist
- [ ] Clicked on "Web client (auto created by Google Service)"
- [ ] Copied the full Client ID (ends with .apps.googleusercontent.com)
- [ ] Updated FirebaseAuthManager.kt with the full Client ID
- [ ] Saved the file
- [ ] Ready to build and test!
