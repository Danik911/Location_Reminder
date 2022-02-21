package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.rule.MainCoroutineRule

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    private lateinit var fakeReminderDataSource: FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setupViewModel() {
        fakeReminderDataSource = FakeDataSource()
        saveReminderViewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeReminderDataSource
        )
    }
    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun shouldReturnError() = runTest {
        val result = saveReminderViewModel.validateEnteredData(createIncompleteReminderDataItem())
        assertThat(result, `is`(false))
    }

    private fun createIncompleteReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            null,
            "Description",
            "location",
            10.00,
            10.00
        )
    }

    @Test
    fun check_loading() = runTest {


        saveReminderViewModel.saveReminder(createFakeReminderDataItem())

        assertThat(saveReminderViewModel.showLoading.value, `is`(true))
        advanceUntilIdle()

        assertThat(saveReminderViewModel.showLoading.value, `is`(false))
    }

    private fun createFakeReminderDataItem(): ReminderDataItem {
        return ReminderDataItem(
            "Title",
            "Description",
            "location",
            10.00,
            10.00
        )
    }

}