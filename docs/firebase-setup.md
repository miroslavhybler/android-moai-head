# Firebase & Firestore Setup Guide

This document explains how to set up Firebase for this project, configure Firestore, and understand the data structure and security considerations.
It is intended for developers building the app **with full Firestore support enabled**.

---

## 1. Create Your Firebase Project

Follow these official guides:

* **Create a Firebase project:**
  [https://firebase.google.com/docs/projects/learn-more](https://firebase.google.com/docs/projects/learn-more)
* **Add Firebase to your Android app:**
  [https://firebase.google.com/docs/android/setup](https://firebase.google.com/docs/android/setup)
* **Enable Anonymous Authentication:**
  [https://firebase.google.com/docs/auth/android/anonymous-auth](https://firebase.google.com/docs/auth/android/anonymous-auth)

After setup, place your generated `google-services.json` inside:

```
app/src/google-services.json
```

> **Never commit this file to git.**

---

## 2. Firestore Setup

### Enable Firestore

Go to: **Firebase Console → Firestore Database → Create Database**
Choose *Production mode* and select your region.

---

## 3. Firestore Data Structure

The app uses a single-user Firestore database.

### **Collection: `mood`**

Each mood entry is stored inside this collection.
Document ID is the **timestamp of the record**, formatted as a unique string (e.g. Unix epoch).

#### **Document Example**

```
mood
└── 1709389200000          // timestamp as document ID
    ├── mood: 7            // int (1–10)
    ├── timestamp: 1709389200000
    ├── note: "Good meal"  // optional string
    └── source: 1       // int enum (define app-specific values)
```

### Fields

| Field       | Type    | Description                       |
|-------------|---------|-----------------------------------|
| `mood`      | int     | Mood value 1–10 by Mood Interface |
| `timestamp` | long    | When the mood was recorded        |
| `note`      | string? | Optional “cause” or note          |
| `source`    | int     | Value by EntrySource Interface    |

---

## 4. Recommended Security Rules

You should *not* leave your database public.

For a single-user app using **Anonymous Auth**, use:

```yaml
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {
    match /mood/{documentId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

This ensures:

* Only authenticated sessions (including anonymous users) may access data.
* Public access is blocked.

---

## 5. Dangers, Secrets & Data Safety

### **google-services.json**

* This file contains Firebase API keys, project IDs, and OAuth identifiers.
* It **must not be committed** to Git.
* Never publish it in your repository.
* Anyone who obtains it could write to your database (depending on your rules).

Add to `.gitignore`:
```
google-services.json
*.keystore
```

### **Possible Risk Scenarios**

* **Leaking `google-services.json`:**
  Someone can connect their app instance and read/write your mood data.
* **Overly permissive Firestore rules:**
  Rules like `allow read, write: if true` make your entire DB public.
* **Publishing debug builds with wrong rules:**
  This could expose your database if using open testing channels.
* **Compromised device:**
  Any app using your Firebase config can access the DB *within the allowed rules*.

### **Mitigations**

* Use **Anonymous Auth** and require `request.auth != null`.
* Keep `google-services.json` private.
* Never allow full public rules.
* Periodically review rules in Firebase Console.