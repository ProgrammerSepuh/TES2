package com.example.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentExplore : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewImages)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // Adjust the number of columns as needed

        val imageUrlList: MutableList<String> = mutableListOf()

        storageReference = FirebaseStorage.getInstance().reference.child("images")

        // Ganti path sesuai dengan lokasi gambar di Firebase Storage
        storageReference.listAll().addOnSuccessListener { result ->
            for (imageRef in result.items) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    imageUrlList.add(imageUrl)

                    // Setelah mendapatkan daftar URL gambar, inisialisasikan adapter RecyclerView
                    imageAdapter = ImageAdapter(imageUrlList)
                    recyclerView.adapter = imageAdapter
                }.addOnFailureListener { exception ->
                    // Handle jika gagal mendapatkan URL gambar
                }
            }
        }.addOnFailureListener {
            // Handle jika gagal mendapatkan daftar gambar dari Firebase Storage
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentExplore.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentExplore().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}