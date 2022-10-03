package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    private lateinit var saveReminderViewModel: SaveReminderViewModel
    private lateinit var fakeDataSource: FakeDataSource

    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // reminder1 is correct, reminder2 with no title, reminder3 with no location
    private val reminder1 = ReminderDataItem(
        "Title1", "Description1", "Location1", 5.0, 5.0, "1"
    )
    private val reminder2 = ReminderDataItem(
        "", "Description2", "Location2", 10.0, 10.0, "2"
    )
    private val reminder3 = ReminderDataItem(
        "Title3", "Description3", "", 15.0, 15.0, "3"
    )

    @Before
    fun setupViewModel() {
        stopKoin()
        fakeDataSource = FakeDataSource()
        saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    @Test
    fun onClear_clearLiveDataObjects() {
        // Given
        saveReminderViewModel.apply {
            reminderTitle.value = reminder1.title
            reminderDescription.value = reminder1.description
            reminderSelectedLocationStr.value = reminder1.location
            latitude.value = reminder1.latitude
            longitude.value = reminder1.longitude
            reminderId.value = reminder1.id

        }

        // When
        saveReminderViewModel.onClear()

        // Then
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is`(nullValue()))
        assertThat(saveReminderViewModel.reminderId.getOrAwaitValue(), `is`(nullValue()))
    }

    @Test
    fun saveReminder_addReminderToDataSource() = mainCoroutineRule.runBlockingTest {
        // When
        saveReminderViewModel.saveReminder(reminder1)
        val check = fakeDataSource.getReminder("1") as Result.Success

        // Then
        assertThat(check.data.title, `is`(reminder1.title))
        assertThat(check.data.description, `is`(reminder1.description))
        assertThat(check.data.location, `is`(reminder1.location))
        assertThat(check.data.latitude, `is`(reminder1.latitude))
        assertThat(check.data.longitude, `is`(reminder1.longitude))
        assertThat(check.data.id, `is`(reminder1.id))
    }

    @Test
    fun editReminder_liveDataEdited() {
        // When
        saveReminderViewModel.editReminder(reminder1)

        // Then
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`(reminder1.title))
        assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(), `is`(reminder1.description))
        assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(), `is`(reminder1.location))
        assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is`(reminder1.latitude))
        assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is`(reminder1.longitude))
        assertThat(saveReminderViewModel.reminderId.getOrAwaitValue(), `is`(reminder1.id))
    }

    @Test
    fun validateData_noTitle_returnFalse() {
        // When
        val trial = saveReminderViewModel.validateEnteredData(reminder2)

        // Then
        assertThat(trial, `is`(false))
    }

    @Test
    fun validateData_noLocation_returnFalse() {
        // When
        val trial = saveReminderViewModel.validateEnteredData(reminder3)

        // Then
        assertThat(trial, `is`(false))
    }

}