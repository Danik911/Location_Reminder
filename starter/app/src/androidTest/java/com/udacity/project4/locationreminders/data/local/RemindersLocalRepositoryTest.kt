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
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
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


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup(){
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()

        repository = RemindersLocalRepository(database.reminderDao())
    }
    @After
    fun cleanUp() = database.close()


    @Test
    fun testInsertRetrieveData() = runTest {

        val data = ReminderDTO(
            "title",
            "description",
            "location",
            10.00,
            10.00)

        repository.saveReminder(data)

        val result = repository.getReminder(data.id)

        result as Result.Success
        assertThat(true, `is`(true))

        val loadedData = result.data
        assertThat(loadedData.id, `is`(data.id))
        assertThat(loadedData.title, `is`(data.title))
        assertThat(loadedData.description, `is`(data.description))
        assertThat(loadedData.location, `is`(data.location))
        assertThat(loadedData.latitude, `is`(data.latitude))
        assertThat(loadedData.longitude, `is`(data.longitude))
    }

    @Test
    fun testDataNotFound_returnError() = runTest {
        val result = repository.getReminder("22")
        val error =  (result is Result.Error)
        assertThat(error,`is`(true))
    }

}