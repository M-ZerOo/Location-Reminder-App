package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var fakeDataSource: FakeDataSource

    private val reminder1 = ReminderDTO(
        "Title1", "Description1", "Location1", 5.0, 5.0, "1"
    )
    private val reminder2 = ReminderDTO(
        "Title1", "Description2", "Location2", 10.0, 10.0, "2"
    )

    // Executes each task synchronously using Architecture Components
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Sets the main dispatcher as TestCoroutineDispatcher
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Setup ViewModel to use a fake data source and application context for testing
    @Before
    fun setupViewModel() {
        stopKoin()
        fakeDataSource = FakeDataSource()
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }

    // Clear all reminders in the data source after finish testing
    @After
    fun cleanUp() = runBlockingTest {
        fakeDataSource.deleteAllReminders()
    }

    // Delete all reminders and call loadReminders, then check if the list is empty and
    // ShowNoData is true
    @Test
    fun invalidateShowNoData_noDataIsTrue() = mainCoroutineRule.runBlockingTest {
        // When
        fakeDataSource.deleteAllReminders()
        remindersListViewModel.loadReminders()

        // Then
        assertThat(remindersListViewModel.remindersList.getOrAwaitValue().size, `is`(0))
        assertThat(remindersListViewModel.showNoData.getOrAwaitValue(), `is`(true))
    }

    // Save 2 new reminders and call loadReminders, then check that the list size is 2 and
    // ShowNoData is false
    @Test
    fun loadReminders_showsAllReminders() = mainCoroutineRule.runBlockingTest {
        // When
        fakeDataSource.saveReminder(reminder1)
        fakeDataSource.saveReminder(reminder2)
        remindersListViewModel.loadReminders()

        // Then
        assertThat(remindersListViewModel.remindersList.getOrAwaitValue().size, `is`(2))
        assertThat(remindersListViewModel.showNoData.getOrAwaitValue(), `is`(false))
    }


}
