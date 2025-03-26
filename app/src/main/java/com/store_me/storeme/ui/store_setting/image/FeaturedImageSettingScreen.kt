@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.store_me.storeme.ui.store_setting.image

import android.app.Activity
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.store_me.storeme.R
import com.store_me.storeme.data.store.FeaturedImageData
import com.store_me.storeme.ui.component.BackWarningDialog
import com.store_me.storeme.ui.component.DefaultBottomSheet
import com.store_me.storeme.ui.component.DefaultButton
import com.store_me.storeme.ui.component.EditAndDeleteRow
import com.store_me.storeme.ui.component.LoadingProgress
import com.store_me.storeme.ui.component.SaveAndAddButton
import com.store_me.storeme.ui.component.TextLengthRow
import com.store_me.storeme.ui.component.TitleWithDeleteButtonAndRow
import com.store_me.storeme.ui.component.WarningDialog
import com.store_me.storeme.ui.component.addFocusCleaner
import com.store_me.storeme.ui.theme.ErrorColor
import com.store_me.storeme.ui.theme.GuideColor
import com.store_me.storeme.ui.theme.HighlightColor
import com.store_me.storeme.ui.theme.storeMeTextStyle
import com.store_me.storeme.utils.CropUtils
import com.store_me.storeme.utils.composition_locals.LocalAuth
import com.store_me.storeme.utils.composition_locals.loading.LocalLoadingViewModel
import com.store_me.storeme.utils.composition_locals.owner.LocalStoreDataViewModel
import com.yalantis.ucrop.UCrop
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun FeaturedImageSettingScreen(
    navController: NavController,
    featuredImageSettingViewModel: FeaturedImageSettingViewModel = hiltViewModel()
) {
    val storeDataViewModel = LocalStoreDataViewModel.current
    val auth = LocalAuth.current
    val loadingViewModel = LocalLoadingViewModel.current

    val originalFeaturedImages by storeDataViewModel.featuredImages.collectAsState()
    val featuredImages by featuredImageSettingViewModel.featuredImages.collectAsState()
    val croppedImageUri by featuredImageSettingViewModel.croppedImageUri.collectAsState()
    val croppedImageUrl by featuredImageSettingViewModel.croppedImageUrl.collectAsState()
    val progress by featuredImageSettingViewModel.uploadProgress.collectAsState()

    var editIndex by remember { mutableStateOf<Int?>(null) }
    var deleteIndex by remember { mutableStateOf<Int?>(null) }

    val focusManager = LocalFocusManager.current
    val hasDifference = remember { mutableStateOf(false) }
    val showBackWarningDialog = remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    //Crop 관련
    val context = LocalContext.current
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let { uri ->
                featuredImageSettingViewModel.updateCroppedImageUri(uri)
            }
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        it?.let { sourceUri ->
            val cropIntent = CropUtils.getCropIntent(context = context, sourceUri = sourceUri, aspectRatio = null)
            cropLauncher.launch(cropIntent)
        }
    }

    LaunchedEffect(originalFeaturedImages) {
        featuredImageSettingViewModel.updateFeaturedImages(originalFeaturedImages)
    }

    LaunchedEffect(originalFeaturedImages, featuredImages) {
        hasDifference.value = originalFeaturedImages != featuredImages
    }

    LaunchedEffect(croppedImageUri) {
        if(croppedImageUri != null) {
            featuredImageSettingViewModel.uploadStoreFeaturedImage(storeName = storeDataViewModel.storeInfoData.value!!.storeName)
        }
    }

    fun onClose() {
        if(hasDifference.value)
            showBackWarningDialog.value = true
        else
            navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        containerColor = Color.White,
        topBar = {
            TitleWithDeleteButtonAndRow(
                title = "사진 관리",
                scrollBehavior = scrollBehavior,
                onClose = { onClose() }
            ) {
                SaveAndAddButton(
                    addButtonText = "사진 추가",
                    hasDifference = hasDifference.value,
                    onAddClick = {
                        galleryLauncher.launch("image/*")
                    },
                    onSaveClick = {
                        loadingViewModel.showLoading()

                        storeDataViewModel.patchStoreFeaturedImages(
                            storeId = auth.storeId.value!!,
                            featuredImages = featuredImages
                        )
                    }
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                FeaturedImagesReorderList(
                    featuredImages = featuredImages,
                    scrollBehavior = scrollBehavior,
                    onMoved = { from, to ->
                        featuredImageSettingViewModel.reorderFeaturedImages(fromIndex = from, toIndex = to)
                    },
                    onDelete = {
                        deleteIndex = it
                    },
                    onEdit = {
                        editIndex = it
                    }
                )
            }
        }
    )

    if(showBackWarningDialog.value) {
        BackWarningDialog(
            onDismiss = { showBackWarningDialog.value = false },
            onAction = {
                showBackWarningDialog.value = false
                navController.popBackStack()
            }
        )
    }

    if(croppedImageUri != null) {
        //추가
        DefaultBottomSheet(sheetState = sheetState, onDismiss = {
            featuredImageSettingViewModel.updateCroppedImageUri(null)
            featuredImageSettingViewModel.updateCroppedImageUrl(null)
        }) {
            AddFeaturedImageBottomSheetContent(
                uri = croppedImageUri!!,
                url = croppedImageUrl,
                progress = progress,
            ) {
                featuredImageSettingViewModel.addFeaturedImage(featuredImage = FeaturedImageData(image = croppedImageUrl ?: "", description = it))

                featuredImageSettingViewModel.updateCroppedImageUri(null)
                featuredImageSettingViewModel.updateCroppedImageUrl(null)
            }
        }
    }

    if(editIndex != null) {
        //수정
        DefaultBottomSheet(sheetState = sheetState, onDismiss = {
            editIndex = null
        }) {
            EditFeaturedImageBottomSheetContent(
                featuredImage = featuredImages[editIndex!!]
            ) {
                featuredImageSettingViewModel.editFeaturedImage(
                    editIndex!!,
                    featuredImage = FeaturedImageData(
                        image = featuredImages[editIndex!!].image, description = it
                    )
                )

                editIndex = null
            }
        }
    }

    if(deleteIndex != null) {
        WarningDialog(
            title = "이미지를 삭제할까요?",
            warningContent = null,
            content = null,
            actionText = "삭제",
            onDismiss = {
                deleteIndex = null
            },
            onAction = {
                featuredImageSettingViewModel.deleteFeaturedImage(deleteIndex!!)
                deleteIndex = null
            }
        )
    }
}

@Composable
fun FeaturedImagesReorderList(
    featuredImages: List<FeaturedImageData>,
    scrollBehavior: TopAppBarScrollBehavior,
    onMoved: (Int, Int) -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit
) {
    val view = LocalView.current

    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onMoved(from.index, to.index)
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        itemsIndexed(featuredImages, key = { _, item -> item.image }) { index, featuredImage ->
            val interactionSource = remember { MutableInteractionSource() }

            ReorderableItem(state = reorderableLazyListState, key = featuredImage.image) { isDragging ->
                Box {
                    FeaturedReorderableImageItem(
                        modifier = Modifier
                            .combinedClickable(
                                interactionSource = interactionSource,
                                indication = ripple(bounded = true),
                                onClick = { },
                                onLongClick = { }
                            )
                            .longPressDraggableHandle(
                                interactionSource = interactionSource,
                                onDragStarted = {
                                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                }
                            )
                            .padding(vertical = 8.dp),
                        featuredImageData = featuredImage,
                        onEdit = { onEdit(index) },
                        onDelete = { onDelete(index) }
                    )

                    if(isDragging) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawRect(color = Color.White.copy(alpha = 0.7f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedReorderableImageItem(
    modifier: Modifier,
    featuredImageData: FeaturedImageData,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var onSuccess by remember { mutableStateOf(false) }

    EditAndDeleteRow(
        modifier = modifier,
        onEdit = { onEdit() },
        onDelete = { onDelete() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(shape = RoundedCornerShape(8)),
            contentAlignment = Alignment.BottomStart
        ) {
            AsyncImage(
                model = featuredImageData.image,
                contentDescription = featuredImageData.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 1f),
                contentScale = ContentScale.Crop,
                onSuccess = {
                    onSuccess = true
                }
            )

            if(!featuredImageData.description.isNullOrBlank() && onSuccess) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                }

                Text(
                    text = featuredImageData.description,
                    style = storeMeTextStyle(FontWeight.Bold, 2),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun AddFeaturedImageBottomSheetContent(uri: Uri, url: String?, progress: Float, onAdd: (String) -> Unit) {
    val isLoading = url == null
    var description by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(description) {
        isError = description.length > 50
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Box {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20))
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            if(isLoading) {
                LoadingProgress(
                    progress = progress,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(20))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "사진 설명 (선택)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                if(description.isNotEmpty()){
                    IconButton(onClick = { description = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_clear),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = "간단한 이미지 설명을 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = GuideColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HighlightColor,
                errorBorderColor = ErrorColor,
                errorLabelColor = ErrorColor,
            ),
            isError = isError,
            supportingText = {
                if(isError){
                    Text(
                        text = "이미지 설명은 50자 이내로 작성해주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = ErrorColor
                    )
                }
            }
        )
        TextLengthRow(text = description, limitSize = 50)

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            buttonText = "추가",
            enabled = url != null && !isError
        ) {
            onAdd(description)
        }

        Spacer(modifier = Modifier
            .height(40.dp)
            .navigationBarsPadding()
        )
    }
}

@Composable
fun EditFeaturedImageBottomSheetContent(featuredImage: FeaturedImageData, onEdit: (String) -> Unit) {
    var description by remember { mutableStateOf(featuredImage.description ?: "") }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(description) {
        isError = description.length > 50
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        AsyncImage(
            model = featuredImage.image,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(20))
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "사진 설명 (선택)",
            style = storeMeTextStyle(FontWeight.ExtraBold, 4),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            textStyle = storeMeTextStyle(FontWeight.Normal, 1),
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                if(description.isNotEmpty()){
                    IconButton(onClick = { description = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_text_clear),
                            contentDescription = "삭제",
                            modifier = Modifier
                                .size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            },
            placeholder = {
                Text(
                    text = "간단한 이미지 설명을 입력해주세요.",
                    style = storeMeTextStyle(FontWeight.Normal, 1),
                    color = GuideColor
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = HighlightColor,
                errorBorderColor = ErrorColor,
                errorLabelColor = ErrorColor,
            ),
            isError = isError,
            supportingText = {
                if(isError){
                    Text(
                        text = "이미지 설명은 50자 이내로 작성해주세요.",
                        style = storeMeTextStyle(FontWeight.Normal, 0),
                        color = ErrorColor
                    )
                }
            }
        )
        TextLengthRow(text = description, limitSize = 50)

        Spacer(modifier = Modifier.height(40.dp))

        DefaultButton(
            buttonText = "추가",
            enabled = !isError
        ) {
            onEdit(description)
        }

        Spacer(modifier = Modifier
            .height(40.dp)
            .navigationBarsPadding()
        )
    }
}