package com.astro.test.aqil.features.dashboard.fragment

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.astro.R
import com.astro.test.aqil.features.dashboard.adapter.ProfilesAdapter
import com.astro.test.aqil.features.dashboard.data.dto.ProfilesDto
import com.astro.test.aqil.features.dashboard.utils.Status
import com.astro.test.aqil.features.dashboard.viewmodel.ProfilesViewModel
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.reflect.Type


class DashboardFragment : Fragment(), ProfilesAdapter.OnItemCallBack {

    private val viewModel : ProfilesViewModel by viewModel()
    lateinit var fragmentView: View
    private var listProfile = mutableListOf<ProfilesDto>()
    private var listProfileDb = ArrayList<String>()
    lateinit var swipeRefresh: SwipeRefreshLayout
    private val prefs by inject<SharedPreferences>()
    val gson = GsonBuilder().create()

    override fun onCreate(savedInstanceState: Bundle?) {

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.common_yes)) { _, _ ->
                            requireActivity().moveTaskToBack(true)
                            requireActivity().finish()
                        }
                        .setNegativeButton(getString(R.string.common_no), null)
                        .show()
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        //setHasOptionsMenu(false)
        requireActivity().window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        fragmentView = view
        // Hide the status bar.
        (activity as AppCompatActivity).supportActionBar?.hide()
        val collectionType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        listProfileDb = gson.fromJson(prefs.getString("KEY_LOGIN", "[]"), collectionType)

        swipeRefresh = view.swipe_refresh

        swipeRefresh.setOnRefreshListener {
            fragmentView.loading_data.visibility = View.VISIBLE
            fragmentView.no_data.visibility = View.INVISIBLE
            fragmentView.found_data.visibility = View.INVISIBLE
            Handler().postDelayed(Runnable {
                swipeRefresh.isRefreshing = false
                viewModel.getProfile()
                fragmentView.rg_sort!!.clearCheck()
                fragmentView.et_search.setText("")
            }, 2000)
        }

        fragmentView.bt_search.setOnClickListener {
            fragmentView.et_search.isFocusable = false
            hideKeyboardFrom(requireContext(), fragmentView)
        }

        fragmentView.et_search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handleSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        observerProfiles()
        viewModel.getProfile()

        fragmentView.rg_sort!!.setOnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
            if (checkedId == R.id.rb_asc) {
                listProfile = listProfile.sortedBy { it.login?.toLowerCase() } as MutableList<ProfilesDto>
                val adapter = ProfilesAdapter(requireContext(), listProfile, listProfileDb, this)
                adapter.notifyDataSetChanged()
                fragmentView.list_profile?.adapter = adapter
            } else if (checkedId == R.id.rb_desc) {
                listProfile = listProfile.sortedByDescending { it.login?.toLowerCase() } as MutableList<ProfilesDto>
                val adapter = ProfilesAdapter(requireContext(), listProfile, listProfileDb, this)
                adapter.notifyDataSetChanged()
                fragmentView.list_profile?.adapter = adapter
            }
        }

        return view
    }

    private fun handleSearch(data: String){
        Handler().postDelayed(Runnable {
            var searchData : List<ProfilesDto>? = null
            if (data.isEmpty()){
                searchData = listProfile
            }else{
                searchData = listProfile.filter { s -> s.login?.toLowerCase()?.contains(data)!! }
            }
            val adapter = ProfilesAdapter(requireContext(), searchData!!, listProfileDb, this)
            adapter.notifyDataSetChanged()
            fragmentView.list_profile?.adapter = adapter
        }, 300)
    }

    private fun observerProfiles() {
        viewModel.profile.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                when(it.status) {
                    Status.SUCCESS -> {
                        fragmentView.loading_data.visibility = View.INVISIBLE
                        fragmentView.no_data.visibility = View.INVISIBLE
                        fragmentView.found_data.visibility = View.VISIBLE
                        listProfile.clear()
                        fragmentView.list_profile?.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
                        it.data?.iterator()?.forEach {
                            listProfile.add(it)
                        }
                        fragmentView.list_profile?.layoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
                        val adapter = ProfilesAdapter(requireContext(), listProfile, listProfileDb, this)
                        adapter.notifyDataSetChanged()
                        fragmentView.list_profile?.adapter = adapter
                        Log.d("TAG", "observerLogin: sukses" )
                    }
                    Status.LOADING -> {
                        fragmentView.loading_data.visibility = View.VISIBLE
                        fragmentView.no_data.visibility = View.INVISIBLE
                        fragmentView.found_data.visibility = View.INVISIBLE
                        Log.d("TAG", "observerLogin: loading" )
                    }
                    Status.ERROR -> {
                        fragmentView.loading_data.visibility = View.INVISIBLE
                        fragmentView.no_data.visibility = View.VISIBLE
                        fragmentView.found_data.visibility = View.INVISIBLE
                        if (!it.message.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        else Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "observerLogin: error" )
                    }
                    Status.DONE -> {
                        Log.d("TAG", "observerLogin: done" )
                    }
                }
            })
    }

    override fun onItemClicked(position: Int, profile: ProfilesDto?) {
        val collectionType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        listProfileDb = gson.fromJson(prefs.getString("KEY_LOGIN", "[]"), collectionType)

        val findData = listProfileDb.find { s -> s == profile?.login!! }
        if (findData.isNullOrBlank()){
            listProfileDb.add(profile?.login!!)
        }else{
            listProfileDb.remove(profile?.login!!)
        }
        val listProfileSaved = gson.toJson(listProfileDb)

        val editor = prefs.edit()
        editor.putString("KEY_LOGIN", listProfileSaved)
        editor.apply()

        val adapter = ProfilesAdapter(requireContext(), listProfile, listProfileDb, this)
        adapter.notifyDataSetChanged()
        fragmentView.list_profile?.adapter = adapter
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}