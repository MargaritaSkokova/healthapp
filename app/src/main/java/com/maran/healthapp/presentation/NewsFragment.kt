package com.maran.healthapp.presentation

// для примера, если не понадобится - можешь удалить

/*import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var adapter: NewsPagingAdapter
    private var categoryJob: Job? = null
    private var countryJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupChips()
        setUpSpinner()
        observeNews()
    }

    private fun setupRecyclerView() {
        adapter = NewsPagingAdapter(
            onItemClick = { news ->
                val action = NewsFragmentDirections.actionNewsFragmentToNewsDetailsFragment(news)
                findNavController().navigate(action)
            },
            onReadFullClick = { news ->
                val action = NewsFragmentDirections.actionNewsFragmentToNewsItemFragment(news)
                findNavController().navigate(action)
            }
        )

        binding.rvNews.adapter = adapter.withLoadStateFooter(
            footer = NewsLoadStateAdapter { adapter.retry() }
        )
    }

    private fun setupChips() {
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val category = when (checkedIds.firstOrNull()) {
                R.id.chipGeneral -> Category.GENERAL.apiName
                R.id.chipBusiness -> Category.BUSINESS.apiName
                R.id.chipHealth -> Category.HEALTH.apiName
                R.id.chipSports -> Category.SPORTS.apiName
                R.id.chipScience -> Category.SCIENCE.apiName
                R.id.chipEntertainment -> Category.ENTERTAINMENT.apiName
                R.id.chipTechnology -> Category.TECHNOLOGY.apiName
                else -> Category.GENERAL.apiName
            }

            viewModel.setCategory(category)
        }
    }

    private fun setUpSpinner() {
        val regionSpinner: Spinner = binding.regionSpinner
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.regions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            regionSpinner.adapter = adapter
        }

        regionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedRegion = parent?.getItemAtPosition(position).toString()
                val countryCode = CountryViewEnum.viewToApi(selectedRegion)
                Log.d("RegionSpinner", "Chosen country: $selectedRegion, code=$countryCode")
                viewModel.setCountry(countryCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun observeNews() {
        categoryJob?.cancel()
        countryJob?.cancel()

        categoryJob = lifecycleScope.launch {
            viewModel.newsCategoryPagingFlow.collectLatest { pagingData: PagingData<ArticleModel> ->
                adapter.submitData(pagingData)
            }
        }

        countryJob = lifecycleScope.launch {
            viewModel.newsCountryPagingFlow.collectLatest { pagingData: PagingData<ArticleModel> ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        categoryJob?.cancel()
        countryJob?.cancel()
        super.onDestroyView()
        _binding = null
    }
}*/