package com.example.myweather.ui.location

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.myweather.databinding.DialogLocationBinding

class LocationDialog : DialogFragment() {

    private lateinit var binding: DialogLocationBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            binding = DialogLocationBinding.inflate(inflater)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(binding.root)
                // Add action buttons
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Mandamos el resultado al fragment
                        val locationName = binding.txtLocation.text.toString()
                        setFragmentResult(Companion.requestKey, bundleOf("locationName" to locationName))
                        getDialog()?.cancel()
                        //findNavController().navigate(R.id.action_locationDialog_to_locationFragment)
                    })

                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val requestKey : String = "LOCATION_DIALOG_RESULT"
    }


}