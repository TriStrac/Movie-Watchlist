package com.busal.finals.moviewatchlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.busal.finals.moviewatchlist.databinding.FragmentWatchedBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WatchedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WatchedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var binding:FragmentWatchedBinding
    private var onWatched = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchedBinding.inflate(inflater,container,false)

        binding.watchedOrRemovedButton.setOnClickListener {
            if(onWatched){
                binding.watchedOrRemovedButton.text="Removed"
                onWatched=false
                showRemoved()
            }else{
                binding.watchedOrRemovedButton.text="Watched"
                onWatched=true
                showWatched()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
    private fun showRemoved(){

    }
    private fun showWatched(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WatchedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WatchedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}