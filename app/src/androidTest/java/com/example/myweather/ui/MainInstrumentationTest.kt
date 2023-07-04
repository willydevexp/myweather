package com.example.myweather.ui

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.devexperto.architectcoders.fromJson
import com.example.myweather.R
import com.example.myweather.data.datasource.WeatherRemoteDataSource
import com.example.myweather.framework.database.AppDao
import com.example.myweather.server.MockWebServerRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
class MainInstrumentationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()

    @get:Rule(order = 2)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule(order = 3)
    val activityRule = ActivityScenarioRule(NavHostActivity::class.java)

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appDao: AppDao

    @Inject
    lateinit var remoteDataSource: WeatherRemoteDataSource

    @Before
    fun setUp() {

        /*
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson("default_weather.json")
        )
         */

        mockWebServerRule.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path
                if (path != null) {
                    if (path.contains("forecast/daily")) {
                        return MockResponse().fromJson("default_weather.json")
                    } else if (path.contains("geo/1.0/direct")) {
                        return MockResponse().fromJson("find_london_location.json")
                    } else  {
                        return MockResponse().setResponseCode(404)
                    }
                } else
                    return MockResponse().setResponseCode(404)
            }
        }

        hiltRule.inject()

        val resource = OkHttp3IdlingResource.create("OkHttp", okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun click_last_location_shows_weather() {
        val cardView = Espresso.onView(
            Matchers.allOf(
                withId(R.id.cardlastLocation),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.myCoordinatorLayout),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        )
        cardView.perform(ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withText("My Weather - Mountain View - US"),
                ViewMatchers.withParent(
                    Matchers.allOf(
                        withId(R.id.toolbar),
                        ViewMatchers.withParent(IsInstanceOf.instanceOf(LinearLayout::class.java))
                    )
                ),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("My Weather - Mountain View - US")))
    }

    @Test
    fun add_london_location_shows_london_in_list_of_locations() {
        val floatingActionButton = Espresso.onView(
            Matchers.allOf(
                withId(R.id.fab),
                childAtPosition(
                    Matchers.allOf(
                        withId(R.id.myCoordinatorLayout),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        )
        floatingActionButton.perform(ViewActions.click())

        val appCompatEditText = Espresso.onView(
            Matchers.allOf(
                withId(R.id.txtLocation),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.custom),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        )
        appCompatEditText.perform(
            ViewActions.replaceText("London"),
            ViewActions.closeSoftKeyboard()
        )

        val materialButton = Espresso.onView(
            Matchers.allOf(
                withId(android.R.id.button1), ViewMatchers.withText("OK"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton.perform(ViewActions.scrollTo(), ViewActions.click())

        val textView = Espresso.onView(
            Matchers.allOf(
                withId(R.id.txtLocation), ViewMatchers.withText("London - GB"),
                ViewMatchers.withParent(ViewMatchers.withParent(withId(R.id.locationItem))),
                ViewMatchers.isDisplayed()
            )
        )
        textView.check(ViewAssertions.matches(ViewMatchers.withText("London - GB")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    /*
    @Test
    fun test_it_works() {
        Thread.sleep(2000)
    }

    @Test
    fun check_4_items_db() = runTest {
        appDao.insertWeatherList(buildWeatherList(1,2,3,4))
        Assert.assertEquals(4, appDao.weatherCount())
    }

    @Test
    fun check_6_items_db() = runTest {
        appDao.insertWeatherList(buildWeatherList(1,2,3,4, 5,6))
        Assert.assertEquals(6, appDao.weatherCount())
    }

    @Test
    fun check_mock_server_is_working() = runTest {
        val weatherList = remoteDataSource.getDailyWeather(DEFAULT_LOCATION)
        weatherList.fold({ throw Exception(it.toString()) }) {
            Assert.assertEquals(1688068800, it[0].dt)
        }
    }

     */
}