package com.maran.healthapp.presentation.article

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maran.healthapp.domain.models.ArticleModel
import com.maran.healthapp.domain.models.Category
import com.maran.healthapp.domain.models.NewsSource
import com.maran.healthapp.domain.repositories.ArticleRepository
import com.maran.healthapp.domain.usecases.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val useCase: GetTopHeadlinesUseCase) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    val isReaderMode = mutableStateOf(false)
    val chosenIndex = mutableStateOf(-1)
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

    fun articleToRead(): ArticleModel {
        return ArticleModel(
            author = "maran",
            description = "meow",
            content = "meow2",
            url = "random",
            publishedAt = "meow",
            title = "title",
            urlToImage = "mock",
            source = NewsSource(id = "1", name = "meow")
        )
    }

    fun readArticle(index: Int) {
        isReaderMode.value = true
        chosenIndex.value = index
    }

    @SuppressLint("NewApi")
    val articles = mutableStateOf(
        listOf(
            ArticleModel(
                title = "How to stay healthy?",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                author = "maran",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                publishedAt = LocalDate.now().toString(),
                urlToImage = "url",
                url = "url",
                source = NewsSource(id = "1L", name = "meow")
            )
        )
    )

    fun getArticles(): List<ArticleModel> {
        //todo, usecase/dao возвращают paging source, не знаю нам в итоге он нужен или без пагинации,
        // поэтому не стала это разрушать
//        coroutineScope.launch {
//            articles.value = repository.getAllArticles()
//        }
        return articles.value
    }
}