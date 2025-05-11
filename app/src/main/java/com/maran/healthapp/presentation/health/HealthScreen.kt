package com.maran.healthapp.presentation.health

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.maran.healthapp.R
import com.maran.healthapp.domain.models.HealthStateModel
import kotlin.math.abs

@Composable
fun HealthScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    healthViewModel: HealthViewModel = hiltViewModel()
) {
    if (!healthViewModel.wasChecked.value) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.header_question),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            ChoiceObject(
                questionName = stringResource(R.string.life_question),
                choices = healthViewModel.lifeList,
                chosenIndex = healthViewModel.chosenLifeIndex
            )
            Spacer(modifier = Modifier.height(32.dp))
            ChoiceObject(
                questionName = stringResource(R.string.health_question),
                choices = healthViewModel.healthList,
                chosenIndex = healthViewModel.chosenHealthIndex
            )
            Spacer(modifier = Modifier.height(32.dp))
            ChoiceObject(
                questionName = stringResource(R.string.mind_question),
                choices = healthViewModel.moodList,
                chosenIndex = healthViewModel.chosenMoodIndex
            )
            Column(
                Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                RoundedMultilineInput(
                    text = healthViewModel.text.value,
                    onTextChange = { healthViewModel.text.value = it })
            }
            Column(
                Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier = Modifier
                        .wrapContentWidth(),
                    onClick = { healthViewModel.saveDaily() },
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    } else {
        if (healthViewModel.isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            WeeklyStatsScreen(
                healthViewModel.weeklyStatistics,
                healthViewModel.weeklyIconStatistics,
                healthViewModel.weeklySuggestion,
                healthViewModel.weeklyLifeSuggestion,
                healthViewModel.weeklyHealthSuggestion,
                healthViewModel.weeklyMoodSuggestion,
                healthViewModel.weeklyLife,
                healthViewModel.weeklyHealth,
                healthViewModel.weeklyMood
            )
        }
    }
}

@Composable
fun RoundedMultilineInput(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        placeholder = { stringResource(R.string.tell_more) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.BorderColor, contentDescription = null)
        },
        singleLine = false,
        shape = RoundedCornerShape(50.dp),
    )
}


@Composable
fun ChoiceObject(
    modifier: Modifier = Modifier,
    questionName: String,
    choices: List<Pair<ImageVector, String>>,
    chosenIndex: MutableState<Int>
) {
    Column {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = questionName,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            ChoiceCarousel(choices = choices, chosenIndex = chosenIndex)
        }
    }
}

@Composable
fun ChoiceCarousel(
    modifier: Modifier = Modifier,
    choices: List<Pair<ImageVector, String>>,
    chosenIndex: MutableState<Int>
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val startPadding = (screenWidth - 32.dp) / 2
    val listState = rememberLazyListState()

    val selectedIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@derivedStateOf 0

            val viewportCenter = (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                abs((item.offset + item.size / 2) - viewportCenter)
            }?.index ?: 0
        }
    }

    chosenIndex.value = selectedIndex

    LaunchedEffect(selectedIndex) {
        listState.animateScrollToItem(selectedIndex)
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = startPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = listState
    ) {
        itemsIndexed(choices) { index, item ->
            ChoiceElement(
                icon = item.first,
                label = item.second,
                modifier = Modifier,
                selected = selectedIndex == index
            )
        }
    }
}

@Composable
fun ChoiceElement(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .wrapContentSize()
            .then(
                if (selected) Modifier.shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = MaterialTheme.colorScheme.primary,
                    spotColor = MaterialTheme.colorScheme.primary
                ) else Modifier
            )
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun WeeklyStatsScreen(
    weeklyStatistics: MutableState<String>,
    weeklyIconStatistics: MutableState<ImageVector>,
    weeklySuggestion: MutableState<String>,
    weeklyLifeSuggestion: MutableState<String>,
    weeklyHealthSuggestion: MutableState<String>,
    weeklyMoodSuggestion: MutableState<String>,
    weeklyLife: MutableState<List<Pair<ImageVector?, String>>>,
    weeklyHealth: MutableState<List<Pair<ImageVector?, String>>>,
    weeklyMood: MutableState<List<Pair<ImageVector?, String>>>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your statistics",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            OverallBanner(weeklyStatistics, weeklyIconStatistics, weeklySuggestion)
            CategorySection(
                title = "Life",
                dailyRatings = weeklyLife,
                recommendation = weeklyLifeSuggestion
            )
            CategorySection(
                title = "Health",
                dailyRatings = weeklyHealth,
                recommendation = weeklyHealthSuggestion
            )
            CategorySection(
                title = "Mood",
                dailyRatings = weeklyMood,
                recommendation = weeklyMoodSuggestion
            )
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    dailyRatings: MutableState<List<Pair<ImageVector?, String>>>,
    recommendation: MutableState<String>
) {
    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                dailyRatings.value.forEach {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = it.first ?: Icons.Default.Block,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(it.second)
                    }
                }
            }
        }

        Text(
            recommendation.value,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OverallBanner(
    weeklyStatistics: MutableState<String>,
    weeklyIconStatistics: MutableState<ImageVector>,
    weeklySuggestion: MutableState<String>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(
                shape = RoundedCornerShape(16.dp)
            )
            .background(color = MaterialTheme.colorScheme.onTertiary),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = weeklyStatistics.value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(72.dp),
                imageVector = weeklyIconStatistics.value,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = weeklySuggestion.value,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}


