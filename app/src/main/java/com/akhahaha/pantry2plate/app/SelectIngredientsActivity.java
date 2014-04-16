package com.akhahaha.pantry2plate.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.akhahaha.pantry2plate.R;

import java.util.ArrayList;

public class SelectIngredientsActivity extends Activity {
	private ArrayList<String> items;
	private DBHandler db;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectingredients);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().show();

		// load database
		db = new DBHandler(this);
		// initialize ingredients from database
		// add example items if empty
		if (db.getItemsCount() == 0) {
			db.addToPantry(new Ingredient("Eggs", 12, ""));
			db.addToPantry(new Ingredient("Flour", 1, "lb"));
		}
		items = db.getAllItemNames();

		// initialize adapter with array
		adapter = new ArrayAdapter<String>
				(this, android.R.layout.simple_list_item_1, items);

		// setup listView
		ListView lv = (ListView) findViewById(R.id.ingredientsListView);
		lv.setAdapter(adapter);

		// long press listView to edit
		lv.setLongClickable(true);
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
				// create alert dialog
				final AlertDialog.Builder alert =
						new AlertDialog.Builder(SelectIngredientsActivity.this);
				alert.setTitle("Edit Ingredient");
				final int pos = position;

				// set an EditText view to get user input
				final EditText input =
						new EditText(SelectIngredientsActivity.this);
				input.setText(items.get(pos));
				alert.setView(input);

				alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// check for empty/whitespace string
						if (input.getText().toString().trim().length() == 0)
							db.deleteItem(items.get(pos));
						else
							db.updateItem(items.get(pos), new Ingredient(
									input.getText().toString().trim(), -1, ""));

						adapter.clear();
						items = db.getAllItemNames();
						adapter.addAll(items);
						adapter.notifyDataSetChanged();
					}
				});

				alert.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						db.deleteItem(items.get(pos));
						adapter.clear();
						items = db.getAllItemNames();
						adapter.addAll(items);
						adapter.notifyDataSetChanged();
					}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// canceled
					}
				});

				alert.show();
				return true;
			}
		});

		// Get Recipes button
		final Button showRecipesButton = (Button)
				findViewById(R.id.showRecipesButton);
		showRecipesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// generate API call
				String app_id = getString(R.string.yummly_app_id);
				String app_key = getString(R.string.yummly_app_key);
				String base = "http://api.yummly.com/v1/api/recipes?";
				String auth = "_app_id=" + app_id + "&_app_key=" + app_key;

				String allowed = "";
				for (int i = 0; i < items.size(); i++) {
					allowed += "&allowedIngredient[]=" +
							items.get(i).toLowerCase();
				}

				String api_call = base + auth + allowed;

				Intent i = new Intent(SelectIngredientsActivity.this,
						ShowRecipesActivity.class);
				i.putExtra("api_call", api_call);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.select_ingredients, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// Add Ingredient button
		if (id == R.id.action_add) {
			// create alert dialog
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Add Ingredient");

			// set an EditText view to get user input
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// check for empty/whitespace string
					if (input.getText().toString().trim().length() > 0) {
						db.addToPantry(new Ingredient(input.getText()
								.toString().trim(), -1, ""));
					}

					adapter.clear();
					items = db.getAllItemNames();
					adapter.addAll(items);
					adapter.notifyDataSetChanged();
				}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// canceled
				}
			});

			alert.show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
