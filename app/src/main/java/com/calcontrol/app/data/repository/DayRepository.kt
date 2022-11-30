package com.calcontrol.app.data.repository

import androidx.lifecycle.LiveData
import com.calcontrol.app.data.model.databasemodel.RecordedDay
import com.calcontrol.app.data.model.databasemodel.dao.RecordedDayDAO

class DayRepository(private val recordedDayDao: RecordedDayDAO) {

    fun getAllRecordedDays(): LiveData<List<RecordedDay>> =
        recordedDayDao.getAllRecordedDays()

    suspend fun addRecordedDay(recordedDay: RecordedDay) {
        recordedDayDao.addRecordedDay(recordedDay)
    }

    suspend fun updateRecordedDay(recordedDay: RecordedDay) {
        recordedDayDao.updateRecordedDay(recordedDay)
    }

    suspend fun deleteRecordedDay(recordedDay: RecordedDay) {
        recordedDayDao.deleteRecordedDay(recordedDay)
    }

    suspend fun deleteAllRecordedDays() {
        recordedDayDao.deleteAllRecordedDays()
    }

    fun getRecordedDayFromDateLiveData(day_date: String, plan_id: Int): LiveData<RecordedDay> =
        recordedDayDao.getRecordedDayFromDateLiveData(day_date, plan_id)

    suspend fun doesDayWithThisPlanExist(day_date: String, plan_id: Int): Boolean {
        return recordedDayDao.doesDayWithThisPlanExist(day_date, plan_id)
    }

}