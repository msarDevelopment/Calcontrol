package com.calcontrol.app.data.model.databasemodel.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.calcontrol.app.data.model.databasemodel.RecordedDay

@Dao
interface RecordedDayDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRecordedDay(recordedDay: RecordedDay)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecordedDay(recordedDay: RecordedDay)

    @Delete
    suspend fun deleteRecordedDay(recordedDay: RecordedDay)

    @Query("SELECT * FROM recorded_day ORDER BY day_id DESC")
    fun getAllRecordedDays(): LiveData<List<RecordedDay>>

    @Query("DELETE FROM recorded_day")
    suspend fun deleteAllRecordedDays()

    @Query("SELECT * FROM recorded_day WHERE recorded_day_date = :day_date AND plan_id = :plan_id")
    fun getRecordedDayFromDateLiveData(day_date: String, plan_id: Int): LiveData<RecordedDay>

    @Query("SELECT EXISTS(SELECT * FROM recorded_day WHERE recorded_day_date = :day_date AND plan_id = :plan_id)")
    suspend fun doesDayWithThisPlanExist(day_date: String, plan_id: Int): Boolean

}