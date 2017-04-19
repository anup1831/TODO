package com.pathfinder.anup.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pathfinder.anup.constants.Constatnts;
import com.pathfinder.anup.loaders.MainActivity;
import com.pathfinder.anup.loaders.R;
import com.pathfinder.anup.models.ModelDailyTodo;
import com.pathfinder.anup.screens.TodoDetailsScreen;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anup on 3/30/2017.
 */

public class TodoListAdapter extends BaseAdapter {

    private Context context;
    MainActivity activity;
    private List<ModelDailyTodo> todoList = new ArrayList<>();
    public OnTitleClickListner onTitleClickListner;

    public TodoListAdapter(Context context, List<ModelDailyTodo> todoList) {
        this.context = context;
        activity = (MainActivity) context;
        this.todoList = todoList;
        onTitleClickListner = (OnTitleClickListner) context;
    }

    @Override
    public int getCount() {
        return todoList.size();
    }

    @Override
    public Object getItem(int i) {
        return todoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        LayoutInflater  mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.todo_items, null);
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.tv_todo);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.calender = (ImageView) convertView.findViewById(R.id.iv_calender);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ModelDailyTodo todo = (ModelDailyTodo) getItem(position);
        holder.title.setText(todo.getTodoTitles());
        //Log.i("Anup", "desc - "+todo.getTodoItemsDetail());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Anup", "desc - data "+todo.getTodoItemsDetail());
                Log.i("Anup", "descID - data "+todo.getId());
                /*if(null == todo.getTodoItemsDetail() && todo.getTodoItemsDetail().equals("")){
                    Intent
                }*/
                //TodoDetailsScreen.displayTodoDetailsActivity(context, todo.getTodoItemsDetail(), todo.getId());
                onTitleClickListner.onTitleClick(todo.getId(), todo.getTodoTitles(), todo.getTodoItemsDetail());

            }
        });
        holder.time.setText("10:10");

        holder.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Time feature will come soon", Toast.LENGTH_LONG).show();
            }
        });

        holder.calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Calender feature will come soon", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView calender;
        TextView title;
        TextView time;
    }


    public interface OnTitleClickListner{
        void onTitleClick(int ID, String title, String itemDetails);
    }


}
