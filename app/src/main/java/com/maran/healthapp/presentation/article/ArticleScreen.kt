package com.maran.healthapp.presentation.article

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.maran.healthapp.domain.models.ArticleModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleViewModel: ArticleViewModel = hiltViewModel()
) {
    if (!articleViewModel.isReaderMode.value) {
        val articles = articleViewModel.newsCategoryPagingFlow.collectAsLazyPagingItems()
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Articles",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.size(16.dp))
                ArticlePagingList(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    items = articles,
                    onClick = { articleViewModel.readArticle(it) })
            }
        }
    } else {
        articleViewModel.selectedArticle.value?.let {
            ReaderScreen(
                readerModeOn = articleViewModel.isReaderMode,
                articleModel = it
            )
        }
    }
}

@Composable
fun ArticlePagingList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<ArticleModel>,
    onClick: (ArticleModel) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = items.itemCount) { index ->
            val item = items[index]
            item?.let {
                ArticleCard(
                    articleName = it.title,
                    articlePreview = it.description ?: "empty",
                    onClick = onClick,
                    item = item
                )
            }
        }

        items.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(Modifier.padding(16.dp))
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(Modifier.padding(16.dp))
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val e = loadState.refresh as LoadState.Error
                    item {
                        Text("Error: ${e.error.localizedMessage}", color = Color.Red)
                    }
                }

                loadState.append is LoadState.Error -> {
                    val e = loadState.append as LoadState.Error
                    item {
                        Text("More Error: ${e.error.localizedMessage}", color = Color.Red)
                    }
                }
            }
        }
    }
}


@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    articleName: String,
    articlePreview: String,
    onClick: (ArticleModel) -> Unit,
    item: ArticleModel
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick(item) }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            text = articleName,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Left,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            text = articlePreview,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@SuppressLint("NewApi")
@Composable
fun ReaderScreen(
    modifier: Modifier = Modifier,
    readerModeOn: MutableState<Boolean>,
    articleModel: ArticleModel
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(articleModel.urlToImage) {
        isLoading = true
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            IconButton(onClick = { readerModeOn.value = false }) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = articleModel.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = articleModel.author ?: "unknown author",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 16.dp, end = 16.dp),
            text = articleModel.publishedAt.format(
                DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy",
                    Locale.getDefault()
                )
            ),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(articleModel.urlToImage)
                .crossfade(true)
                .listener(
                    onSuccess = { _, _ -> isLoading = false },
                    onError = { _, _ -> isLoading = false }
                )
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(200.dp)
                .placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.Gray.copy(alpha = 0.2f)
                ),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleModel.url))
                    context.startActivity(intent)
                },
            text = "Read more",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textDecoration = TextDecoration.Underline,
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier,
                text = articleModel.content ?: "Read article in the source",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}