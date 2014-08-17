package com.akhahaha.pantry2plate;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import com.akhahaha.pantry2plate.R;

import java.io.IOException;
import java.net.URL;

public class ShowRecipesActivity extends ListActivity {

	RecipeListItemAdapter adapter = null;
	String api_call;
	JSONObject recipeJSON;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_recipes);

		api_call = getIntent().getExtras().getString("api_call");
		new GetRecipesTask().execute(this.api_call);

		try {
			adapter = new RecipeListItemAdapter(this.getApplicationContext(),
					this.getListView());
		} catch (IOException e) {
			e.printStackTrace();
		}

		setListAdapter(adapter);
	}

	public void onPostExecute() {
		new RetreiveBitmapTask().execute("swag");
	}

	protected void onListItemClick(ListView lv, View v, int position, long id) {
		Recipe recipe = ((Recipe) adapter.getItem(position));
		String title = recipe.getTitle();
		String category = recipe.getCategory();
		if (title != null) {
			Intent intent = new Intent(ShowRecipesActivity.this,
					RecipeActivity.class);
			intent.putExtra("recipeTitle", recipe.getTitle());
			intent.putExtra("recipeDescription", recipe.getDescription());
			intent.putExtra("recipeCategory", recipe.getCategory());
			intent.putExtra("recipeIngredients", recipe.getIngredients());
			intent.putExtra("recipeDirections", recipe.getInstructions());
			intent.putExtra("bmp", recipe.getImage());

			startActivity(intent);
		} else {
			adapter.expandCategory(category);
			adapter.notifyDataSetChanged();
		}
	}

	protected class RetreiveBitmapTask extends AsyncTask<String, Void, Void> {

		protected Void doInBackground(String... urls) {
			try {
				Log.i("Recipe.java", "API Call: " + urls[0]);
				for (int i = 0; i < getListAdapter().getCount(); ++i) {
					Recipe recipe = (Recipe) getListAdapter().getItem(i);
					if (recipe.getUrl() != null) {
						String url = recipe.getUrl();
						URL newurl = new URL(url);
						recipe.setImage(BitmapFactory.decodeStream(newurl
								.openConnection().getInputStream()));
					}
				}
				onPostExecute();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ShowRecipesActivity.this.adapter.notifyDataSetChanged();
				}
			});
		}
	}

	protected class GetRecipesTask extends AsyncTask<String, Void, Void> {
		private JSONObject recipeJSON;

		protected Void doInBackground(String... urls) {
			try {
				ServiceHandler sh = new ServiceHandler();
				Log.i("CourseProfile", "API Call: " + urls[0]);
				String jsonStr = sh
						.makeServiceCall(urls[0], ServiceHandler.GET);

				if (jsonStr != null) {
					try {
						recipeJSON = new JSONObject(jsonStr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.e("ServiceHandler", "No data received.");
				}
				onPostExecute();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute() throws JSONException, IOException {
			final JSONObject tmpJson = this.recipeJSON;
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						ShowRecipesActivity.this.adapter
								.addRecipeItems(tmpJson);
					} catch (JSONException e) {
					} catch (IOException e) {
					}

					ShowRecipesActivity.this.onPostExecute();
					ShowRecipesActivity.this.adapter.notifyDataSetChanged();
				}
			});
		}
	}

}
