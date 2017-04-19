package com.pathfinder.anup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pathfinder.anup.constants.Constatnts;
import com.pathfinder.anup.models.ModelDailyTodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anup on 3/24/2017.
 */

public class LocalDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dailytodo";
    public static final int DB_VERSION = 1;


    //Table details
    public static final String TB_DAILY_TODO_ITEMS = "DailyTodoItems";
    public static final String ID = "id";
    public static final String TODO_ITEM = "item";
    public static final String TODO_ITEM_DETAIL = "details";


    //Query part
    public static final String DROP = "DROP TABLE IF EXISTES ";


    /** As you are thinking about generating table and it's field dynamically, you have to send the table name, fields name in
     * constructor, also you have to think about how to define data types of table fields.
     * All you need to create local veriable in this class and assign it appropriate parameters.
     * As if it's a dream so think over it thoroughly*/

    public LocalDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String createDailyTodoItems = "CREATE TABLE "+ TB_DAILY_TODO_ITEMS + " ("
                + ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TODO_ITEM + " TEXT NOT NULL, "
                + TODO_ITEM_DETAIL + " TEXT"
                + " )";

        db.execSQL(createDailyTodoItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olderVersion, int newerVersion) {
        if(olderVersion != newerVersion){
            db.execSQL(DROP + TB_DAILY_TODO_ITEMS);
        }
        onCreate(db);
    }

    public long insertDataIntoDB(ModelDailyTodo dailyTodo){
        long rowInserted = -5;
        SQLiteDatabase db = this.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(TODO_ITEM, dailyTodo.getTodoTitles());
            values.put(TODO_ITEM_DETAIL, dailyTodo.getTodoItemsDetail());

            rowInserted = db.insert(TB_DAILY_TODO_ITEMS, null, values);
            Log.i("Anup",  "Anup "+rowInserted);
            return rowInserted;
        }catch (Exception e){

        } finally {
            db.close();
        }
        return 0;
    }

//    public long insertDescIntoDBFirst(String desc){
//        long rowInserted = -5;
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        try{
//            ContentValues values = new ContentValues();
//            values.put(TODO_ITEM, dailyTodo.getTodoTitles());
//            values.put(TODO_ITEM_DETAIL, dailyTodo.getTodoItemsDetail());
//
//            rowInserted = db.insert(TB_DAILY_TODO_ITEMS, null, values);
//            Log.i("Anup",  "Anup "+rowInserted);
//            return rowInserted;
//        }catch (Exception e){
//
//        } finally {
//            db.close();
//        }
//        return 0;
//    }

    public int getRowCounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            String countQuery = "SELECT * FROM " + TB_DAILY_TODO_ITEMS;
            cursor = db.rawQuery(countQuery, null);
            return cursor.getCount();
        }catch (Exception e){

        }finally {
            db.close();
        }


        return 0;
    }

    public List<ModelDailyTodo> getAllTodos(){
        List<ModelDailyTodo> dailyTodosList = new ArrayList<ModelDailyTodo>();

        String selectQuery = "SELECT ROWID, * FROM " + TB_DAILY_TODO_ITEMS;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null && cursor.getCount() > -1){
            while (cursor.moveToNext()){
                ModelDailyTodo dailyTodo = new ModelDailyTodo();
                Log.i("Anup", "todo ID "+ cursor.getInt(cursor.getColumnIndex(LocalDBHelper.ID)));
                dailyTodo.setId(cursor.getInt(cursor.getColumnIndex(LocalDBHelper.ID)));
                Log.i("Anup", "todo Title "+cursor.getString(cursor.getColumnIndex(LocalDBHelper.TODO_ITEM)));
                dailyTodo.setTodoTitles(cursor.getString(cursor.getColumnIndex(LocalDBHelper.TODO_ITEM)));
                dailyTodo.setTodoItemsDetail(cursor.getString(cursor.getColumnIndex(LocalDBHelper.TODO_ITEM_DETAIL)));
                Log.i("Anup", "todo desc "+cursor.getString(cursor.getColumnIndex(LocalDBHelper.TODO_ITEM_DETAIL)));
                dailyTodosList.add(dailyTodo);
            }
        }

        return dailyTodosList;
    }

    public void updateTodo(int Id, String title, String dataTobeUpdated){
        String updatedDesc = "";
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues updatedValues = new ContentValues();
        //updatedValues.put(ID, Id);
        updatedValues.put(TODO_ITEM, title);
        updatedValues.put(TODO_ITEM_DETAIL, dataTobeUpdated);
        Log.i("Anup", "updated value -"+dataTobeUpdated);
        db.update(TB_DAILY_TODO_ITEMS, updatedValues, ID + " = ?", new String[] { String.valueOf(Id)});

         //String query = "SELECT * FROM " + TB_DAILY_TODO_ITEMS + " WHERE ID ="+ Id;
       //Cursor cursor = db.execSQL("SELECT * FROM " + TB_DAILY_TODO_ITEMS + " WHERE ID ="+ Id, null);
       /* Cursor cursor = db.query(TB_DAILY_TODO_ITEMS, new String[] { ID,
                        TODO_ITEM, TODO_ITEM_DETAIL }, ID + "=?",
                new String[] { String.valueOf(Id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        updatedDesc = cursor.getString(2);
        return updatedDesc;*/
    }
}
