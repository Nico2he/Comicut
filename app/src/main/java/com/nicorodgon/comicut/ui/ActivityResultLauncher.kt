package com.nicorodgon.comicut.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ActionCodeSettings
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.nicorodgon.comicut.R

abstract class ActivityResultLauncher: AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_ui)
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            IdpConfig.EmailBuilder().build(),
            IdpConfig.PhoneBuilder().build(),
            IdpConfig.GoogleBuilder().build(),
            IdpConfig.FacebookBuilder().build(),
            IdpConfig.TwitterBuilder().build())

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
        } else {
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
            }
    }

    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
            }
    }

    private fun privacyAndTerms() {
        val providers = emptyList<IdpConfig>()
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTosAndPrivacyPolicyUrls(
                "https://example.com/terms.html",
                "https://example.com/privacy.html")
            .build()
        signInLauncher.launch(signInIntent)
    }

    open fun emailLink() {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(
                "...",
                true,
                null)
            .setHandleCodeInApp(true) // This must be set to true
            .setUrl("https://google.com") // This URL needs to be whitelisted
            .build()

        val providers = listOf(
            IdpConfig.EmailBuilder()
                .enableEmailLinkSignIn()
                .setActionCodeSettings(actionCodeSettings)
                .build()
        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    open fun catchEmailLink() {
        val providers: List<IdpConfig> = emptyList()
        if (AuthUI.canHandleIntent(intent)) {
            val extras = intent.extras ?: return
            val link = extras.getString("email_link_sign_in")
            if (link != null) {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setEmailLink(link)
                    .setAvailableProviders(providers)
                    .build()
                signInLauncher.launch(signInIntent)
            }
        }
    }
}