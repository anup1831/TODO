package com.pathfinder.anup.loaders;

import android.support.test.espresso.Espresso.*;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;

/**
 * Created by Anup on 3/28/2017.
 */

public class MainActivityInstrumentationTest {

    public static final String TODO_STRING = "Instrumentation Test";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void CheckEditViewAndButton(){
        onView(ViewMatchers.withId(R.id.todo_title)).perform(ViewActions.typeText(TODO_STRING));
        onView(ViewMatchers.withId(R.id.todo_add)).perform(ViewActions.click());
       // String expectedText = "Hello, " + TODO_STRING + "!";

    }
}
