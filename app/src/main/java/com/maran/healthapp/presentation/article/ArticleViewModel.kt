package com.maran.healthapp.presentation.article

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maran.healthapp.domain.models.ArticleModel
import com.maran.healthapp.domain.usecases.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val useCase: GetTopHeadlinesUseCase) : ViewModel() {
    val isReaderMode = mutableStateOf(false)
    private val _category = MutableStateFlow("health")
    private val _country: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _pageSize = 20;


    val newsCategoryPagingFlow = _category.flatMapLatest { category ->
        useCase.invoke(category, _country.value, _pageSize)
    }.cachedIn(viewModelScope)

    val newsCountryPagingFlow = _country.flatMapLatest { country ->
        useCase.invoke(_category.value, country, _pageSize)
    }.cachedIn(viewModelScope)

    fun setCategory(category: String) {
        _category.value = category
    }

    fun setCountry(country: String?) {
        Log.d("D", "country $country selected")
        _country.value = country
    }

    private val _selectedArticle = mutableStateOf<ArticleModel?>(null)
    val selectedArticle: State<ArticleModel?> = _selectedArticle

    fun readArticle(article: ArticleModel) {
        isReaderMode.value = true
        _selectedArticle.value = article
    }
}