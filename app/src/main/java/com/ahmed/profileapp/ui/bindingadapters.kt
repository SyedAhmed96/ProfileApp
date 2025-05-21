// bindingadapters.kt

package com.ahmed.profileapp.ui

//Imports necessary Android, DataBinding, and Glide classes.
import android.widget.ImageView
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.ahmed.profileapp.R
import com.bumptech.glide.Glide

//Lets you bind the imageUrl property in XML to a function.
@BindingAdapter("imageUrl")

// Loads an image (as a circle) from a URL into an ImageView using Glide.
fun ImageView.setImageUrl(uri: String?) {
    Glide.with(context)
        .load(uri)
        .circleCrop() // Add circular transformation
        .placeholder(R.drawable.ic_profile_placeholder)
        .into(this)
}




//Lets you bind spinner entries and selection in XML.
@BindingAdapter("entries", "selectedItem")

//	Sets up a Spinner (dropdown): fills it with options, listens for changes, and updates the selected value.
fun bindSpinner(
    spinner: Spinner,
    entries: List<String>?,
    selectedItem: MutableLiveData<String>?
) {
    if (entries == null) return

    val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, entries)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter

    // Set selected item if present
    selectedItem?.value?.let {
        val index = entries.indexOf(it)
        if (index >= 0) spinner.setSelection(index)
    }

    // Handle item selection
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            selectedItem?.value = entries[position]
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
}



