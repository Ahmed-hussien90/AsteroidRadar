package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.viewmodels.MainViewModel


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            requireActivity(),
            MainViewModel.Factory(activity.application)
        )[MainViewModel::class.java]
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        adapter = MainAdapter(MainAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClick(asteroid)
        })
        checkInternet()
        updateAdapter(resources.getString(R.string.saved_asteroids))

        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToDetailScreen.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action =
                    MainFragmentDirections.actionShowDetail(it)
                this.findNavController().navigate(action)
                viewModel.onDetailScreenNavigated()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateAdapter(str: String) {

        binding.statusLoadingWheel.visibility = View.VISIBLE
        binding.asteroidRecycler.visibility = View.GONE

        when (str) {
            resources.getString(R.string.today_asteroids) -> {
                viewModel.todayAsteroids.observe(viewLifecycleOwner) {
                    adapter.addList(it)
                }
            }
            resources.getString(R.string.next_week_asteroids) -> {
                viewModel.weekAsteroids.observe(viewLifecycleOwner) {
                    adapter.addList(it)
                }
            }
            else -> {
                viewModel.asteroids.observe(viewLifecycleOwner) {
                    adapter.addList(it)
                }
            }
        }
        binding.statusLoadingWheel.visibility = View.GONE
        binding.asteroidRecycler.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> updateAdapter(resources.getString(R.string.saved_asteroids))
            R.id.show_today_menu -> updateAdapter(resources.getString(R.string.today_asteroids))
            R.id.show_week_menu -> updateAdapter(resources.getString(R.string.next_week_asteroids))
        }
        return true
    }

    private fun checkInternet() {
        viewModel.connectivityLiveData.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                Snackbar.make(
                    binding.asteroidRecycler,
                    "Internet Connection Failed",
                    Snackbar.LENGTH_LONG
                ).setAction("OK") {}.show()
            } else {
                viewModel.refreshPicOfDayAndAsteroids()
                observeImageOfDay()
            }
        }
    }

    private fun observeImageOfDay() {
        viewModel.imageOfDay.observe(viewLifecycleOwner) { pic ->
            binding.imageTitle.text = pic.title
            Picasso.get().load(pic.url).error(android.R.drawable.stat_notify_error)
                .into(binding.activityMainImageOfTheDay)
        }
    }
}
