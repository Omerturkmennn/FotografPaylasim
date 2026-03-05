package com.example.fotografpaylasma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.fotografpaylasma.databinding.FragmentKayitBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class KayitFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private var _binding: FragmentKayitBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth= Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKayitBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { kayitOl(it) }
        binding.girisButton.setOnClickListener { girisYap(it) }
        val guncelKullanici=auth.currentUser
        if(guncelKullanici !=null){
            val action= KayitFragmentDirections.actionKayitFragmentToFeedFragment()
            Navigation.findNavController(view).navigate(action)
        }

    }

    fun kayitOl(view: View){

        val email=binding.emailText.text.toString()
        val password=binding.passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){ //kullanıcı oluşturuldu
                    val action= KayitFragmentDirections.actionKayitFragmentToFeedFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText( requireContext(),exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun girisYap(view: View){
        val email=binding.emailText.text.toString()
        val password=binding.passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val action= KayitFragmentDirections.actionKayitFragmentToFeedFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(),exception.localizedMessage, Toast.LENGTH_SHORT).show() }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}