import android.os.SystemClock
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.shippedsuite.example.MainActivity
import com.shippedsuite.example.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExampleTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testWidgetView() {
        Espresso.onView(ViewMatchers.withId(R.id.widget_view)).check(matches(isDisplayed()))
    }

    @Test
    fun testLearnMoreDialog() {
        Espresso.onView(ViewMatchers.withId(R.id.display_learn_more_model))
            .perform(click())

        SystemClock.sleep(2000)

        Espresso.onView(withId(com.invisiblecommerce.shippedsuite.R.id.shipped_title)).check(matches(isDisplayed()))
    }

    @Test
    fun testSendOffersFeeRequest() {
        Espresso.onView(ViewMatchers.withId(R.id.send_offers_fee_request)).perform(click())
        Thread.sleep(100)
    }
}
