package com.example.navigation_app.ui.home

import android.os.Bundle
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
import com.example.navigation_app.R
import com.example.navigation_app.viewmodel.loginViewModel
import com.github.chrisbanes.photoview.PhotoView

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var homeViewModel: HomeViewModel
    var spinner_from_id : String? = null
    var spinner_to_id : String? = null

    var select_from = 0
    var select_to = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
        })

        var viewModel = ViewModelProvider(requireActivity()).get(loginViewModel::class.java)
        d("bomoh", viewModel.id.toString()+"home")

        val spinner_from : Spinner = root.findViewById(R.id.spinner_from)
        val spinner_to : Spinner = root.findViewById(R.id.spinner_to)

        spinner_from.onItemSelectedListener = this
        spinner_to.onItemSelectedListener = this
        spinner_from_id = spinner_from.id.toString()
        spinner_to_id = spinner_to.id.toString()

        ArrayAdapter.createFromResource(
            root.context,
            R.array.from_arrays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_from.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            root.context,
            R.array.destination_arrays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner_to.adapter = adapter
        }

        return root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val photoView : PhotoView = requireView().findViewById(R.id.photo_view)
        // check which spinner selected
        if (parent!!.id.toString() == spinner_from_id) {
            select_from = position
//            d("bomoh", "${parent!!.getItemAtPosition(position)}, id: ${parent.id}, position: $position")
        } else {
            select_to = position+1
//            d("bomoh", "${parent!!.getItemAtPosition(position)}, id: ${parent.id}, position: $select_to")
        }

        if (select_from > 0 && select_to > 1) {
            d("bomoh", "selection satisfied")
            val nav = "nav_${select_from}_${select_to}"
            val id = resources.getIdentifier(nav, "drawable", requireActivity().packageName)

            photoView.setImageResource(id)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}