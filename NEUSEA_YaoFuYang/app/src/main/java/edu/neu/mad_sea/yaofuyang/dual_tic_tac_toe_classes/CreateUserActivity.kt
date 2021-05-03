package edu.neu.mad_sea.yaofuyang.dual_tic_tac_toe_classes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import edu.neu.mad_sea.yaofuyang.R

private const val TAG = "DualTicTacToeActivity"

private val database = FirebaseDatabase.getInstance()
private lateinit var auth : FirebaseAuth
private lateinit var signIn : Button
private lateinit var customToken : String
private lateinit var usernamePrompt : TextView
private lateinit var username : EditText
private lateinit var submitButton : Button
private lateinit var deleteButton: Button
private lateinit var fcmToken : String

class CreateUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        // Initialize Firebase Auth
        auth = Firebase.auth
        signIn = findViewById(R.id.create_anon_account)
        usernamePrompt = findViewById(R.id.user_sign_in_prompt)
        username = findViewById(R.id.username_box)
        submitButton = findViewById(R.id.submit_button)
        deleteButton = findViewById(R.id.delete_account_button)

        // Create anonymous account without need for password.
        signIn.setOnClickListener {
            signInAnonymously()
        }

        // Submit a custom username after creating account.
        submitButton.setOnClickListener {
            val user = username.text.toString()
            // TODO: Need to include constraints for username.
            print("Username token is: $user")
            if (user != "") {
                customToken = user
                updateUsername()
            }
        }

        // Delete the current user's account, including key-value saved in database.
        deleteButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val name = FirebaseAuth.getInstance().currentUser?.displayName
                FirebaseAuth.getInstance().currentUser?.delete()
                if (name != null) {
                    val userRef = database.getReference("users")
                    val user = userRef.child(name)
                    user.removeValue()
                }
            }
        }
    }

    // Displays whether user is already signed in/not signed in.
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    // Performs an anonymous sign-in through the realtime database.
    private fun signInAnonymously() {
        auth.signInAnonymously().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUI(user)
            } else {
                Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    // Update the username for the anonymous account.
    // Creates mapping inside database under 'users'
    private fun updateUsername() {
        val userRef = database.getReference("users")

        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(baseContext, "Please create anonymous account first.",
                Toast.LENGTH_SHORT).show()
            return
        }
        val profileUpdates = userProfileChangeRequest {
            fcmToken = FirebaseInstanceId.getInstance().getToken().toString()
            displayName = customToken
            userRef.child(customToken).setValue(fcmToken)
//            userRef.child(customToken).setValue(user.uid)
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Updated username name to $customToken.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Displays Toasts to reflect whether or not user has signed in.
    private fun updateUI(account : FirebaseUser?) {
        if (account != null) {
            val name = FirebaseAuth.getInstance().currentUser?.displayName
            Toast.makeText(this,"Signed in as $name",Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this,"Not signed in",Toast.LENGTH_LONG).show()
        }
    }
}