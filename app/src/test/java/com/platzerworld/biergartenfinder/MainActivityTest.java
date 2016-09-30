package com.platzerworld.biergartenfinder;

import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    private BiergartenActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(BiergartenActivity.class);
    }

    @Test
    public void clickButton() {
        Button button = (Button) activity.findViewById(R.id.btnGooglePlayServices);
        assertNotNull("test button could not be found", button);
        assertTrue("button does not contain text 'Click Me!'", "Click Me".equals(button.getText()));
    }

}
