package com.maran.healthapp.presentation.article

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
                ArticleList(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    items = articleViewModel.articles.value,
                    onClick = { index: Int -> articleViewModel.readArticle(index) })
            }
        }
    } else {
        ReaderScreen(
            readerModeOn = articleViewModel.isReaderMode,
            articleModel = articleViewModel.articleToRead()
        )
    }
}

@Composable
fun ArticleList(modifier: Modifier = Modifier, items: List<ArticleModel>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(items) { index, item ->
            ArticleCard(
                articleName = item.name,
                articlePreview = item.text,
                onClick = onClick,
                index = index
            )
        }
    }
}

@Composable
fun ArticleCard(
    modifier: Modifier = Modifier,
    articleName: String,
    articlePreview: String,
    onClick: (Int) -> Unit,
    index: Int
) {
    Column(
        Modifier
            .clip(RoundedCornerShape(16))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick(index) }
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
            modifier = Modifier.fillMaxWidth(),
            text = articleModel.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            text = articleModel.author,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, start = 16.dp, end = 16.dp),
            text = articleModel.date.format(
                DateTimeFormatter.ofPattern(
                    "dd.MM.yyyy",
                    Locale.getDefault()
                )
            ),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier,
                text = articleModel.text,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}