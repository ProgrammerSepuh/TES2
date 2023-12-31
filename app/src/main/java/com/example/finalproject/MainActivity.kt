package com.example.finalproject

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.FragmentHome
import com.example.finalproject.FragmentExplore
import com.example.finalproject.FragmentSearch
import com.example.finalproject.FragmentProfile
import android.content.Intent
import com.example.finalproject.UploadActivity
import com.example.finalproject.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if the activity is started from LoginActivity
        if (intent.hasExtra("fromLogin")) {
            // Set FragmentHome as the default fragment after the intent from LoginActivity
            val homeFragment = FragmentHome()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, homeFragment)
                commit()
            }
        } else {
            // If not started from LoginActivity, set the default fragment as needed
            // For example, you can set the default fragment here
        }

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            // Start the UploadActivity when the FAB is clicked
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }


//        val fab: View = findViewById(R.id.fab)
//        fab.setOnClickListener {
//            val homeFragment = FragmentHome()
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.fragmentContainer, homeFragment)
//                commit()
//            }
//        }

        // Set listener for fragmentHome ImageButton
        val fragmentHomeButton: ImageButton = findViewById(R.id.fragmentHome)
        fragmentHomeButton.setOnClickListener {
            val homeFragment = FragmentHome()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, homeFragment)
                commit()
            }
        }

        val fragmentExploreButton: ImageButton = findViewById(R.id.fragmentExplore)
        fragmentExploreButton.setOnClickListener {
            val exploreFragment = FragmentExplore()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, exploreFragment)
                commit()
            }
        }

        val fragmentSearchButton: ImageButton = findViewById(R.id.fragmentSearch)
        fragmentSearchButton.setOnClickListener {
            val searchFragment = FragmentSearch()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, searchFragment)
                commit()
            }
        }

        val fragmentProfileButton: ImageButton = findViewById(R.id.fragmentProfile)
        fragmentProfileButton.setOnClickListener {
            val profileFragment = FragmentProfile()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, profileFragment)
                commit()
            }
        }
    }
}
