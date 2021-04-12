package com.example.navigation_app.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.RestrictionEntry.TYPE_NULL
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_TEXT
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.navigation_app.R
import com.example.navigation_app.viewmodel.loginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    val db = Firebase.firestore

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
                ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
//        val textView: TextView = root.findViewById(R.id.text_dashboard)
        profileViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })

        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        d("bomoh", viewModel.id.toString() + "dashboard")

        // Disable text input
        val profileUsername : EditText = root.findViewById(R.id.profile_username)
        profileUsername.isFocusable = false
        profileUsername.isFocusableInTouchMode = true
        profileUsername.inputType = TYPE_NULL
        val profileEmail : EditText = root.findViewById(R.id.profile_email)
        profileEmail.isFocusable = false
        profileEmail.isFocusableInTouchMode = true
        profileEmail.inputType = TYPE_NULL
        val profilePhone : EditText = root.findViewById(R.id.profile_phone)
        profilePhone.isFocusable = false
        profilePhone.isFocusableInTouchMode = true
        profilePhone.inputType = TYPE_NULL

        // When Edit pressed
        val profile_edit : TextView = root.findViewById(R.id.profile_edit)
        profile_edit.setOnClickListener {
            if (profile_edit.text.toString() == "Edit") {
                profile_edit.text = "Save"
//                profileUsername.isFocusable = true
//                profileUsername.inputType = TYPE_CLASS_TEXT
                profileEmail.isFocusable = true
                profileEmail.inputType = TYPE_CLASS_TEXT
                profilePhone.isFocusable = true
                profilePhone.inputType = TYPE_CLASS_TEXT
            }

            else if (profile_edit.text.toString() == "Save") {
                profile_edit.text = "Edit"
//                profileUsername.isFocusable = false
//                profileUsername.isFocusableInTouchMode = true
                profileUsername.inputType = TYPE_NULL
                profileEmail.isFocusable = false
                profileEmail.isFocusableInTouchMode = true
                profileEmail.inputType = TYPE_NULL
                profilePhone.isFocusable = false
                profilePhone.isFocusableInTouchMode = true
                profilePhone.inputType = TYPE_NULL

                val data = hashMapOf(
                    "email" to profileEmail.text.toString(),
                    "phone" to profilePhone.text.toString()
                )

                db.collection("user").document(viewModel.id.toString())
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener {
                        Snackbar.make(requireView(), "Profile Updated", Snackbar.LENGTH_SHORT).show()
                    }

                closeKeyBoard(it)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ConstraintLayout>(R.id.profile_layout).setOnClickListener {
            closeKeyBoard(view)
        }

        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        db.collection("user").document(viewModel.id.toString())
            .get()
            .addOnSuccessListener {
                val username = it.getField<String>("username")
                val email = it.getField<String>("email")
                val phone = it.getField<String>("phone")
                view.findViewById<TextView>(R.id.profile_username).text = username.toString()
                if (email!= null) {
                    view.findViewById<TextView>(R.id.profile_email).text = email.toString()
                }
                if (phone != null) {
                    view.findViewById<TextView>(R.id.profile_phone).text = phone.toString()
                }
            }
    }

    private fun closeKeyBoard(v : View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}