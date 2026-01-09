package com.example.plantguideapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants WHERE category = :category")
    fun getPlantsByCategory(category: String): Flow<List<Plant>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: Plant)

    @Query("DELETE FROM plants")
    suspend fun clearAll()

    @Query("SELECT * FROM plants WHERE id = :id")
    fun getPlantById(id: Int): Flow<Plant?>

    @Query("SELECT * FROM plants")
    fun getAllPlants(): Flow<List<Plant>>

    @Query("UPDATE plants SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM plants WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Plant>>

}