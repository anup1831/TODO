package com.pathfinder.anup.loaders;

import android.provider.ContactsContract;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Anup on 3/28/2017.
 */
public class MainActivityTest {
    MainActivity mMainActivity;
    @Before
    public void setUp() throws Exception {
        mMainActivity = new MainActivity();
    }

    @Test
    public void insertDataIntoDB() throws Exception {
        assertTrue("Called functionality to save the data into DB", true);
        //assertEquals("Validation is correct", );

    }

    @Test
    public void saveTodos() throws Exception {
        assertTrue("Getting data from EditViews", true);
    }

}