package com.pathfinder.anup.loaders;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pathfinder.anup.adapters.TodoListAdapter;
import com.pathfinder.anup.alarm.AlarmMainActivity;
import com.pathfinder.anup.constants.Constatnts;
import com.pathfinder.anup.database.LocalDBHelper;
import com.pathfinder.anup.models.ModelDailyTodo;
import com.pathfinder.anup.screens.TodoDetailsScreen;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TodoListAdapter.OnTitleClickListner{

    public static final int REQUEST_CODE = 100;
    ModelDailyTodo todo;
    LocalDBHelper dbHelper;
    EditText et_title;
    ImageButton ib_desc, ib_add, ib_alarm;
    ListView todoListView;
    List<ModelDailyTodo> todoList;
    TodoListAdapter todoListAdapter;
    private String descData;
    boolean isChildActivityData;
    private String alarmValue;

    public String getAlarmValue() {
        return alarmValue;
    }

    public void setAlarmValue(String alarmValue) {
        this.alarmValue = alarmValue;
    }

    public boolean isChildActivityData() {
        return isChildActivityData;
    }

    public void setChildActivityData(boolean childActivityData) {
        isChildActivityData = childActivityData;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
        fetchTodoData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*long dataInserted = dbHelper.insertDataIntoDB(MainActivity.this, todo);
        Toast.makeText(MainActivity.this, "values "+dataInserted, Toast.LENGTH_LONG).show();
        dbHelper.close();*/
    }

    private void setupViews(){
        et_title = (EditText) findViewById(R.id.todo_title);
        ib_desc = (ImageButton) findViewById(R.id.todo_desc);
        ib_desc.setOnClickListener(this);
        ib_alarm = (ImageButton) findViewById(R.id.todo_alarm) ;
        ib_alarm.setOnClickListener(this);
        /*ib_add = (ImageButton) */findViewById(R.id.todo_add).setOnClickListener(this);
        //ib_add.setOnClickListener(this);
        todoListView = (ListView) findViewById(R.id.id_todoList);
    }

    private void setupListViews(){
        fetchTodoData();

    }

    private void fetchTodoData(){
        LocalDBHelper dbHelper = new LocalDBHelper(MainActivity.this);
        todoList = new ArrayList<ModelDailyTodo>();
       // if(isChildActivityData()){
            todoList = dbHelper.getAllTodos();
            todoListAdapter = new TodoListAdapter(MainActivity.this, todoList);
            todoListAdapter.notifyDataSetChanged();
            todoListView.setAdapter(todoListAdapter);
       // } else {
            /*todoList = dbHelper.getAllTodos();
            todoListAdapter = new TodoListAdapter(MainActivity.this, todoList);
            todoListAdapter.notifyDataSetChanged();
            todoListView.setAdapter(todoListAdapter);*/
       // }
        //setupTodoList();
        /*for (ModelDailyTodo todo: todoList) {
           Log.i("Anup", " title - Details "+ todo.getTodoTitles() + " - "+todo.getTodoItemsDetail());
        }*/
        dbHelper.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.todo_desc) {
            if (et_title.getText().toString().equals("") || et_title.getText().toString() == null) {
                Toast.makeText(getApplicationContext(), "There is nothing in Title to provide description", Toast.LENGTH_LONG).show();
            } else {
                navigateToDetails(-1, "", "");
            }
        } else if (view.getId() == R.id.todo_add) {
            saveTodos();
            et_title.setText("");
            setupListViews();
        }
    }

    public void saveTodos(){
        todo = new ModelDailyTodo();
        if(et_title.getText().toString() != null && (!et_title.getText().toString().equals(""))){
            todo.setTodoTitles(et_title.getText().toString());
            //below condition is for straight entry, first title then desc
            if(getDescData() != null && !getDescData().equals("")){
                todo.setTodoItemsDetail(getDescData());
            } else {
                //todo.setTodoItemsDetail("");
            }

            insertDataIntoDB(todo);
        } else {
            Toast.makeText(getApplicationContext(), "Add some todo item", Toast.LENGTH_LONG).show();
        }
    }

    public void insertDataIntoDB(ModelDailyTodo todo){
        LocalDBHelper dbHelper = new LocalDBHelper(MainActivity.this);
        Constatnts.IS_TODO_ADDED = "true";
        dbHelper.insertDataIntoDB(todo);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Constatnts.REQUEST_CODE){
            if(data.hasExtra(Constatnts.IS_VALUE_UPDATED) == true){
                setChildActivityData(true);
                fetchTodoData();
            }
        } else if (resultCode == RESULT_OK && requestCode == Constatnts.REQUEST_CODE1){
            if(data.hasExtra(Constatnts.DESC_DATA)){
                setDescData(data.getExtras().getString(Constatnts.DESC_DATA));
                Log.i("Anup", "DESCDATA - "+getDescData());
            }
        } else if(resultCode == RESULT_OK && requestCode == Constatnts.REQUEST_ALARM){
            if(data.hasExtra(Constatnts.ALARM_TIMING)){
                setAlarmValue(data.getStringExtra(Constatnts.ALARM_TIMING));
                Log.i("Anup", "ALARMDATA - "+getAlarmValue());
            }
        }
    }

    @Override
    public void onTitleClick(int ID, String title, String itemDetails) {
        Toast.makeText(getApplicationContext(), "ID - Details "+ ID + " - "+ itemDetails, Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(this, TodoDetailsScreen.class);
        intent.putExtra(Constatnts.TODO_ID, ID);
        intent.putExtra(Constatnts.TODO_TITLE, title);
        intent.putExtra(Constatnts.DESC_DATA, itemDetails);
        startActivityForResult(intent, Constatnts.REQUEST_CODE);*/

        navigateToDetails(ID, title, itemDetails);
    }

    @Override
    public void onTimeChange(/*TextView str*/) {
        Intent timeIntent = new Intent(getApplicationContext(), AlarmMainActivity.class);
        startActivityForResult(timeIntent, Constatnts.REQUEST_ALARM);
       // str.setTextColor(Color.BLACK);
       // str.setText(getAlarmValue());
        /*str.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                str.setText(getAlarmValue());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
        Toast.makeText(getApplicationContext(), getAlarmValue(), Toast.LENGTH_LONG).show();
        Log.i("Anup", "ALARMDATA - "+getAlarmValue());

    }


    @Override
    public void onCalederChange() {
        Toast.makeText(getApplicationContext(), "Calender feature will come soon", Toast.LENGTH_LONG).show();
    }

    private void navigateToDetails(int ID, String title, String details){
        Intent intent = new Intent(this, TodoDetailsScreen.class);
        Log.i("Anup", "navigateToDetails "+ ID + " - "+ title + " - "+details);
        if(ID > -1 && !title.isEmpty()/* && (details.isEmpty() || details.equals("") || details == null)*/){
            intent.putExtra(Constatnts.NAVIGATION, getString(R.string.navigation_by_title_clk));
            intent.putExtra(Constatnts.TODO_ID, ID);
            intent.putExtra(Constatnts.TODO_TITLE, title);
            intent.putExtra(Constatnts.DESC_DATA, details);
            startActivityForResult(intent, Constatnts.REQUEST_CODE);
        } else {
            intent.putExtra(Constatnts.NAVIGATION, getString(R.string.navigation_desc_btn_clk));
            startActivityForResult(intent, Constatnts.REQUEST_CODE1);
        }

    }
}
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        // Create the list fragment and add it as our sole content.
        if (fm.findFragmentById(android.R.id.content) == null) {
            CursorLoaderListFragment list = new CursorLoaderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }
    //BEGIN_INCLUDE(fragment_cursor)
    public static class CursorLoaderListFragment extends ListFragment
            implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<Cursor> {
        // This is the Adapter being used to display the list's data.
        SimpleCursorAdapter mAdapter;
        // The SearchView for doing filtering.
        SearchView mSearchView;
        // If non-null, this is the current filter the user has provided.
        String mCurFilter;
        @Override public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText("No phone numbers");
            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);
            // Create an empty adapter we will use to display the loaded data.
            mAdapter = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_2, null,
                    new String[] { ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS },
                    new int[] { android.R.id.text1, android.R.id.text2 }, 0);
            setListAdapter(mAdapter);
            // Start out with a progress indicator.
            setListShown(false);
            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }
        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }
            // The normal SearchView doesn't clear its search text when
            // collapsed, so we will do this for it.
            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }
        @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Place an action bar item for searching.
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            mSearchView = new MySearchView(getActivity());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }
        public boolean onQueryTextChange(String newText) {
            // Called when the action bar search text has changed.  Update
            // the search filter, and restart the loader to do a new query
            // with this filter.
            String newFilter = !TextUtils.isEmpty(newText) ? newText : null;
            // Don't do anything if the filter hasn't actually changed.
            // Prevents restarting the loader when restoring state.
            if (mCurFilter == null && newFilter == null) {
                return true;
            }
            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }
            mCurFilter = newFilter;
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }
        @Override public boolean onQueryTextSubmit(String query) {
            // Don't care about this.
            return true;
        }
        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }
        @Override public void onListItemClick(ListView l, View v, int position, long id) {
            // Insert desired behavior here.
            Log.i("FragmentComplexList", "Item clicked: " + id);
        }
        // These are the Contacts rows that we will retrieve.
        static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY,
        };
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Uri baseUri;
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = ContactsContract.Contacts.CONTENT_URI;
            }
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                    + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";
            return new CursorLoader(getActivity(), baseUri,
                    CONTACTS_SUMMARY_PROJECTION, select, null,
                    ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)
            mAdapter.swapCursor(cursor);
            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {

            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            mAdapter.swapCursor(null);
        }

        *//*public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)
            mAdapter.swapCursor(data);
            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }*//*
        *//*public void onLoaderReset(Loader<Cursor> loader) {
            // This is called when the last Cursor provided to onLoadFinished()
            // above is about to be closed.  We need to make sure we are no
            // longer using it.
            mAdapter.swapCursor(null);
        }*//*
    }
//END_INCLUDE(fragment_cursor)*/
//}
