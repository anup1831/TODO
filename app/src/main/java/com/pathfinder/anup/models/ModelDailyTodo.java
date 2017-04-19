package com.pathfinder.anup.models;

/**
 * Created by Anup on 3/24/2017.
 */

public class ModelDailyTodo {
    private int id;
    private String todoTitles;
    private String todoItemsDetail;

    public ModelDailyTodo() {
    }

    public ModelDailyTodo(int id, String todoTitles, String todoItemsDetail) {
        this.id = id;
        this.todoTitles = todoTitles;
        this.todoItemsDetail = todoItemsDetail;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodoTitles() {
        return todoTitles;
    }

    public void setTodoTitles(String todoTitles) {
        this.todoTitles = todoTitles;
    }

    public String getTodoItemsDetail() {
        return todoItemsDetail;
    }

    public void setTodoItemsDetail(String todoItemsDetail) {
        this.todoItemsDetail = todoItemsDetail;
    }
}
