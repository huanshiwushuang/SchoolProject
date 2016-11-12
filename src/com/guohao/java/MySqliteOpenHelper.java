package com.guohao.java;

import com.guohao.util.Data;
import com.guohao.util.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
	public static final String CREATE_TABLE_EXAM_PAPER = "create table "+Data.EXAM_PAPER_TABLE_NAME
			+ "("
				+ "dataId integer primary key autoincrement,"
				+ "examPaperId integer,"
				+ "itemScore integer,"
				+ "selectScore integer,"
				+ "examTiType text,"
				+ "score integer,"
				+ "answerCount integer,"
				+ "strandAnswer text,"
				+ "selectAnswers text,"
				+ "createAdmin text,"
				+ "createTime real,"
				+ "course text,"
				+ "id integer,"
				+ "content text,"
				+ "chooseAnswer text"
			+ ")";
	private Context mContext;
	

	public MySqliteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_EXAM_PAPER);
		Util.showToast(mContext, "执行了---创建表命令");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
}
