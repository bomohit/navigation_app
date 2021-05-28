package com.example.navigation_app.ui.notifications

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navigation_app.R
import com.example.navigation_app.viewmodel.loginViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class TimeTableFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var notificationsViewModel: NotificationsViewModel
    val db = Firebase.firestore
    val classContent = mutableListOf<ClassContent>()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        d("bomoh", viewModel.id.toString() + "notification")
        val class_list: MutableList<String> = ArrayList()
        val class_spinner : Spinner = root.findViewById(R.id.class_spinner)
//        val recyclerView : RecyclerView = root.findViewById(R.id.class_recyclerView)



        class_spinner.onItemSelectedListener = this

        val adapter = ArrayAdapter(
            root.context,
            android.R.layout.simple_spinner_item,
            class_list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        db.collection("class").document("class_list")
            .get()
            .addOnSuccessListener {
                d("bomoh", "${it.get("list")}")
                class_list.addAll(it.get("list") as Collection<String>)
                d("bomoh", "classAll $class_list")
                d("bomoh", "class ${class_list[1]}")
                class_spinner.adapter = adapter
            }

        return root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position > 0) {
            d("bomoh", "${parent!!.getItemAtPosition(position)}, $id")

            db.collection("class_content").document(parent.getItemAtPosition(position).toString())
                .get()
                .addOnSuccessListener {
                    classContent.clear()
                    classContent.add(ClassContent(it.id, it.getField<String>("content").toString()))
                    d("bomohit", "onItemSelectCollection , $classContent")
                    if (it.getField<String>("content").toString() == "null") {
                        classContent.clear()
                    }
                    rv()
                }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    private fun rv() {
        d("bomohit", "recyclerView , $classContent")
        val rsv = requireView().findViewById<RecyclerView>(R.id.class_recyclerView)
        rsv.apply {
            layoutManager = LinearLayoutManager(this@TimeTableFragment.context)
            adapter = ClassAdapter(classContent)
        }
    }
}

data class ClassContent (
    val name : String,
    val content: String
)
