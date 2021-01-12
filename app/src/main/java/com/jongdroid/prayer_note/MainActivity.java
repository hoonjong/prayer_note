package com.jongdroid.prayer_note;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_todo;
    private FloatingActionButton btn_write;
    private ArrayList<TodoItem> todoItems;
    private DBHelper mDBHelper;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInit();
    }

    private void setInit() {
        mDBHelper = new DBHelper(this);
        rv_todo = findViewById(R.id.rv_todo); //리사이클러뷰
        btn_write = findViewById(R.id.btn_write);
        todoItems = new ArrayList<>();

        //Load RecentDB
        loadRecentDB();

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_edit);  //우리가 만든 다이아로그를 출력하기!
                EditText et_title = dialog.findViewById(R.id.ed_title);
                EditText et_content = dialog.findViewById(R.id.ed_content);

                Button btn_ok = dialog.findViewById(R.id.btn_ok);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //현재 시간 년 월 일 시 분 초 받아오기 INSERT DATABASE
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(new Date());
                        mDBHelper.InsertTodo(et_title.getText().toString(), et_content.getText().toString(), currentTime);

                        //INSERT UI
                        TodoItem item = new TodoItem();
                        item.setTitle(et_title.getText().toString());
                        item.setContent(et_content.getText().toString());
                        item.setWriteDate(currentTime);

                        mAdapter.addItem(item);
                        rv_todo.smoothScrollToPosition(0);
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "작성이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                });

                dialog.show();
            }
        });
    }

    private void loadRecentDB() {
        //저장되어 있는 DB 호출
        todoItems = mDBHelper.getTodoList();
        if (mAdapter == null) {
            mAdapter = new CustomAdapter(todoItems, this);
            rv_todo.setHasFixedSize(true);
            rv_todo.setAdapter(mAdapter);
        }


    }


}