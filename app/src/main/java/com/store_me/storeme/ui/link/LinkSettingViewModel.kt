package com.store_me.storeme.ui.link

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LinkSettingViewModel: ViewModel() {
    private val _links: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val links: StateFlow<List<String>> = _links

    fun updateLinks(newLinks: List<String>) {
        _links.value = newLinks
    }

    fun reorderLinks(fromIndex: Int, toIndex: Int) {
        val currentLinks = _links.value.toMutableList()
        val movedItem = currentLinks.removeAt(fromIndex)
        currentLinks.add(toIndex, movedItem)
        _links.value = currentLinks.toList()
    }

    fun addLink(link: String) {
        val currentLinks = _links.value.toMutableList()
        currentLinks.add(link)
        _links.value = currentLinks
    }

    fun editLink(index: Int, link: String) {
        _links.value = _links.value.toMutableList().apply {
            this[index] = link
        }
    }

    fun deleteLink(index: Int) {
        val currentLinks = _links.value.toMutableList()
        currentLinks.removeAt(index)
        _links.value = currentLinks
    }
}