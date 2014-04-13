package com.lahacksrecipeapp.app;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Alan on 4/12/14.
 */
public class SelectIngredientsActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectingredients);

        // initialize ingredients list
        ArrayList<String> items = new ArrayList<String>();
        items.add("Eggs");
        items.add("Flour");
        items.add("Chocolate");

        // initialize adapter with array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items);

        // setup listView
        setListAdapter(adapter);
    }
}
