package com.udacity.project4.locationreminders.data.local

import android.text.TextUtils.replace
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    private lateinit var database: RemindersDatabase

    private val reminder1 = ReminderDTO(
        "Title1", "Description1", "Location1", 5.0, 5.0, "1"
    )
    private val reminder2 = ReminderDTO(
        "Title2", "Description2", "Location2", 10.0, 10.0, "2"
    )
    private val reminder3 = ReminderDTO(
        "Title3", "Description3", "Location3", 15.0, 15.0, "3"
    )

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Setup database for test, using in-memory database because information stored here
    // disappears when the process is killed
    @Before
    fun setupDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    // Close database after finishing test
    @After
    fun cleanUpDb() {
        database.close()
    }

    // Save 3 new reminders to database then call getAll function and check if they were 3 reminders
    @Test
    fun insertAndGetReminders() = runBlockingTest {
        // Given
        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        // When
        val getAll = database.reminderDao().getReminders()

        // Then
        assertThat(getAll.size, `is`(3))
    }

    // Save a new reminder and call getReminderById and check if the reminder details is the same
    @Test
    fun getReminderById() = runBlockingTest {
        // Given
        database.reminderDao().saveReminder(reminder1)

        //When
        val reminder = database.reminderDao().getReminderById(reminder1.id)

        // Then
        assertThat(reminder as ReminderDTO, `is`(notNullValue()))
        assertThat(reminder.title, `is`(reminder1.title))
        assertThat(reminder.description, `is`(reminder1.description))
        assertThat(reminder.location, `is`(reminder1.location))
        assertThat(reminder.latitude, `is`(reminder1.latitude))
        assertThat(reminder.longitude, `is`(reminder1.longitude))
        assertThat(reminder.id, `is`(reminder1.id))
    }

    // Save 3 new reminders and call deleteAll then check if the database is empty
    @Test
    fun deleteAll() = runBlockingTest {
        // Given
        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        database.reminderDao().deleteAllReminders()

        // When
        val reminders = database.reminderDao().getReminders()

        // Then
        assertThat(reminders.size, `is`(0))
    }


    // Save 3 new reminders and delete the first reminder then check if database has only 2 reminders in
    // the list the first reminder in the list is the second from the old list
    @Test
    fun deleteReminderById() = runBlockingTest {
        // Given
        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)

        database.reminderDao().deleteReminderById(reminder1.id)

        // When
        val reminders = database.reminderDao().getReminders()

        // Then
        assertThat(reminders.size, `is`(2))
        assertThat(reminders[0].id, `is`(reminder2.id))
    }
    

}