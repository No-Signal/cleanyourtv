package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OpenOnStartTest {
    private static final String SELF = "com.gathin.tvhome";
    private static final String YOUTUBE = "com.google.android.youtube.tv";

    @Test public void firstHomeOfANewBootOpensTheApp() {
        assertTrue(OpenOnStart.due(12, 11, YOUTUBE, SELF));
        assertTrue(OpenOnStart.due(0, -1, YOUTUBE, SELF));
    }

    @Test public void laterHomeOfTheSameBootStaysHome() {
        assertFalse(OpenOnStart.due(12, 12, YOUTUBE, SELF));
    }

    @Test public void unknownBootCountStaysHome() {
        assertFalse(OpenOnStart.due(-1, -1, YOUTUBE, SELF));
    }

    @Test public void offStaysHome() {
        assertFalse(OpenOnStart.due(12, 11, "", SELF));
        assertFalse(OpenOnStart.due(12, 11, null, SELF));
    }

    @Test public void theLauncherItselfIsNeverOpened() {
        assertFalse(OpenOnStart.due(12, 11, SELF, SELF));
    }

    @Test public void onlineTheAppOpensAtOnce() {
        assertTrue(OpenOnStart.opensNow(true, 1_000, 60_000));
    }

    @Test public void offlineTheAppWaitsUntilTheEndOfTheWait() {
        assertFalse(OpenOnStart.opensNow(false, 59_999, 60_000));
        assertTrue(OpenOnStart.opensNow(false, 60_000, 60_000));
    }

    private static final String HDMI1 = "com.tcl.tvinput/.TvPassThroughService/HW15";

    @Test public void powerOnInputSwitchesWhileTheWindowIsOpen() {
        assertTrue(OpenOnStart.inputDue(HDMI1, 61_000, 2_000));
    }

    @Test public void powerOnInputStaysHomeOnceTheWindowCloses() {
        assertFalse(OpenOnStart.inputDue(HDMI1, 61_000, 61_000));
        assertFalse(OpenOnStart.inputDue(HDMI1, 0, 2_000));   // a key, a switch or leaving Home
    }

    @Test public void powerOnInputOffStaysHome() {
        assertFalse(OpenOnStart.inputDue("", 61_000, 2_000));
        assertFalse(OpenOnStart.inputDue(null, 61_000, 2_000));
    }
}
