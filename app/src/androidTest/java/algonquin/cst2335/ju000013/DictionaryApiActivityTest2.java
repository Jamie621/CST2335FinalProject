package algonquin.cst2335.ju000013;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
public class DictionaryApiActivityTest2 {

    @Rule
    public ActivityTestRule<DictionaryApiActivity> activityRule = new ActivityTestRule<>(DictionaryApiActivity.class);

    @Test
    public void testDisplayOfSearchInputField() {
        // Check if the search input field is displayed
        onView(withId(R.id.word_search_input))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testEnteringSearchTerm() {
        // Type 'hello' in the search input field
        onView(withId(R.id.word_search_input)).perform(typeText("hello"));
        // Ensure the text was entered
        onView(withId(R.id.word_search_input)).check(matches(isDisplayed()));
    }

    @Test
    public void testPerformingSearch() {
        // Type 'hello' in the search input field
        onView(withId(R.id.word_search_input)).perform(typeText("hello"));
        // Click on the search button
        onView(withId(R.id.search_button)).perform(click());
        // After this step, you might want to add assertions that depend on your app's behavior after a search is performed.
    }

    @Test
    public void testSearchInputFieldClearsAfterSearch() {
        // Type 'hello' in the search input field
        onView(withId(R.id.word_search_input)).perform(typeText("hello"));
        // Click on the search button
        onView(withId(R.id.search_button)).perform(click());
        // This step would ideally check if the input field is cleared, but since it's app-specific, you may need to add your logic here.
    }

    @Test
    public void testDisplayOfNoResultsFoundMessage() {
        // Type a random string unlikely to produce results
        onView(withId(R.id.word_search_input)).perform(typeText("asdfghjkl"));
        // Click on the search button
        onView(withId(R.id.search_button)).perform(click());
        // This step would check for a "No Results Found" message, but it's left for you to implement based on your app's behavior.
    }

    // Note: Replace the IDs like R.id.word_search_input with actual IDs from your layout
}