package com.example.plantguideapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantguideapp.data.Plant
import com.example.plantguideapp.data.PlantDatabase
import com.example.plantguideapp.repository.PlantRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PlantViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantRepository(
        PlantDatabase.getDatabase(application).plantDao()
    )

    fun getPlantById(id: Int): Flow<Plant?> = repository.getPlantById(id)

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> get() = _category

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    fun setCategory(cat: String) {
        _category.value = cat
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val favorites: StateFlow<List<Plant>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun toggleFavorite(plant: Plant) {
        viewModelScope.launch {
            repository.setFavorite(plant.id, !plant.isFavorite)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val plants = combine(_category, _searchQuery) { category, query ->
        category to query
    }.flatMapLatest { (category, query) ->
        repository.getPlantsByCategory(category).map { plants ->
            if (query.isBlank()) plants
            else plants.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            val existing = repository.getAllPlants().first()
            if (existing.isEmpty()) {
                repository.insertSampleData(
                    listOf(
                        Plant(
                            name = "Базилик",
                            description = "Травянистое растение для гидропоники",
                            longDescription = "---",
                            category = "Травы",
                            imageResName = "basil"
                        ),
                        Plant(
                            name = "Фикус",
                            description = "Комнатное растение, хорошо растёт в воде",
                            longDescription = "---",
                            category = "Комнатные растения",
                            imageResName = "ficus"
                        ),
                        Plant(
                            name = "Томат",
                            description = "Популярный овощ для теплиц и огорода",
                            longDescription = "---",
                            category = "Овощи",
                            imageResName = "tomato"
                        )
                    )
                )
            }
        }
    }
}