package com.udacity.project4.locationreminders.reminderslist

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : KoinTest {

    private val dataSource: ReminderDataSource by inject()
    private val reminder1 = ReminderDTO(
        "Title1", "Description1", "Location1", 5.0, 5.0, "1"
    )
    private val reminder2 = ReminderDTO(
        "Title2", "Description2", "Location2", 10.0, 10.0, "2"
    )
    private val reminder3 = ReminderDTO(
        "Title3", "Description3", "Location3", 15.0, 15.0, "3"
    )

    // Declare a ViewModel and prepare our koin for test
    @Before
    fun initRepository() {
        stopKoin() // Stop the original app koin

        val myModule = module {
            viewModel {
                RemindersListViewModel(get(), get())
            }
            single<ReminderDataSource> { FakeDataSource() }
        }

        startKoin {
            androidContext(getApplicationContext())
            modules(listOf(myModule))
        }
    }

    // Clean up data from any reminders after any test finish
    @After
    fun cleanUp() = runBlockingTest {
        dataSource.deleteAllReminders()
    }

    // Save 3 new reminders then check if the UI is displayed correctly
    @Test
    fun remindersListDisplayedInUi() = runBlockingTest {
        // Given
        dataSource.saveReminder(reminder1)
        dataSource.saveReminder(reminder2)
        dataSource.saveReminder(reminder3)

        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Then
        onView(withText(reminder1.title)).check(matches(isDisplayed()))
        onView(withText(reminder2.description)).check(matches(isDisplayed()))
        onView(withText(reminder3.location)).check(matches(isDisplayed()))
        onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
    }

    // Delete all reminders and check if "No Data" icon and text are displayed
    @Test
    fun remindersListIsEmpty() = runBlockingTest {
        // Given
        dataSource.deleteAllReminders()

        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Then
        onView(withId(R.id.noDataTextView)).check(matches(isDisplayed()))
        onView(withText(R.string.no_data)).check(matches(isDisplayed()))
    }

    // Being on reminders list fragment, check if "Add Reminder" button navigates to save reminder fragment
    @Test
    fun addReminderButtonNavigation() {
        // Given
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // When
        onView(withId(R.id.addReminderFAB)).perform(click())

        // Then
        verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }


}