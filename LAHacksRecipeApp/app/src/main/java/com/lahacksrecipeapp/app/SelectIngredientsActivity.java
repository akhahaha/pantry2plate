package com.lahacksrecipeapp.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Alan on 4/12/14.
 */
public class SelectIngredientsActivity extends ActionBarActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingredients);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize ingredients list
        items = new ArrayList<String>();
        items.add("Eggs");
        items.add("Flour");
        items.add("Chocolate");

        // initialize adapter with array
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items);

        // setup listView
        ListView lv = (ListView) findViewById(R.id.ingredientsListView);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            // prompt for user input
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Add Ingredient");
            // alert.setMessage("Message");

            // set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    items.add(input.getText().toString());
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
            return true;
        }
        else if (id == R.id.action_settings) {
            // TODO: list settings?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
