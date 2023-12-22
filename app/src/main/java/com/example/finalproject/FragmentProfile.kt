package com.example.finalproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
            .child(firebaseAuth.currentUser?.uid ?: "")
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Get the current user
        val currentUser: FirebaseUser? = firebaseAuth.currentUser

        // Get user information
        val userEmail = currentUser?.email

        // Update TextViews
        val userEmailTextView = view.findViewById<TextView>(R.id.userEmailTextView)
        val usernameTextView = view.findViewById<TextView>(R.id.userIdTextView)

        userEmailTextView.text = userEmail

        // Fetch username from Firebase and update TextView
        userRef.child("username").get().addOnSuccessListener { snapshot ->
            val username = snapshot.value as? String
            usernameTextView.text = username
        }.addOnFailureListener {
            // Handle failure
        }

        // Display the username from SignUpActivity if available
        username?.let {
            usernameTextView.text = it
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
