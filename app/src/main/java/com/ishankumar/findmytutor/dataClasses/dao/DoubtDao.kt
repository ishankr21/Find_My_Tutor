package com.ishankumar.findmytutor.dataClasses.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ishankumar.findmytutor.dataClasses.DoubtInfo

@Dao
interface DoubtDao {
    @Query("SELECT * FROM doubt_info")
    fun getAllDoubts(): LiveData<MutableList<DoubtInfo>>

    @Insert
    suspend fun insert(doubtInfo: DoubtInfo)

    @Query("DELETE FROM doubt_info")
    suspend fun deleteAll()

    @Query("SELECT * FROM doubt_info where isClosed = 1 ")
    fun getAllClosedDoubt():LiveData<MutableList<DoubtInfo>>

    @Query("SELECT * FROM doubt_info where isClosed = 0 ")
    fun getAllOpenDoubt():LiveData<MutableList<DoubtInfo>>

    @Query("SELECT * FROM doubt_info ORDER BY createdOn DESC")
    fun getDoubtLatestFirst(): LiveData<MutableList<DoubtInfo>>

    @Query("SELECT * FROM doubt_info ORDER BY createdOn ASC")
    fun getDoubtOldestFirst(): LiveData<MutableList<DoubtInfo>>

    @Query("SELECT * FROM doubt_info ORDER BY noOfSolutions DESC")
    fun getDoubtNoOfSolutionsHighToLow(): LiveData<MutableList<DoubtInfo>>

    @Query("SELECT * FROM doubt_info ORDER BY noOfSolutions ASC")
    fun getDoubtNoOfSolutionsLowToHigh(): LiveData<MutableList<DoubtInfo>>

}