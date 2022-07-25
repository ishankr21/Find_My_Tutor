package com.ishankumar.findmytutor.dataRepo

import android.content.Context
import androidx.lifecycle.LiveData
import com.ishankumar.findmytutor.dataClasses.DoubtInfo
import com.ishankumar.findmytutor.dataClasses.database.FindMyTutorDb

class LocalDataRepo(context: Context) {

    private val doubtDao= FindMyTutorDb.initDatabase(context).doubtDao

    fun getAllDoubts(): LiveData<MutableList<DoubtInfo>> =
        doubtDao.getAllDoubts()
    suspend fun insertDoubts(doubtInfo: DoubtInfo) =
        doubtDao.insert(doubtInfo)

    suspend fun deleteAllDoubts() =
        doubtDao.deleteAll()

    fun getAllOpenDoubt():LiveData<MutableList<DoubtInfo>> = doubtDao.getAllOpenDoubt()
    fun getAllClosedDoubt():LiveData<MutableList<DoubtInfo>> = doubtDao.getAllClosedDoubt()

    fun getLatestDoubtFirst():LiveData<MutableList<DoubtInfo>> = doubtDao.getDoubtLatestFirst()
    fun getOldestDoubtFirst():LiveData<MutableList<DoubtInfo>> = doubtDao.getDoubtOldestFirst()

    fun getNoOfSolutionsHighToLow():LiveData<MutableList<DoubtInfo>> = doubtDao.getDoubtNoOfSolutionsHighToLow()
    fun getNoOfSolutionsLowToHigh():LiveData<MutableList<DoubtInfo>> = doubtDao.getDoubtNoOfSolutionsLowToHigh()

}