package com.example.anla.myapplication

import android.content.SharedPreferences
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.content.Context
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private val favourites = "favourites"
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var nullablePrefs: SharedPreferences? = null

    /**
     * The [ViewPager] that will host the section contents.
     */
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getPreferences(Context.MODE_PRIVATE)
        nullablePrefs = preferences


        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        val jotaroSounds = arrayListOf<Int>(R.raw.yareyaredaze, R.raw.nonono, R.raw.yesyesyes)

        val jotaroStrings = arrayListOf<String>("Yare Yare Daze", "No, no, no", "Yes, yes, yes")
        val josephSounds = arrayListOf<Int>(R.raw.helpmeohmygod, R.raw.horyshet, R.raw.holyshit, R.raw.ohmygodwryy,
                R.raw.ohmyhod, R.raw.ohno3, R.raw.sonofabitch, R.raw.ohnooo, R.raw.yesyesyesomg, R.raw.naisu)
        val josephStrings = arrayListOf<String>("Help me, oh my god", "Hory shit", "Holy shit", "Oh my god, REE",
                "Oh my god!", "Oh no!", "Son of a bitch", "Oh no...", "Yes, yes, yes, oh my god", "Naisu!")
        val dioSounds = arrayListOf<Int>(R.raw.konodioda, R.raw.goodbyejojo, R.raw.zawarudo, R.raw.zawarudo2)
        val dioStrings = arrayListOf<String>("Kono Dio Da!", "Goodbye JoJo", "Za Warudo", "Za Warudo!")
        val duwangSounds = arrayListOf<Int>(R.raw.beautifulduwang, R.raw.chew, R.raw.picnic, R.raw.stoopid, R.raw.stop)
        val duwangStrings = arrayListOf<String>("Beautiful Duwang", "chew", "picnic", "Reatard", "Stop")
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, preferences)
        var i = 0
        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(i++, preferences, jotaroStrings, jotaroSounds), "Jotaro")
        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(i++, preferences, josephStrings, josephSounds), "Joseph")
        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(i++, preferences, dioStrings, dioSounds), "Dio")
        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(i++, preferences, duwangStrings, duwangSounds), "Duwang")
        if (preferences.all.keys.size > 0)
            (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(FavFragment.favInstance(i++, preferences), favourites)
//        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(3, "Joseph", arrayOf("Oh","my","god"), arrayOf(4,5,6)), "Joseph")
//        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(n), "Jotaro")
//        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(n), "Jotaro")
//        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(n), "Jotaro")
//        (mSectionsPagerAdapter as SectionsPagerAdapter).addItem(MyFragment.newInstance(n), "Jotaro")

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager!!.adapter = mSectionsPagerAdapter


        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            if (nullablePrefs != null){
                var editor = nullablePrefs!!.edit()
                editor.clear()
                editor.commit()
                mSectionsPagerAdapter!!.notifyChanged()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager, var preferences: SharedPreferences) : FragmentPagerAdapter(fm) {
        var pages: ArrayList<Fragment> = ArrayList()
        var pageTitles: ArrayList<String> = ArrayList()
        var fav: FavFragment? = null
        private fun favEmpty(): Boolean = favSize() == 0

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return pages.get(position)

        }
        private fun favSize(): Int {
            return nullablePrefs!!.all.size
        }


        public fun notifyChanged(){
            fav?.notifyChanged()
        }


        fun addItem(fragment: Fragment, name: String){
            if (fragment is FavFragment) {
                fragment.manager = this
                fav = fragment

            }

            if (fragment is MyFragment) {
                fragment.manager = this
            }
            pages.add(fragment)
            pageTitles.add(name)

        }

        override fun getCount(): Int {
            return pages.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return pageTitles.get(position)
        }
    }
}

