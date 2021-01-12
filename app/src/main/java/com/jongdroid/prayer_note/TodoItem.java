package com.jongdroid.prayer_note;

public class TodoItem {
    //테이블 칼럼을 그대로 만들기

    private int id;
    private String title;
    private String content;
    private String writeDate;


    public TodoItem() {

    }
    //ALT+INSERT 이용해서 게터세터로 진행하면 편함
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}