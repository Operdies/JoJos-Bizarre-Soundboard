package com.example.anla.myapplication;

import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.view.menu.ListMenuItemView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout

class FavFragment(n: Int, var soundNames: List<String>, var fileHandles: ArrayList<Int>) : MyFragment(n, soundNames, fileHandles) {

    override fun onResume() {
        super.onResume()
        updateFavs()
        initButtons(preference!!)

    }

    private fun updateFavs(){
        val prefs = preference!!
        var strings = prefs.all.keys
        var files: ArrayList<Int> = ArrayList()
        var keys: ArrayList<String> = ArrayList()
        for (key in strings) {
            try {
                var file = prefs.getInt(key, 0)
                files.add(file)
                keys.add(key)
            } catch (e: Exception) { /* block */
            }
        }
        soundNames = keys
        fileHandles = files
    }


    private fun initButtons(preference: SharedPreferences){
        val buttonManager: ListMenuItemView = view?.findViewById(R.id.bContainer) as ListMenuItemView
        buttonManager.removeAllViews()
        for (key in soundNames) {
            val button = Button(currentContext)
            val player = MediaPlayer.create(currentContext, preference!!.getInt(key, 0))
            button.text = key
            button.setOnClickListener { _ ->
                player.start()
            }
            button.setOnLongClickListener { _ ->
                removeFromFavorites(view as View, button)
            }
            buttonManager.addView(button)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        val buttonManager = rootView.findViewById(R.id.bContainer) as ListMenuItemView
        currentContext = rootView.context

        for (i in files.indices) {
            val button = Button(currentContext)
            val player = MediaPlayer.create(currentContext, files[i])
            button.text = filenames[i]

            button.setOnClickListener { _ ->
                player.start()
            }
            button.setOnLongClickListener { _ ->
                removeFromFavorites(rootView, button)
            }
            buttonManager.addView(button)

        }
//        updateFavs()
//        initButtons(preference!!, rootView)

        return rootView
    }
    fun removeButton(button: Button){
        val buttonManager: ListMenuItemView = view?.findViewById(R.id.bContainer) as ListMenuItemView
        buttonManager.removeView(button)
    }

    private fun removeFromFavorites(view: View, button: Button): Boolean {
        var alert = AlertDialog.Builder(view.context)
        val name: String = button.text as String
        alert.setTitle("Remove $name from favorites?")
//        alert.setMessage("Hello!")
        alert.setCancelable(true)
        alert.setPositiveButton("Hai", DialogInterface.OnClickListener { _, _ ->
            val editor = preference?.edit()
            editor?.remove(button.text as String?)
            editor?.commit()
            removeButton(button)
        })
        alert.setNegativeButton("No.", DialogInterface.OnClickListener { _, _ -> /* */ })
        alert.show()
        return true

    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        fun favInstance(n: Int, prefs: SharedPreferences): FavFragment {
            var strings = prefs.all.keys
            var files: ArrayList<Int> = ArrayList()
            var keys: ArrayList<String> = ArrayList()
            for (key in strings) {
                try {
                    var file = prefs.getInt(key, 0)
                    files.add(file)
                    keys.add(key)
                }
                catch (e: Exception){ /* block */ }
            }


            val fragment = FavFragment(n, keys, files)
            val args: Bundle = Bundle()
            args.putInt(ARG_SECTION_NUMBER, n)
            fragment.arguments = args
            fragment.preference = prefs
            fragment.retainInstance = true

            return fragment
        }
    }


    fun notifyChanged() {
//        val fm: FragmentTransaction = fragmentManager.beginTransaction()
//        fm.detach(this)
//        fm.attach(this)
//        fm.commit()
        if (view != null) {
            updateFavs()
            initButtons(preference!!)
        }



    }
}