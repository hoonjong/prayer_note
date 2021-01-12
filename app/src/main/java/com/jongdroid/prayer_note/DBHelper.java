package com.jongdroid.prayer_note;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

//쉐어드레퍼런스랑은 다른 방식 이며, SQLite는 좀 더 데이터를 체계적으로 관리할 수 있음!
public class DBHelper extends SQLiteOpenHelper {

    //private는 다른 클래스에서는 접근할 수 없음
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Jongdroid.db";    //데이터베이스의 이름

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //구조 데이터베이스(부모님) --> 테이블 --> 컬럼 --> 값

    @Override
    public void onCreate(SQLiteDatabase db) {
        //데이터베이스가 생성될 때 호출          //JongDroid라는 테이블 그 안에 들어가는 값 AUTOINCREMENT = 자동적으로 id가 1씩 증가함(데이터가 쓰이면)
        //TEXT NOT NULL title 이라는 친구는 비어 있으면 안됨!**  //Writedate = 작성 날짜
        //Jongdroid 라는 테이블에 속하는 컬럼 입니다. 여기까지 했으면 데이터를 만드는 것까지는 완성.
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, content TEXT NOT NULL, writeDate TEXT NOT NULL )"); //테이블을 만드는 작업 이미 테이블이 존재하면 이 쿼리문을 실행하지 않음, 뒤에 테이블 명
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //SELECT 문
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        //쌍따음표 = 쿼리문 TodoList에 데이터를 모두 가져옵니다. ORDER BY 정렬합니다. DESC = 내림차순을 통해서 정렬합니다.
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList ORDER BY writeDate DESC", null);
        //반드시 데이터가 있는 상황
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String writeDate = cursor.getString(cursor.getColumnIndex("writeDate"));

                TodoItem todoItem = new TodoItem();
                todoItem.setId(id);
                todoItem.setTitle(title);
                todoItem.setContent(content);
                todoItem.setWriteDate(writeDate);
                todoItems.add(todoItem);


            }
        }
        cursor.close();
        return todoItems;
    }


    //INSERT 문 --> public 으로 만드는 이유는 언제든지 이  메소드에 접근할 수 있도록 접근지정자를 설정해줌!
    //할일 목록을 DB에 넣음
    public void InsertTodo(String _title, String _content, String _writeDate) {
        SQLiteDatabase db = getWritableDatabase();
        //TodoList에 id를 제외한 칼럼을 모두 넣어줌! 이 부분이 좀 복잡함
        //자바 언어가 아니라 VALUES 안에 들어간 내용은 쿼리문이라 조금 복잡함
        db.execSQL("INSERT INTO TodoList (title, content, writeDate) VALUES('" + _title + "','" + _content + "', '" + _writeDate + "');");   //db를 작성합니다!! -->TodoList 라는 곳 안에 작성을 합니다.

    }



    //UPDATE 문 --> 목록을 수정 함   beforeDate --> 이전 Date
    //WHERE 은 조건문이랑 비슷함 -->PRIMARY KEY 값을 이용해서 WHERE ID 로 값을 지정했음
    public void UpdateTodo(String _title, String _content, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET title='" + _title + "', content='" + _content + "', writeDate='" + _writeDate + "' WHERE writeDate = '" + _beforeDate + "'");
    }

    //DELETE 문 --> 목록을 제거 함
    //SQL 형식은 '" + "' 을 많이 씀
    public void deleteTodo(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE writeDate ='" + _beforeDate + "'");
    }


}