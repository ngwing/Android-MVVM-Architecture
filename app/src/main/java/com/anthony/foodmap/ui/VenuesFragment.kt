package com.anthony.foodmap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anthony.foodmap.databinding.VenuesFragBinding
import com.anthony.foodmap.util.getViewModelFactory
import com.anthony.foodmap.util.setupSnackbar
import com.anthony.foodmap.util.toParamString
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class VenuesFragment : Fragment() {

    private val viewModel by viewModels<VenuesViewModel> { getViewModelFactory() }
    private lateinit var mapViewModel: MapViewModel

    private lateinit var viewDataBinding: VenuesFragBinding

    private lateinit var listAdapter: VenuesAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mapViewModel = ViewModelProviders.of(activity!!, getViewModelFactory()).get(MapViewModel::class.java)
        viewDataBinding = VenuesFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            mapviewmodel = mapViewModel
        }
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewModel.visibleRegionBounds.observe(viewLifecycleOwner, Observer { bounds ->
            viewModel.loadVenues(bounds.southwest.toParamString(), bounds.northeast.toParamString(), bounds.center)
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.reset()
                mapViewModel.reset()
            }
        })

    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }


    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = VenuesAdapter(viewModel, mapViewModel)
            viewDataBinding.venuesList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }


}
