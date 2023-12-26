package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.Upload
import com.google.firebase.database.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentProfile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private var username: String? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var imageViewProfile: ImageView
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
            .child(firebaseAuth.currentUser?.uid ?: "")

        setContentView(R.layout.fragment_profile)
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
        val btnUp: Button = view.findViewById(R.id.btnUpdate)
        btnUp.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
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