package com.lahacksrecipeapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cover = (ImageView) findViewById(R.id.coverart);
        cover.setImageResource(R.drawable.coverart);
        cover.setScaleType(ImageView.ScaleType.CENTER_CROP);

        final Button selectIngredientsButton = (Button) findViewById(R.id.selectIngredientsButton);
        selectIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                // selectIngredientsButton.setBackgroundColor(0x008000); // green
                Intent i = new Intent(MainActivity.this, SelectIngredientsActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Has here. The action bar will
        // aundle action bar item clicktomatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
