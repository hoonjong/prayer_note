package com.jongdroid.prayer_note;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<TodoItem> todoItems;
    private Context mContext;
    private DBHelper mDBHelper;

    public CustomAdapter(ArrayList<TodoItem> todoItems, Context mContext) {
        this.todoItems = todoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.tv_title.setText(todoItems.get(position).getTitle());
        holder.tv_content.setText(todoItems.get(position).getContent());
        holder.tv_writeDate.setText(todoItems.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_writeDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_title2);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            //뷰 하나의 대한 아이템뷰
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curPos = getAdapterPosition(); //현재 리스트 아이템 위치
                    TodoItem todoItem = todoItems.get(curPos);
                    String[] strClickItems = {"수정하기", "삭제하기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("원하는 동작을 선택해주세요");
                    builder.setItems(strClickItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if (position == 0) {
                                //수정하기
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_edit);  //우리가 만든 다이아로그를 출력하기!
                                EditText et_title = dialog.findViewById(R.id.ed_title);
                                EditText et_content = dialog.findViewById(R.id.ed_content);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                et_title.setText(todoItem.getTitle());
                                et_content.setText(todoItem.getContent());
                                //커서 이동
                                et_title.setSelection(et_title.getText().toString().length());

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String title = et_title.getText().toString();
                                        String content = et_content.getText().toString();
                                        //현재 시간 년 월 일 시 분 초 받아오기 INSERT DATABASE
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                        String beforeTime = todoItem.getWriteDate();

                                        mDBHelper.UpdateTodo(title, content, currentTime, beforeTime);

                                        //INSERT UI
                                        todoItem.setTitle(title);
                                        todoItem.setContent(content);
                                        todoItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, todoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "목록 수정이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                            } else if (position == 1) {
                                //삭제하기
                                String beforeTime = todoItem.getWriteDate();
                                mDBHelper.deleteTodo(beforeTime);
                                //delete UI
                                todoItems.remove(curPos);
                                notifyItemRemoved(curPos);
                                Toast.makeText(mContext, "목록이 제거되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                }
            });
        }

    }
    //액티비티에서 호출되는 함수이며, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가합니다.
    public void addItem(TodoItem _item) {
        todoItems.add(0, _item);
        notifyItemInserted(0);
    }

}