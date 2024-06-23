package com.example.codenames;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    @RunWith(AndroidJUnit4.class)
    public class CreateGameViewTest {

        @Rule
        public ActivityScenarioRule<MainActivity> activityRule =
                new ActivityScenarioRule<>(MainActivity.class);

        @Test
        public void testButtonClickOpensNewActivity() {
            onView(withId(R.id.create_game_btn)).perform(click());
            onView(withId(R.id.invite_players)).check(matches(withText("invite_players geladen")));
        }
    }

    @RunWith(AndroidJUnit4.class)
    public class JoinGameViewTest {

        @Rule
        public ActivityScenarioRule<MainActivity> activityRule =
                new ActivityScenarioRule<>(MainActivity.class);

        @Test
        public void testButtonClickOpensNewActivity() {
            onView(withId(R.id.join_game_btn)).perform(click());
            onView(withId(R.id.join_game)).check(matches(withText("join_game geladen")));
        }
    }

    @RunWith(AndroidJUnit4.class)
    public class gameRulesViewTest {

        @Rule
        public ActivityScenarioRule<MainActivity> activityRule =
                new ActivityScenarioRule<>(MainActivity.class);

        @Test
        public void testButtonClickOpensNewActivity() {
            onView(withId(R.id.game_rules_btn)).perform(click());
            onView(withId(R.id.game_rules)).check(matches(withText("game_rules geladenD")));
        }
    }





}