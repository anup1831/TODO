package com.pathfinder.anup.screens;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pathfinder.anup.constants.Constatnts;
import com.pathfinder.anup.database.LocalDBHelper;
import com.pathfinder.anup.loaders.MainActivity;
import com.pathfinder.anup.loaders.R;

/**
 * Created by Anup on 3/27/2017.
 */

public class TodoDetailsScreen extends Activity implements View.OnClickListener {

    TextView descTextView;
    EditText descEditView;
    Button btn_cancel, btn_edit, btn_save;
    String descData;
    int selectedID;
    String descTitle;
    boolean btnEditClicked = false;
    boolean valueUpdated = false;
    String tappedFromBtn;

    public String getTappedFromBtn() {
        return tappedFromBtn;
    }

    public void setTappedFromBtn(String tappedFromBtn) {
        this.tappedFromBtn = tappedFromBtn;
    }

    public String getDescTitle() {
        return descTitle;
    }

    public void setDescTitle(String descTitle) {
        this.descTitle = descTitle;
    }

    public int getSelectedID() {
        return selectedID;
    }

    public void setSelectedID(int selectedID) {
        this.selectedID = selectedID;
    }

    public String getDescData() {
        return descData;
    }

    public void setDescData(String descData) {
        this.descData = descData;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDescDataIfAny();
        setContentView(R.layout.todo_desc);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        params.height = height/2;
        params.width = (int)((width *1.5)/2);
        this.getWindow().setAttributes(params);


        setupViews();
        setupData();

    }

    private void setupData(){
        if(getSelectedID() > -1){
            descEditView.setVisibility(View.GONE);
            descTextView.setVisibility(View.VISIBLE);
            descTextView.setText(getDescData());
            btnVisibleState(btn_save, false);
            btnVisibleState(btn_edit, true);

        } else {}
    }

    /*public static void displayTodoDetailsActivity(Context context, String todoDetails, int id) {
        Intent intent = new Intent(context, TodoDetailsScreen.class);
        if (context != null && todoDetails != null && id > 0) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constatnts.DESC_DATA, todoDetails);
            intent.putExtra(Constatnts.TODO_ID, id);
            Log.i("Anup", "desc data TDS - ID "+ todoDetails+ " - "+ id);
            context.startActivity(intent);
        } else {
            context.startActivity(intent);
        }
    }*/

    private void getDescDataIfAny(){
        if(!(getIntent().getExtras().getString(Constatnts.NAVIGATION).equalsIgnoreCase(getString(R.string.navigation_desc_btn_clk)))){
            setTappedFromBtn(getIntent().getExtras().getString(Constatnts.NAVIGATION));
            if(getIntent().getExtras().getInt(Constatnts.TODO_ID) > -1){
                setSelectedID(getIntent().getExtras().getInt(Constatnts.TODO_ID));
                setDescTitle(getIntent().getExtras().getString(Constatnts.TODO_TITLE));
                setDescData(getIntent().getExtras().getString(Constatnts.DESC_DATA));
            }
        } else {
            setTappedFromBtn(getIntent().getExtras().getString(Constatnts.NAVIGATION));
            //tappedFromBtn = getIntent().getExtras().getString(Constatnts.NAVIGATION);
        }

    }

    private void setupViews(){
        descTextView = (TextView) findViewById(R.id.tv_desc);
        descEditView = (EditText) findViewById(R.id.et_desc);
        if (getTappedFromBtn() == "straight") {
            descTextView.setVisibility(View.GONE);
            descEditView.setVisibility(View.VISIBLE);
            btnVisibleState(btn_edit, false);
            btnVisibleState(btn_save, true);
        } else{

            descTextView.setVisibility(View.VISIBLE);
            Toast.makeText(TodoDetailsScreen.this, " - "+getDescData(), Toast.LENGTH_SHORT).show();
            descTextView.setText(getDescData());
        }


        descEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnVisibleState(btn_save, true);
                btnClickableState(btn_save, false);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnClickableState(btn_save, true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(View.VISIBLE);
        btn_cancel.setOnClickListener(this);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btnVisibleState(btn_save, true);
        btnClickableState(btn_save, false);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                 this.finish();
                break;
            case R.id.btn_edit:
                String descText = descTextView.getText().toString();
                descTextView.setVisibility(View.GONE);
                descEditView.setVisibility(View.VISIBLE);
                descEditView.setText(descText);
                descEditView.setSelection(descText.length());
                btnVisibleState(btn_edit, false);
                btnVisibleState(btn_save, true);
                break;
            case R.id.btn_save:
                LocalDBHelper db = new LocalDBHelper(getApplicationContext());
                //need to insert this data into the table along with title
                Log.i("Anup", "Tapped from "+ tappedFromBtn);
                if(tappedFromBtn.equalsIgnoreCase(getString(R.string.navigation_desc_btn_clk))){
                     /*btnVisibleState(btn_edit, false);
                    btnVisibleState(btn_save, true);*/
                    Intent intent = new Intent(this, MainActivity.class);
                    Log.i("Anup", "DescData "+ descEditView.getText().toString());
                    intent.putExtra(Constatnts.DESC_DATA, descEditView.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                 //need to update the table with description against the ID and Title
                    Log.i("Anup", "Tapped from "+ tappedFromBtn);
                } else if (tappedFromBtn.equalsIgnoreCase(getString(R.string.navigation_by_title_clk))){
                    String str = descEditView.getText().toString();
                    descEditView.setSelection(str.length());
                    btnClickableState(btn_save, true);
                    db.updateTodo(getSelectedID(), getDescTitle(), descEditView.getText().toString());
                    valueUpdated = true;
                    setBundleData(valueUpdated);
                } else {
                    String str = descEditView.getText().toString();
                    if(str != null || str.equals("")){
                        descEditView.setSelection(descEditView.getText().toString().length());
                        btnClickableState(btn_save, true);
                        db.updateTodo(getSelectedID(), getDescTitle(), descEditView.getText().toString());
                        valueUpdated = true;
                        setBundleData(valueUpdated);
                    } else {
                        btnClickableState(btn_save, false);
                    }
                }
                /*if(!(tappedFromBtn.equalsIgnoreCase(getString(R.string.navigation_desc_btn_clk)))){
                    String str = descEditView.getText().toString();
                    if(str != null || str.equals("")){
                        descEditView.setSelection(descEditView.getText().toString().length());
                        btnClickableState(btn_save, true);
                        db.updateTodo(getSelectedID(), getDescTitle(), descEditView.getText().toString());
                        valueUpdated = true;
                        setBundleData(valueUpdated);
                    } else {
                        btnClickableState(btn_save, false);
                    }
                } else {
                    *//*btnVisibleState(btn_edit, false);
                    btnVisibleState(btn_save, true);*//*
                    Intent intent = new Intent(this, MainActivity.class);
                    Log.i("Anup", "DescData "+ descEditView.getText().toString());
                    intent.putExtra(Constatnts.DESC_DATA, descEditView.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }*/


                break;
              default:
                  break;

        }
    }

    private void setBundleData(boolean flag){
        Intent intent = new Intent(this, MainActivity.class);
        Log.i("Anup", "IsValueUpdated "+ flag);
        intent.putExtra(Constatnts.IS_VALUE_UPDATED, flag);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void btnClickableState(Button btn, boolean flag) {
        if (flag) {
            btn.setEnabled(true);
            btn.setClickable(true);
        } else {
            btn.setClickable(false);
            btn.setEnabled(false);
        }
    }

    private void btnVisibleState(Button btn, boolean flag){
        if(flag){
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.GONE);
        }
    }
}
