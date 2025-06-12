package com.example.plantin.classification.history.dao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantin.notes.MainApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val _history = MutableStateFlow<List<DetectionHistory>>(emptyList())
    val history: StateFlow<List<DetectionHistory>> = _history

    init {
        viewModelScope.launch {
            MainApplication.database.detectionHistoryDao()
                .getAll()
                .collectLatest { data ->
                    _history.value = data
                }
        }
    }
}