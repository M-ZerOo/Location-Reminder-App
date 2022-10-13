package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(
    var reminders: MutableList<ReminderDTO>? = mutableListOf()
) : ReminderDataSource {

    var shouldReturnError = false

    fun itShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    // Get reminders method used by the FakeDataSource
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error("Reminders list not found!")
        }
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error("Reminders list not found!")
    }

    // Save a reminder method used by the FakeDataSource
    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    // Get a reminder by id method used by the FakeDataSource
    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) {
            return Result.Error("Reminder not found!")
        }
        val reminder = reminders?.find {
            it.id == id
        }
        return if (reminder != null) {
            Result.Success(reminder)
        } else {
            Result.Error("Reminder not found!")
        }
    }

    // Delete a reminder by id method used by the FakeDataSource
    override suspend fun deleteReminder(id: String) {
        val reminder = reminders?.find {
            it.id == id
        }
        reminders?.remove(reminder)
    }

    // Delete all reminders method used by the FakeDataSource
    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }


}