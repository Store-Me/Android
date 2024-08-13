package com.store_me.storeme.ui.post

import androidx.lifecycle.ViewModel
class AddPostViewModel: ViewModel() {

    enum class LayoutItem(val displayName: String) {
        TEXT("텍스트"), IMAGE("이미지"),
    }

}