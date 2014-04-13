package com.lahacksrecipeapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Alan on 4/12/14.
 */
public class SelectIngredientsActivity extends Activity {
    private ArrayList<String> items;
    private DBHandler db;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingredients);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DBHandler(this);

        if ( db.getItemsCount() < 1){
            db.addToPantry(new Item("Eggs" , "12"));
            db.addToPantry(new Item("Flour" , "Huge ass bag"));
        }
        items = db.getAllItemNames();

        // initialize ingredients list
        //items = new ArrayList<String>();

        // TODO: Remove diagnostic entries, add placeholder entry ("Click to add item...")
        //items.add("Eggs");
        //items.add("Flour");
        //items.add("Chocolate");
        /*for (int i = 0; i < 20; i++)

            items.add("Steak "+i);*/

        // initialize adapter with array
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items);

        // setup listView
        ListView lv = (ListView) findViewById(R.id.ingredientsListView);
        lv.setAdapter(adapter);

        // long press listView
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> av, View v, int position, long id) {
                // create alert dialog
                final AlertDialog.Builder alert =
                        new AlertDialog.Builder(SelectIngredientsActivity.this);
                alert.setTitle("Edit Ingredient");
                final int pos = position;

                // set an EditText view to get user input
                final EditText input = new EditText(SelectIngredientsActivity.this);
                input.setText(items.get(pos));
                alert.setView(input);
                alert.setMessage("Delete entry to remove ingredient.");

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // check for empty/whitespace string
                        if (input.getText().toString().trim().length() == 0){
                            db.deleteItem(items.get(pos));
                            //items.remove(pos);
                        }
                        else{
                            db.addToPantry(new Item(input.getText().toString(),"a trillion"));
                            //items.set(pos, input.getText().toString());
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
        });

        // Get Recipes button
        Button showRecipesButton = (Button) findViewById(R.id.showRecipesButton);
        showRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // generate API call
                String app_id = getString(R.string.app_id);
                String app_key = getString(R.string.app_key);
                String base = "http://api.yummly.com/v1/api/recipes?";
                String auth = "_app_id=" + app_id + "&_app_key=" + app_key;

                String allowed = "";
                for (int i = 0; i < items.size(); i++) {
                    allowed += "&allowedIngredient[]=" + items.get(i).toLowerCase();
                }

                String api_call = base + auth + allowed;

                Intent i = new Intent(SelectIngredientsActivity.this, ShowRecipesActivity.class);
                i.putExtra("api_call", api_call);
                startActivity(i);
            }
        });
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
            // create alert dialog
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Add Ingredient");

            // set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // check for empty/whitespace string
                    if (input.getText().toString().trim().length() > 0){
                        //items.add(input.getText().toString());
                        db.addToPantry(new Item(input.getText().toString(),"a trillion"));
                    }
                        //items.set(pos, input.getText().toString());
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
        else if (id == R.id.action_settings) {
            // TODO: list settings?
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
