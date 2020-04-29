package com.example.gestionreunion;

import android.app.DatePickerDialog;
import android.app.LauncherActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.ContentView;
import androidx.fragment.app.DialogFragment;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.gestionreunion.API.MeetingListManagement;
import com.example.gestionreunion.DI.Injector;
import com.example.gestionreunion.Fragment.DatePickerFragment;
import com.example.gestionreunion.Fragment.TimePickerFragment;
import com.example.gestionreunion.Model.Meeting;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.sql.Time;
import java.util.List;

import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {
    private MainActivity mMainActivity;
    private MeetingListManagement mApiService;
    List<Meeting> mMeetingList;
    private int originalItemNumber;
    public String Room;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        mMainActivity = mActivityTestRule.getActivity();
        assertThat(mMainActivity, notNullValue());
        mApiService = Injector.getApiService();
        mMeetingList = mApiService.getMeetingList();
        originalItemNumber = mApiService.getMeetingList().size();
        Room = mApiService.getMeetingList().get(0).getPlace();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.example.gestionreunion", appContext.getPackageName());
    }

    @Test
    public void meetingListIsDisplayed() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void A_addMeetingOpensAndWorks() {
        // DATE
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 7, 20));
        onView(withId(android.R.id.button1)).perform(click());
        // TIME
        onView(withId(R.id.time_button)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 30));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.time_button2)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(11, 30));
        onView(withId(android.R.id.button1)).perform(click());
        // ROOM
        onView(withId(R.id.assembly_room)).perform(click());
        onView(withText("Room E")).perform(click());
        // SUBJECT
        onView(allOf(withClassName(endsWith("TextInputEditText")), withHint(is("Meeting Subject")))).perform(replaceText("Approvisionnement"));
        // PARTICIPANTS
        onView(allOf(withClassName(endsWith("EditText")), withHint(is("Enter participant email")))).perform(replaceText("worthyamv@gmail.com"));
        onView(withId(R.id.add_mail_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        // CHECK IF MEETING IS ADD TO LIST AND DISPLAYED
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(originalItemNumber + 1)));
    }

    @Test
    public void deleteMeetingWorks() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.delete_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        1),
                                5),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(android.R.id.button1), withText("YES, I DO"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton.perform(scrollTo(), click());
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(originalItemNumber - 1)));
    }

    @Test
    public void C_filterByDateAndRoom() {
        // BY DATE
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 5, 20));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));

        // BY ROOM
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Room")).perform(click());
        onView(withText("Room A")).perform(click());
        onView(withId(R.id.meeting_place)).check(matches(withText(Room)));

    }

    @Test
    public void D_ResetFilter() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Room")).perform(click());
        onView(withText("Room A")).perform(click());
        onView(withId(R.id.meeting_place)).check(matches(withText(Room)));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("No Filter")).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(originalItemNumber)));
    }


    @Test
    public void B_availabilityTest() {
        addRandomMeeting();
        addRandomMeeting2();
        assertTrue(mMeetingList.size() == 6);
        onView(withText("Alert")).check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public void addRandomMeeting() {
        // DATE
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 05, 20));
        onView(withId(android.R.id.button1)).perform(click());
        // TIME
        onView(withId(R.id.time_button)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 30));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.time_button2)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(12, 30));
        onView(withId(android.R.id.button1)).perform(click());
        // ROOM
        onView(withId(R.id.assembly_room)).perform(click());
        onView(withText("Room G")).perform(click());
        // SUBJECT
        onView(allOf(withClassName(endsWith("TextInputEditText")), withHint(is("Meeting Subject")))).perform(replaceText("Approvisionnement"));
        onView(allOf(withClassName(endsWith("EditText")), withHint(is("Enter participant email")))).perform(replaceText("worthyamv@gmail.com"));
        onView(withId(R.id.add_mail_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
    }

    public void addRandomMeeting2() {
        // DATE
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 05, 20));
        onView(withId(android.R.id.button1)).perform(click());
        // TIME
        onView(withId(R.id.time_button)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(9, 30));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.time_button2)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13, 30));
        onView(withId(android.R.id.button1)).perform(click());
        // ROOM
        onView(withId(R.id.assembly_room)).perform(click());
        onView(withText("Room G")).perform(click());
        // SUBJECT
        onView(allOf(withClassName(endsWith("TextInputEditText")), withHint(is("Meeting Subject")))).perform(replaceText("Approvisionnement"));
        onView(allOf(withClassName(endsWith("EditText")), withHint(is("Enter participant email")))).perform(replaceText("worthyamv@gmail.com"));
        onView(withId(R.id.add_mail_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
    }
}
