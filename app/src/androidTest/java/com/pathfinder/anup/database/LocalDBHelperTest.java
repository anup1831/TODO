package com.pathfinder.anup.database;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.pathfinder.anup.models.ModelDailyTodo;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Anup on 3/30/2017.
 */
public class LocalDBHelperTest {

    private LocalDBHelper database;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(LocalDBHelper.DB_NAME);
        database = new LocalDBHelper(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void shouldInsertDataIntoDB() throws Exception {
        database.insertDataIntoDB(new ModelDailyTodo(1, "Jogging", "Morning jogging is good for health"));

        List<ModelDailyTodo> todoList = database.getAllTodos();
        Log.i("Anup", "todoSize -"+todoList.size());
//      Log.i("Anup", "todoSize -"+todoList.get(1).getTodoTitles());
        assertThat(todoList.size(), is(1));
        assertThat(todoList.size(), is(todoList.size()));
        assertTrue(todoList.get(0).getTodoTitles().equals("Jogging")); // todoList count is starting from 0.
    }

}