package com.store_me.storeme.ui.home.owner.tab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.store_me.storeme.R
import com.store_me.storeme.ui.store_setting.story.setting.StoryDetailDialog
import com.store_me.storeme.ui.store_setting.story.setting.StoryThumbnailItem
import com.store_me.storeme.ui.store_setting.story.setting.StoryViewModel
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel

@Composable
fun StoryTab(
    storyViewModel: StoryViewModel
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val storeInfoData by storeDataViewModel.storeInfoData.collectAsState()

    val stories by storyViewModel.stories.collectAsState()
    var showStory by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    val chunkedStories = remember(stories) {
        stories.chunked(2)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        if(stories.isEmpty()) {
            Text(
                text = stringResource(R.string.owner_home_story_none),
                style = storeMeTextStyle(FontWeight.Normal, 0),
                color = GuideColor,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chunkedStories.forEachIndexed { chunkedIndex, chunkedStory ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        chunkedStory.forEachIndexed { index, recentStory ->
                            StoryThumbnailItem(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedIndex = (chunkedIndex * 2) + index
                                        showStory = true
                                    },
                                thumbnailUrl = recentStory.thumbNail,
                            )
                        }

                        if(stories.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if(showStory) {
                StoryDetailDialog(
                    storeInfoData = storeInfoData!!,
                    selectedIndex = selectedIndex,
                    stories = stories,
                    onDismiss = { showStory = false },
                    onLike = {
                        storyViewModel.updateStoryLike(it)
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(200.dp)
        )
    }
}