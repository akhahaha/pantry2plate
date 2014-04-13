package com.lahacksrecipeapp.app;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jeremykao on 4/12/14.
 */
public class ShowRecipesActivity extends ListActivity{

    RecipeListItemAdapter adapter = null;
    String api_call;
    JSONObject recipeJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);

        api_call = getIntent().getExtras().getString("api_call");
        Log.i("ShowRecipesActivity.java", "onCreate(): url= " + api_call);
        new GetRecipesTask().execute(this.api_call);

        try {
            adapter = new RecipeListItemAdapter(this.getApplicationContext(), this.getListView());

            //if (((RecipeListItem)adapter.getItem(0)).getImage() == null){
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }

        setListAdapter(adapter);
    }

    public void onPostExecute(){
        Log.i("ShowRecipesActivity", "inside UI onPostExecute");
        new RetreiveBitmapTask().execute("swag");
    }

    protected void onListItemClick(ListView lv, View v, int position, long id){
        RecipeListItem recipe = ((RecipeListItem)adapter.getItem(position));
        String title = recipe.getTitle();
        String category = recipe.getCategory();
        if (title != null){
            Intent intent = new Intent(ShowRecipesActivity.this, RecipeActivity.class);
            intent.putExtra("recipeTitle", recipe.getTitle());
            intent.putExtra("recipeDescription", recipe.getDescription());
            intent.putExtra("recipeCategory", recipe.getCategory());
            intent.putExtra("recipeIngredients", recipe.getIngredients());
            intent.putExtra("recipeDirections", recipe.getInstructions());
            intent.putExtra("bmp", recipe.getImage());

            startActivity(intent);
        }
        else{
            adapter.expandCategory(category);
            adapter.notifyDataSetChanged();
        }
    }

    protected class RetreiveBitmapTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        protected Void doInBackground(String... urls) {
            try {
                Log.i("RecipeListItem.java", "this is url: " + urls[0]);
                for (int i = 0; i < getListAdapter().getCount(); ++i){
                    RecipeListItem recipeListItem = (RecipeListItem) getListAdapter().getItem(i);
                    if(recipeListItem.getUrl() != null){
                        Log.i("RecipeListItem.java", "i: " + i);
                        String url = recipeListItem.getUrl();
                        URL newurl = new URL(url);
                        recipeListItem.setImage(BitmapFactory.decodeStream(newurl.openConnection().getInputStream()));
                        // recipeListItem.setImage(resizeBitMap(BitmapFactory.decodeStream(newurl.openConnection().getInputStream())));
                        Log.i("RecipeListItem.java", "Bitmap: " + recipeListItem.getImage().toString());
                    }
                }
                onPostExecute();
                return null;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   ShowRecipesActivity.this.adapter.notifyDataSetChanged();
                }
            });
        }

        private Bitmap resizeBitMap(Bitmap bmp){
            Log.i("RecipeListItem.java", "resizeBitMap start");
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int viewportWidth = ((ImageView)ShowRecipesActivity.this.findViewById(R.id.recipeListItemImage)).getWidth();
            int viewportHeight = ((ImageView)ShowRecipesActivity.this.findViewById(R.id.recipeListItemImage)).getHeight();
            Log.i("RecipeListItem.java", "resizeBitMap: " + viewportWidth + ", " + viewportHeight);
            //float maxScale = Math.max(viewportHeight / height, width / viewportWidth);
            float scaleH = viewportHeight;
            float scaleW = viewportWidth;

            Log.i("RecipeListItem.java", "scaleW, scaleH: " + scaleW + ", " + scaleH);

            Bitmap dest = Bitmap.createScaledBitmap(bmp, (int)scaleW, (int)scaleH, false);
            //Canvas cvs = new Canvas(dest);
            //cvs.drawBitmap(bmp, null, targetF, null);
            return dest;
        }
    }
    protected class GetRecipesTask extends AsyncTask<String, Void, Void> {

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
            final JSONObject tmpJson = this.recipeJSON;
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        ShowRecipesActivity.this.adapter.addRecipeItems(tmpJson);
                    } catch (JSONException e) {
                    } catch (IOException e) {
                    }
                    Log.i("ShowRecipesActivity", "log before calling onPostExecute");
                    ShowRecipesActivity.this.onPostExecute();
                    ShowRecipesActivity.this.adapter.notifyDataSetChanged();
                }
            });
        }

    }
}
