package com.akhahaha.pantry2plate.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Pantry2Plate";
	private static final String TABLE_NAME = "pantry";

	// table column names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_QUANTITY = "quantity";
	private static final String KEY_UNITS = "units";

	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
				KEY_NAME + " TEXT NOT NULL," +
				KEY_QUANTITY + " INTEGER," +
				KEY_UNITS + " TEXT" + ")";
		db.execSQL(CREATE_PANTRY_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop and recreate tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	void addToPantry(Ingredient ingredient) {
		SQLiteDatabase db = this.getWritableDatabase();

		ArrayList<String> itemNames = this.getAllItemNames();
		if (!itemNames.contains(ingredient.getName())) {
			ContentValues values = new ContentValues();
			values.put(KEY_NAME, ingredient.getName());
			values.put(KEY_QUANTITY, ingredient.getQuantity());
			values.put(KEY_UNITS, ingredient.getUnits());

			db.insert(TABLE_NAME, null, values);
		}

		db.close();
	}

	Ingredient getItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_NAME,
				new String[]{KEY_ID, KEY_NAME, KEY_QUANTITY, KEY_UNITS},
				KEY_ID + "=?" + id, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		return new Ingredient(cursor.getString(1), cursor.getInt(2),
				cursor.getString(3));
	}

	public ArrayList<Ingredient> getAllItems() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

		// loop through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				Ingredient ingredient = new Ingredient(cursor.getString(1),
						cursor.getInt(2), cursor.getString(3));
				ingredientList.add(ingredient);
			} while (cursor.moveToNext());
		}

		return ingredientList;
	}

	// get all item names
	public ArrayList<String> getAllItemNames() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		Cursor cursor = db.rawQuery(selectQuery, null);

		ArrayList<String> itemList = new ArrayList<String>();

		// loop through all rows and add to list
		if (cursor.moveToFirst()) {
			do {
				itemList.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}

		return itemList;
	}

	public void updateItem(String name, Ingredient ingredient) {
		SQLiteDatabase db = this.getWritableDatabase();
		String updateQuery = "UPDATE " + TABLE_NAME
				+ " SET name = '"+ ingredient.getName()
				+ "', quantity = " + ingredient.getQuantity()
				+ ", units = '" + ingredient.getUnits()
				+ "' WHERE name = '" + name + "';";
		Cursor cursor = db.rawQuery(updateQuery, null);
		cursor.getCount(); // magic fix for updateItem
	}

	public void deleteItem(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_NAME + " = ?",
				new String[]{String.valueOf(name)});
		db.close();
	}

	public int getItemsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
}
