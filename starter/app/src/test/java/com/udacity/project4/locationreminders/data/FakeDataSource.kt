package com.udacity.project4.locationreminders.data

import androidx.lifecycle.LiveData
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    var reminderDtoList = mutableListOf<ReminderDTO>()
    private var shouldReturnError = false

    fun shouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return try {
            if(shouldReturnError) {
                throw Exception("Reminders not found")
            }
            Result.Success(ArrayList(reminderDtoList))
        } catch (ex: Exception) {
            Result.Error(ex.localizedMessage)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {

            reminderDtoList.add(reminder)


    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        return try {
            val reminder = reminderDtoList.find { it.id == id }
            if (shouldReturnError || reminder == null) {
                throw Exception("Not found $id")
            } else {
                Result.Success(reminder)
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        reminderDtoList.clear()
    }


}