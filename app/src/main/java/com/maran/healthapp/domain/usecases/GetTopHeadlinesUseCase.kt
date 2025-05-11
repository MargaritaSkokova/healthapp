package com.maran.healthapp.domain.usecases

import androidx.paging.PagingData
import com.maran.healthapp.domain.models.ArticleModel
import com.maran.healthapp.domain.models.Category
import com.maran.healthapp.domain.models.Country
import com.maran.healthapp.domain.repositories.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(
        category: String?,
        country: String?,
        pageSize: Int = 20
    ): Flow<PagingData<ArticleModel>> {
        val enumCategory = if (category != null) Category.validateCategory(category) else null
        val enumCountry = if (country != null) Country.validateCountry(country) else null
        return repository.getTopHeadlines(enumCategory?.apiName, enumCountry?.apiCode, pageSize)
    }
}
