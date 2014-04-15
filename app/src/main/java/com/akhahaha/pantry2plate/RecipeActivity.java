package com.akhahaha.pantry2plate;

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

import java.io.IOException;

public class RecipeActivity extends Activity {
	public TextView directions;
	public TextView ingredients;
	public TextView time;
	public TextView yield;
	protected String recipeUrl = "";

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
		if (recipeInfo != null) {
			Bitmap bmp = (Bitmap) recipeInfo.getParcelableExtra("bmp");
			if (bmp != null)
				image.setImageBitmap(bmp);
			title.setText(recipeInfo.getStringExtra("recipeTitle"));

			String recipeId = recipeInfo.getStringExtra("recipeDescription");
			String app_id = getString(R.string.yummly_app_id);
			String app_key = getString(R.string.yummly_app_key);
			String base = "http://api.yummly.com/v1/api/recipe/" +
					recipeId + "?";
			String auth = "_app_id=" + app_id + "&_app_key=" + app_key;

			this.recipeUrl = base + auth;
			new GetRecipeTask().execute(this.recipeUrl);
		}

	}

	protected void fillFields(String dire, String ingredient) {
		this.ingredients = (TextView) findViewById(R.id.ingredientText);
		this.ingredients.setText(ingredient);
	}

	protected class GetRecipeTask extends AsyncTask<String, Void, Void> {
		private Exception exception;
		private JSONObject recipeJSON;

		protected Void doInBackground(String... urls) {
			try {
				ServiceHandler sh = new ServiceHandler();
				Log.i("CourseProfile", "API Call: " + urls[0]);
				String jsonStr =
						sh.makeServiceCall(urls[0], ServiceHandler.GET);

				if (jsonStr != null) {
					try {
						recipeJSON = new JSONObject(jsonStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.e("ServiceHandler",
							"No data received.");
				}
				onPostExecute();
				return null;
			} catch (Exception e) {
				this.exception = e;
				return null;
			}
		}

		protected void onPostExecute() throws JSONException, IOException {
			JSONArray tmpIngredients =
					this.recipeJSON.getJSONArray("ingredientLines");
			String ingredients = "";
			for (int z = 0; z < tmpIngredients.length(); ++z) {
				ingredients += " * " + tmpIngredients.getString(z) + "\n\n";
			}
			final String ingredientsStr = ingredients;
			String sourceUrl =
					this.recipeJSON.getJSONObject("source")
							.getString("sourceRecipeUrl");

			final String directions =
					"Preparation instructions available at:\n" + sourceUrl;
			final String yield = this.recipeJSON.getString("numberOfServings");
			final String time = this.recipeJSON.getString("totalTime");

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					RecipeActivity.this.ingredients.setText(ingredientsStr);
					RecipeActivity.this.directions.setText(directions);
					if (!time.equalsIgnoreCase("null"))
						RecipeActivity.this.time.setText(time);
					if (!yield.equalsIgnoreCase("null"))
						RecipeActivity.this.yield.setText("Serves " + yield);
				}
			});
		}
	}
}
