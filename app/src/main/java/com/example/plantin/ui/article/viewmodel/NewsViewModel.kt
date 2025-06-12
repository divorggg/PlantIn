package com.example.plantin.ui.article.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantin.ui.article.dataclass.Article
import com.example.plantin.ui.article.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    var articles by mutableStateOf<List<Article>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        loadNews()
    }

    private fun loadNews() {
        viewModelScope.launch {
            try {
                isLoading = true
                articles = repository.fetchNews()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}
