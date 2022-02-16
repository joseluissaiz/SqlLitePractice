package com.overshade.sqllitepractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private CommentListener commentListener;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "commentsdb";
    private static final String TABLE_Comments = "comments";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";

    public DatabaseHandler(@Nullable Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_Comments + "("
                    + KEY_TITLE + " TEXT,"
                    + KEY_CONTENT + " TEXT"+
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Comments);
        // Create tables again
        onCreate(db);
    }

    public void insertComment(String title, String content) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, title);
        contentValues.put(KEY_CONTENT, content);
        long newRowId = database.insert(TABLE_Comments, null, contentValues);
        Log.d("SQL", "New row added :"+newRowId);
        database.close();
        commentListener.onCommentCreated();
    }

    public ArrayList<HashMap<String, String>> getCommentList() {
        ArrayList<HashMap<String, String>> commentList = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT title, content FROM comments", null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
               do {
                    HashMap<String,String> comment = new HashMap<>();
                    comment.put("title",cursor.getString(0));
                    comment.put("content",cursor.getString(1));
                    commentList.add(comment);
                } while (cursor.moveToNext());
            }
        }
        return commentList;
    }

    public void setCommentListener(CommentListener listener) {
        this.commentListener = listener;
    }

    public interface CommentListener {
        void onCommentCreated();
    }

}
