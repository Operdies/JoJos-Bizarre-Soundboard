package com.example.anla.myapplication;

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.menu.ListMenuItemView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

open class MyFragment(var n: Int, var filenames: List<String>, var files: ArrayList<Int>) : Fragment() {

    var currentContext: Context? = null
    var preference: SharedPreferences? = null
    public var manager: MainActivity.SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_main, container, false)
        val bContainer = rootView.findViewById(R.id.bContainer) as ListMenuItemView
        currentContext = rootView.context

        for (i in files.indices) {
            val button = Button(currentContext)
            val player = MediaPlayer.create(currentContext, files[i])
            button.text = filenames[i]

            button.setOnClickListener { _ ->
                player.start()
            }
            button.setOnLongClickListener { _ ->
                addToFavorites(rootView, filenames[i], files[i], button)
            }
            bContainer.addView(button)

        }

        return rootView
    }

    private fun  addToFavorites(view: View, name: String, file: Int, button: Button): Boolean {
        var alert = AlertDialog.Builder(view.context)
        alert.setTitle("Add $name to favorites?")
        alert.setMessage("Hello!")
        alert.setCancelable(true)
        alert.setPositiveButton("Hai", DialogInterface.OnClickListener{ _, _ ->
            var editor = preference?.edit()
            editor?.putInt(name, file)
            editor?.putBoolean("changed", true)
            editor?.commit()
            manager?.notifyChanged()
        })
        alert.setNegativeButton("No.", DialogInterface.OnClickListener { _, _ -> /* */})
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
        fun newInstance(n: Int, prefs: SharedPreferences, filenames: ArrayList<String>, files: ArrayList<Int>): MyFragment {
            val fragment = MyFragment(n, filenames, files)
            var args: Bundle = Bundle()
            args.putInt(ARG_SECTION_NUMBER, n)
            fragment.arguments = args
            fragment.retainInstance = true
            fragment.preference = prefs

            return fragment
        }

    }
}