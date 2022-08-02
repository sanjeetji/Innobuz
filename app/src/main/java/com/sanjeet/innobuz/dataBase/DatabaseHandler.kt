package com.sanjeet.innobuz.dataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.sanjeet.innobuz.model.PostItem

class DatabaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {


    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "PostDatabase"
        private val TABLE_CONTACTS = "PostTable"
        private val KEY_USERID = "userId"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_BODY = "body"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE =
            ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERID +" VARCHAR(256),"
                + KEY_TITLE + " TEXT,"
                + KEY_BODY + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    fun addPost(post: PostItem):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, post.id)
        contentValues.put(KEY_USERID,post.userId)
        contentValues.put(KEY_TITLE, post.title)
        contentValues.put(KEY_BODY,post.body)
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    @SuppressLint("Range")
    fun viewPost():ArrayList<PostItem>{
        val postList:ArrayList<PostItem> = ArrayList<PostItem>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Int
        var id: Int
        var title: String
        var body: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("userId"))
                id = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                body = cursor.getString(cursor.getColumnIndex("body"))
                val emp= PostItem(userId = userId,id=id, title = title, body = body)
                postList.add(emp)
            } while (cursor.moveToNext())
        }
        return postList
    }
    fun deletePost(post: PostItem):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, post.id)
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, "id=?"+ post.id,null)
        db.close() // Closing database connection
        return success
    }
}