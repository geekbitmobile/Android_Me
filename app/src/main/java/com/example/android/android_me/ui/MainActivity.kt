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

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.Toast

import com.example.android.android_me.R
import com.example.android.android_me.data.AndroidImageAssets

// This activity is responsible for displaying the master list of all images
// Implement the MasterListFragment callback, OnImageClickListener
class MainActivity : AppCompatActivity(), MasterListFragment.OnImageClickListener {

    // Variables to store the values for the list index of the selected images
    // The default value will be index = 0
    private var headIndex: Int = 0
    private var bodyIndex: Int = 0
    private var legIndex: Int = 0

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private var mTwoPane: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Determine if you're creating a two-pane or single-pane display
        if (findViewById<View>(R.id.android_me_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true

            // Change the GridView to space out the images more on tablet
            val gridView = findViewById<View>(R.id.images_grid_view) as GridView
            gridView.numColumns = 2

            // Getting rid of the "Next" button that appears on phones for launching a separate activity
            val nextButton = findViewById<View>(R.id.next_button) as Button
            nextButton.visibility = View.GONE

            if (savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                val fragmentManager = supportFragmentManager

                // Creating a new head fragment
                val headFragment = BodyPartFragment()
                headFragment.setImageIds(AndroidImageAssets.heads)
                // Add the fragment to its container using a transaction
                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit()

                // New body fragment
                val bodyFragment = BodyPartFragment()
                bodyFragment.setImageIds(AndroidImageAssets.bodies)
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit()

                // New leg fragment
                val legFragment = BodyPartFragment()
                legFragment.setImageIds(AndroidImageAssets.legs)
                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit()
            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false
        }

    }

    // Define the behavior for onImageSelected
    override fun onImageSelected(position: Int) {
        // Create a Toast that displays the position that was clicked
        Toast.makeText(this, "Position clicked = " + position, Toast.LENGTH_SHORT).show()

        // bodyPartNumber will be = 0 for the head fragment, 1 for the body, and 2 for the leg fragment
        // Dividing by 12 gives us these integer values because each list of images resources has a size of 12
        val bodyPartNumber = position / 12

        // Store the correct list index no matter where in the image list has been clicked
        // This ensures that the index will always be a value between 0-11
        val listIndex = position - 12 * bodyPartNumber

        // Handle the two-pane case and replace existing fragments right when a new image is selected from the master list
        if (mTwoPane) {
            // Create two=pane interaction

            val newFragment = BodyPartFragment()

            // Set the currently displayed item for the correct body part fragment
            when (bodyPartNumber) {
                0 -> {
                    // A head image has been clicked
                    // Give the correct image resources to the new fragment
                    newFragment.setImageIds(AndroidImageAssets.heads)
                    newFragment.setListIndex(listIndex)
                    // Replace the old head fragment with a new one
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.head_container, newFragment)
                            .commit()
                }
                1 -> {
                    newFragment.setImageIds(AndroidImageAssets.bodies)
                    newFragment.setListIndex(listIndex)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.body_container, newFragment)
                            .commit()
                }
                2 -> {
                    newFragment.setImageIds(AndroidImageAssets.legs)
                    newFragment.setListIndex(listIndex)
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.leg_container, newFragment)
                            .commit()
                }
                else -> {
                }
            }
        } else {

            // Handle the single-pane phone case by passing information in a Bundle attached to an Intent

            when (bodyPartNumber) {
                0 -> headIndex = listIndex
                1 -> bodyIndex = listIndex
                2 -> legIndex = listIndex
                else -> {
                }
            }

            // Put this information in a Bundle and attach it to an Intent that will launch an AndroidMeActivity
            val b = Bundle()
            b.putInt("headIndex", headIndex)
            b.putInt("bodyIndex", bodyIndex)
            b.putInt("legIndex", legIndex)

            // Attach the Bundle to an intent
            val intent = Intent(this, AndroidMeActivity::class.java)
            intent.putExtras(b)

            // The "Next" button launches a new AndroidMeActivity
            val nextButton = findViewById<View>(R.id.next_button) as Button
            nextButton.setOnClickListener { startActivity(intent) }
        }

    }

}
