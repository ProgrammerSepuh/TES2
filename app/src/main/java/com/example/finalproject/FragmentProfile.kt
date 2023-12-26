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
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var view: View
    private lateinit var textViewUsername: TextView
    private lateinit var textViewEmail: TextView

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentProfile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().reference.child("users")
            .child(firebaseAuth.currentUser?.uid ?: "")
        database = FirebaseDatabase.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_profile, container, false)

        val userEmailTextView = view.findViewById<TextView>(R.id.userEmailTextView)
        val usernameTextView = view.findViewById<TextView>(R.id.userIdTextView)

        val currentUser: FirebaseUser? = firebaseAuth.currentUser
        val userEmail = currentUser?.email
        userEmail?.let {
            userEmailTextView.text = it
        }

        userRef.child("username").get().addOnSuccessListener { snapshot ->
            val username = snapshot.value as? String
            username?.let {
                usernameTextView.text = it
            }
        }.addOnFailureListener {
            // Handle failure
        }

        // Fetch and display user images
        fetchUserImages(currentUser?.uid ?: "")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyProfil)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        textViewUsername = view.findViewById(R.id.userIdTextView)
        textViewEmail = view.findViewById(R.id.userEmailTextView)

        userId?.let { uid ->
            val userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.exists()) {
                        val username = userSnapshot.child("username").getValue(String::class.java)
                        val email = userSnapshot.child("email").getValue(String::class.java)

                        username?.let {
                            textViewUsername.text = "@$it"
                        }

                        email?.let {
                            textViewEmail.text = it
                        }

                        fetchUserImages(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to read user data.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun fetchUserImages(uid: String) {
        val uploadsRef = database.reference.child("uploads").orderByChild("s").equalTo(uid)

        uploadsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList: MutableList<String> = mutableListOf()
                val descriptionList: MutableList<String> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    val description = snapshot.child("imageDescription").getValue(String::class.java)
                    imageUrl?.let {
                        imageList.add(it)
                        descriptionList.add(description ?: "")
                    }
                }

                val recyclerView: RecyclerView = view.findViewById(R.id.recyProfil)
                recyclerView.adapter = ImageUserAdapter(requireContext(), imageList, descriptionList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to read user images.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        userId?.let { uid ->
            userReference = database.reference.child("users").child(uid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()
                        val email = snapshot.child("email").value.toString()

                        textViewUsername.text = "@$username"
                        textViewEmail.text = "$email"

                        fetchUserImages(uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to read user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
