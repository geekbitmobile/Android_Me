/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.android_me.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.android.android_me.R
import java.util.*

/**
 * Mandatory empty constructor for the fragment manager to instantiate the fragment
 */
class BodyPartFragment : Fragment() {

    // Variables to store a list of image resources and the index of the image that this fragment displays
    private var mImageIds: List<Int>? = null
    private var mListIndex: Int = 0

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Load the saved state (the list of images and list index) if there is one
        if (savedInstanceState != null) {
            mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST)
            mListIndex = savedInstanceState.getInt(LIST_INDEX)
        }

        // Inflate the Android-Me fragment layout
        val rootView = inflater!!.inflate(R.layout.fragment_body_part, container, false)

        // Get a reference to the ImageView in the fragment layout
        val imageView = rootView.findViewById<View>(R.id.body_part_image_view) as ImageView

        // If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found
        if (mImageIds != null) {
            // Set the image resource to the list item at the stored index
            imageView.setImageResource(mImageIds!![mListIndex])

            // Set a click listener on the image view
            imageView.setOnClickListener {
                // Increment position as long as the index remains <= the size of the image ids list
                if (mListIndex < mImageIds!!.size - 1) {
                    mListIndex++
                } else {
                    // The end of list has been reached, so return to beginning index
                    mListIndex = 0
                }
                // Set the image resource to the new list item
                imageView.setImageResource(mImageIds!![mListIndex])
            }

        } else {
            Log.v(TAG, "This fragment has a null list of image id's")
        }

        // Return the rootView
        return rootView
    }

    // Setter methods for keeping track of the list images this fragment can display and which image
    // in the list is currently being displayed

    fun setImageIds(imageIds: List<Int>) {
        mImageIds = imageIds
    }

    fun setListIndex(index: Int) {
        mListIndex = index
    }

    /**
     * Save the current state of this fragment
     */
    override fun onSaveInstanceState(currentState: Bundle?) {
        currentState!!.putIntegerArrayList(IMAGE_ID_LIST, mImageIds as ArrayList<Int>?)
        currentState.putInt(LIST_INDEX, mListIndex)
    }

    companion object {

        // Final Strings to store state information about the list of images and list index
        val IMAGE_ID_LIST = "image_ids"
        val LIST_INDEX = "list_index"

        // Tag for logging
        private val TAG = "BodyPartFragment"
    }


}
