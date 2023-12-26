package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.model.User
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentSearch : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var searchUserAdapter: SearchUserAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch)
        recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())

        val userList: MutableList<User> = mutableListOf()

        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val uid = snapshot.child("uid").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)
                    val username = snapshot.child("username").getValue(String::class.java)

                    if (uid != null && email != null && username != null) {
                        val user = User(uid, email, username)
                        userList.add(user)
                    }
                }
                // Setelah mendapatkan daftar pengguna, inisialisasikan adapter dan RecyclerView
                searchUserAdapter = SearchUserAdapter(userList) { user ->
                    val intent = Intent(requireContext(), UserLainActivity::class.java)
                    intent.putExtra("userId", user.uid)
                    requireActivity().startActivity(intent)
                }
                recyclerViewSearch.adapter = searchUserAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
            }
        })
        return view

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSearch().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}