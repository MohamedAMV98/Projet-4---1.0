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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
public class ExampleInstrumentedTest {

    private MainActivity mMainActivity;
    private MeetingListManagement mApiService;
    List<Meeting> mMeetingList;
    private int originalItemNumber;
    public String Room;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp(){
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
    public void meetingListIsDisplayed(){
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void addMeetingOpensAndWorks(){
        // DATE
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.date_button)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 5, 20));
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
        onView(withText("Room G")).perform(click());
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
    public void deleteMeetingWorks(){
        //onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        /*onView(withId(R.id.delete_button)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));*/
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
    public void filterByDateAndRoom(){
        // BY DATE
        addRandomMeeting();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Date")).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 5, 20));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));

        // BY ROOM
        /*openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("No Filter")).perform(click());*/
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Room")).perform(click());
        onView(withText("Room A")).perform(click());
        onView(withId(R.id.meeting_place)).check(matches(withText(Room)));

    }

    @Test
    public void ResetFilter(){
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Assembly Room")).perform(click());
        onView(withText("Room A")).perform(click());
        onView(withId(R.id.meeting_place)).check(matches(withText(Room)));
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("No Filter")).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(originalItemNumber)));
    }

    public void addRandomMeeting(){
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

    @Test
    public void availabilityTest(){
        create2meetings();
        assertTrue(mMeetingList.size() == 5);
        onView(withText("Alert")).check(matches(isDisplayed()));
    }

    public void create2meetings() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.main_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.date_button), withText("MEETING DAY"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.time_button), withText("FROM"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton4.perform(scrollTo(), click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.time_button2), withText("TO"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton6.perform(scrollTo(), click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.assembly_room), withText("ASSEMBLY ROOM"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton7.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.title), withText("Room A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.subject_text2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.subject_text),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("Exa"), closeSoftKeyboard());

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.mail_entered),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                1),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("@."), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.add_mail_button), withContentDescription("TODO"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.add_button), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.main_activity),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                0),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.date_button), withText("MEETING DAY"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton9.perform(click());

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.time_button), withText("FROM"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                0),
                        isDisplayed()));
        appCompatButton11.perform(click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton12.perform(scrollTo(), click());

        ViewInteraction appCompatButton13 = onView(
                allOf(withId(R.id.time_button2), withText("TO"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        appCompatButton13.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        appCompatButton14.perform(scrollTo(), click());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.assembly_room), withText("ASSEMBLY ROOM"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton15.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.title), withText("Room A"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.subject_text2),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.subject_text),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(replaceText("Exa"), closeSoftKeyboard());

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.mail_entered),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                1),
                        isDisplayed()));
        textInputEditText4.perform(replaceText("@."), closeSoftKeyboard());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.add_mail_button), withContentDescription("TODO"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(R.id.add_button), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.add_meeting_activity),
                                        1),
                                0),
                        isDisplayed()));
        appCompatButton16.perform(click());
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
}
