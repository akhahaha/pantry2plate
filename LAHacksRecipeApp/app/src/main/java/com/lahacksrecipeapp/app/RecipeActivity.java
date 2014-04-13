package com.lahacksrecipeapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jeremykao on 4/12/14.
 */
public class RecipeActivity extends Activity {

    protected String recipeUrl = "";
    public TextView directions;
    public TextView ingredients;
    public TextView time;
    public TextView yield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ImageView image = (ImageView) findViewById(R.id.recipeImage);
        TextView title = (TextView) findViewById(R.id.recipeTitle);
        this.ingredients = (TextView) findViewById(R.id.ingredientText);
        this.directions = (TextView) findViewById(R.id.directionText);
        this.time = (TextView) findViewById(R.id.time);
        this.yield = (TextView) findViewById(R.id.yield);

        Intent recipeInfo = getIntent();
        if (recipeInfo != null){
            Bitmap bmp = (Bitmap) recipeInfo.getParcelableExtra("bmp");
            if (bmp != null)
                image.setImageBitmap(bmp);
            title.setText(recipeInfo.getStringExtra("recipeTitle"));

            String recipeId = recipeInfo.getStringExtra("recipeDescription");
            String app_id = getString(R.string.app_id);
            String app_key = getString(R.string.app_key);
            String base = "http://api.yummly.com/v1/api/recipe/" + recipeId +"?";
            String auth = "_app_id=" + app_id + "&_app_key=" + app_key;

            this.recipeUrl = base + auth;
            new GetRecipeTask().execute(this.recipeUrl);
            /*ArrayList<String> ingredientsArr = recipeInfo.getStringArrayListExtra("recipeIngredients");
            String ingredientsStr = "";
            for (int i = 0; i < ingredientsArr.size(); ++i){
                ingredientsStr += ingredientsArr.get(i) + " \n";
            }
            ArrayList<String> directionsArr = recipeInfo.getStringArrayListExtra("recipeDirections");
            String directionsStr = "";
            for (int i = 0; i < directionsArr.size(); ++i){
                directionsStr += directionsArr.get(i) + " \n";
            }
            ingredients.setText(ingredientsStr);
            directions.setText(directionsStr);
            */
        }

    }

    protected void fillFields(String dire, String ingred){
        this.ingredients = (TextView) findViewById(R.id.ingredientText);
        this.ingredients.setText(ingred);

        //this.directions = (TextView) findViewById(R.id.directionText);
        //this.directions.setText(dire);
        return;
    }

    protected class GetRecipeTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private JSONObject recipeJSON;
        protected Void doInBackground(String... urls) {
            try {
                ServiceHandler sh = new ServiceHandler();
                Log.i("CourseProfile", "this is the url hit: " + urls[0]);
                String jsonStr = sh.makeServiceCall(urls[0], ServiceHandler.GET);

                Log.d("Response: ", "> " + jsonStr);

                if(jsonStr != null) {
                    try {
                        recipeJSON = new JSONObject(jsonStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
                onPostExecute();
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute() throws JSONException, IOException {
            //Log.i("RecipeActivity", "OnPostExecute: json returned" + this.recipeJSON);
            JSONArray tmpIngredients = this.recipeJSON.getJSONArray("ingredientLines");
            String ingredients = "";
            for(int z = 0; z < tmpIngredients.length(); ++z){
                Log.i("RecipeActivity", "OnPostExecute: z" + tmpIngredients.getString(z));
                ingredients += "->" + tmpIngredients.getString(z) + "\n";
            }
            final String ingredientsStr = ingredients;
            String sourceUrl = this.recipeJSON.getJSONObject("source").getString("sourceRecipeUrl");
            //Log.i("RecipeActivity", "OnPostExecute: sourceUrl" + sourceUrl);

            final String directions = "Check out the preparation instructions at " + sourceUrl + "\n";

            final String yield = this.recipeJSON.getString("numberOfServings");
            final String time = this.recipeJSON.getString("totalTime");

            //Log.i("RecipeActivity", "OnPostExecute: directions" + directions);
            //Log.i("RecipeActivity", "OnPostExecute: ingredients" + ingredients);
            //RecipeActivity.this.fillFields(directions, ingredients);

            //(TextView)(RecipeActivity.this.findViewById(R.id.ingredientText)).setText(ingredients);
            //RecipeActivity.this.directions.setText(directions);
            //RecipeActivity.this.fillFields(directions, ingredients);
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    RecipeActivity.this.ingredients.setText(ingredientsStr);
                    RecipeActivity.this.directions.setText(directions);
                    RecipeActivity.this.time.setText(time);
                    RecipeActivity.this.yield.setText("Serves " + yield);
                }
            });

        }


    }
}
