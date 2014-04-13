package com.lahacksrecipeapp.app;

import android.app.ListActivity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jeremykao on 4/12/14.
 */
public class ShowRecipesActivity extends ListActivity{

    RecipeListItemAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
        try {
            adapter = new RecipeListItemAdapter(this.getApplicationContext());

            if (((RecipeListItem)adapter.getItem(0)).getImage() == null){
              new RetreiveBitmapTask().execute("swag");
            }

            Log.i("ShowRecipesActivity.java", "recipelistitemadatper");
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListAdapter(adapter);
    }

    class RetreiveBitmapTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String... urls) {
            try {
                Log.i("RecipeListItem.java", "this is url: " + urls[0]);
                for (int i = 0; i < getListAdapter().getCount(); ++i){
                    RecipeListItem recipeListItem = (RecipeListItem) getListAdapter().getItem(i);
                    String url = recipeListItem.getUrl();
                    URL newurl = new URL(url);
                    recipeListItem.setImage(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
                    Log.i("RecipeListItem.java", "Bitmap: " + recipeListItem.getImage().toString());
                }
                onPostExecute();
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute() {
            Log.i("RecipeListItem.java", "bmp swag");
            ShowRecipesActivity.this.adapter.notifyDataSetChanged();
        }
    }

}
