package com.udacity.project4.locationreminders.reminderslist

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.rule.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//@Config(sdk = [Build.VERSION_CODES.P])
class RemindersListViewModelTest : AutoCloseKoinTest() {

    private lateinit var fakeReminderDataSource: FakeDataSource
    private lateinit var reminderViewModel: RemindersListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeReminderDataSource = FakeDataSource()
        reminderViewModel = RemindersListViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeReminderDataSource
        )
    }

    @Test
    fun testShouldReturnError() = runTest {
        fakeReminderDataSource.shouldReturnError(true)
        saveReminderFakeData()
        reminderViewModel.loadReminders()

        assertThat(
            reminderViewModel.showSnackBar.value, `is`("Reminders not found")
        )
    }

    @Test
    fun check_loading() = runTest {


        //mainCoroutineRule.pauseDispatcher()
        saveReminderFakeData()
        reminderViewModel.loadReminders()

        assertThat(reminderViewModel.showLoading.value, CoreMatchers.`is`(true))

        advanceUntilIdle()
        assertThat(reminderViewModel.showLoading.value, CoreMatchers.`is`(false))
    }

    private suspend fun saveReminderFakeData() {
        fakeReminderDataSource.saveReminder(
            ReminderDTO(
                "title",
                "description",
                "location",
                10.00,
                10.00
            )
        )
    }
}