package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val reminder1 = ReminderDTO(
        "Title1", "Description1", "Location1", 5.0, 5.0, "1"
    )
    private val reminder2 = ReminderDTO(
        "Title2", "Description2", "Location2", 10.0, 10.0, "2"
    )
    private val reminder3 = ReminderDTO(
        "Title3", "Description3", "Location3", 15.0, 15.0, "3"
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        remindersLocalRepository =
            RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveReminders_getReminders() = runBlocking {
        // Given
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)
        remindersLocalRepository.saveReminder(reminder3)

        // When
        val result = remindersLocalRepository.getReminders() as Result.Success

        // Then
        assertThat(result.data.size, `is`(3))
    }

    @Test
    fun saveReminder_getReminderById() = runBlocking {
        // Given
        remindersLocalRepository.saveReminder(reminder1)

        // When
        val result = remindersLocalRepository.getReminder(reminder1.id) as Result.Success

        // Then
        assertThat(result.data.title, `is`(reminder1.title))
        assertThat(result.data.description, `is`(reminder1.description))
        assertThat(result.data.location, `is`(reminder1.location))
        assertThat(result.data.latitude, `is`(reminder1.latitude))
        assertThat(result.data.longitude, `is`(reminder1.longitude))
        assertThat(result.data.id, `is`(reminder1.id))
    }

    @Test
    fun saveReminders_deleteReminderById() = runBlocking {
        // Given
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)
        remindersLocalRepository.saveReminder(reminder3)

        // When
        remindersLocalRepository.deleteReminder(reminder1.id)
        val result = remindersLocalRepository.getReminders() as Result.Success

        // Then
        assertThat(result.data.size, `is`(2))
        assertThat(result.data[0].id, `is`(reminder2.id))
    }

    @Test
    fun saveReminders_deleteAllReminders() = runBlocking {
        // Given
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.saveReminder(reminder2)
        remindersLocalRepository.saveReminder(reminder3)

        // When
        remindersLocalRepository.deleteAllReminders()
        val result = remindersLocalRepository.getReminders() as Result.Success

        // Then
        assertThat(result.data.size, `is`(0))
    }

    @Test
    fun getReminderById_returnsError() = runBlocking {
        // Given
        remindersLocalRepository.saveReminder(reminder1)
        remindersLocalRepository.deleteAllReminders()

        // When
        val result = remindersLocalRepository.getReminder(reminder1.id) as Result.Error

        // Then
        assertThat(result.message, `is`("Reminder not found!"))

    }

}