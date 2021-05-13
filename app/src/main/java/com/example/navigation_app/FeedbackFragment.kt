package com.example.navigation_app

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.navigation_app.viewmodel.loginViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class FeedbackFragment : Fragment() {

    val db = Firebase.firestore
    var star = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_feedback, container, false)
        val comm = root.findViewById<TextView>(R.id.feedback_com)
        val star1 = root.findViewById<ImageView>(R.id.star1)
        val star2 = root.findViewById<ImageView>(R.id.star2)
        val star3 = root.findViewById<ImageView>(R.id.star3)

        val btn = root.findViewById<Button>(R.id.feedback_submit)
        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)

        btn.isEnabled = false

        db.collection("feedback").document(viewModel.id.toString())
            .get()
            .addOnSuccessListener {
                val com = it.getField<String>("comment").toString()
                val star = it.getField<String>("star").toString()

                if (com != "null" || com != "") {
                    comm.text = com
                }
                if (star != "null" || star != "") {
                    if (star == "1") {
                        star1.setImageResource(R.drawable.ic_baseline_star_24)
                    } else if (star == "2") {
                        star1.setImageResource(R.drawable.ic_baseline_star_24)
                        star2.setImageResource(R.drawable.ic_baseline_star_24)
                    } else if (star == "3") {
                        star1.setImageResource(R.drawable.ic_baseline_star_24)
                        star2.setImageResource(R.drawable.ic_baseline_star_24)
                        star3.setImageResource(R.drawable.ic_baseline_star_24)
                    }
                }
            }

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val star1 = view.findViewById<ImageView>(R.id.star1)
        val star2 = view.findViewById<ImageView>(R.id.star2)
        val star3 = view.findViewById<ImageView>(R.id.star3)
        val btn = view.findViewById<Button>(R.id.feedback_submit)
        val com = view.findViewById<TextView>(R.id.feedback_com)

        // get current user username
        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        d("bomoh", viewModel.id.toString() + "dashboard")

        star1.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24)
            star2.setImageResource(R.drawable.ic_baseline_star_border_24)
            star3.setImageResource(R.drawable.ic_baseline_star_border_24)
            star = 1
            btn.isEnabled = true
        }

        star2.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24)
            star2.setImageResource(R.drawable.ic_baseline_star_24)
            star3.setImageResource(R.drawable.ic_baseline_star_border_24)
            star = 2
            btn.isEnabled = true
        }

        star3.setOnClickListener {
            star1.setImageResource(R.drawable.ic_baseline_star_24)
            star2.setImageResource(R.drawable.ic_baseline_star_24)
            star3.setImageResource(R.drawable.ic_baseline_star_24)
            star = 3
            btn.isEnabled = true
        }

        btn.setOnClickListener {
            val data = hashMapOf(
                "star" to star.toString(),
                "comment" to com.text.toString()
            )

            db.collection("feedback").document(viewModel.id.toString())
                .set(data)
                .addOnSuccessListener {
                    d("bomoh", "feedback submitted")
                    Snackbar.make(view, "Feedback Submitted", Snackbar.LENGTH_SHORT).show()
                }
        }

    }
}