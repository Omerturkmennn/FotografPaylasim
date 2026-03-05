package com.example.fotografpaylasma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fotografpaylasma.R
import com.example.fotografpaylasma.adapter.PostAdapter
import com.example.fotografpaylasma.databinding.FragmentFeedBinding
import com.example.fotografpaylasma.model.Post
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.ArrayList

class FeedFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    val postList: ArrayList<Post> = arrayListOf()

    private lateinit var db: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var popup : PopupMenu
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private var adapter : PostAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= Firebase.auth
        db= Firebase.firestore

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener { floatingActionButtonTiklandi(it) }

        popup= PopupMenu(requireContext(),binding.floatingActionButton)
        val inflater=popup.menuInflater
        inflater.inflate(R.menu.my_popup_menu,popup.menu)
        popup.setOnMenuItemClickListener(this)
        firestoreVeriAl()
        adapter= PostAdapter(postList)
        binding.feedRecyclerView.layoutManager= LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter=adapter
    }

    private fun firestoreVeriAl(){
        db.collection("Posts").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT).show()
            }else{
                if (value!=null){
                    if(!value.isEmpty){
                        postList.clear()
                        val documents=value.documents
                        for(document in documents){
                            val comment=document.get("comment") as String
                            val email=document.get("email") as String
                            val downloadUrl=document.get("downloadUrl") as String

                            val post= Post(email, comment, downloadUrl)
                            postList.add(post)
                        }
                        adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    fun floatingActionButtonTiklandi (view: View){

        popup.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.yuklemeItem) {
            val action = FeedFragmentDirections.actionFeedFragmentToYuklemeFragment()
            Navigation.findNavController(requireView()).navigate(action)

        } else if (item?.itemId == R.id.cikisItem) {
            auth.signOut()
            val action = FeedFragmentDirections.actionFeedFragmentToKayitFragment()
            Navigation.findNavController(requireView()).navigate(action)

        }
        return true
    }
}