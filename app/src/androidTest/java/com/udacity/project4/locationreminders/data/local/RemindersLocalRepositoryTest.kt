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

    // Executes each task synchronously using Architecture Components
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

    // Setup database for test, using in-memory database because information stored here
    // disappears when the process is killed
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

    // Close database after finishing test
    @After
    fun cleanUp() {
        database.close()
    }

    // Save 3 new reminders to database then call getAll function and check if they were 3 reminders
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

    // Save a new reminder and call getReminderById and check if the reminder details is the same
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

    // Save 3 new reminders and delete the first reminder then check if database has only 2 reminders in
    // the list the first reminder in the list is the second from the old list
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

    // Save 3 new reminders and call deleteAll then check if the database is empty
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

    // Save a new reminder then delete all from database then try to call this reminder and check
    // that the result message would be as expected
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