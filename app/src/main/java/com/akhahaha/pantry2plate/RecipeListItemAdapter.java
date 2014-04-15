package com.akhahaha.pantry2plate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecipeListItemAdapter extends BaseAdapter {
	private final Context context;
	private ArrayList<Recipe> recipes = new ArrayList<Recipe>();
	private ListView lv;

	public RecipeListItemAdapter(Context context, ListView lv)
			throws IOException {
		this.context = context;
		this.lv = lv;
	}

	public void addRecipeItems(JSONObject jsonResult)
			throws JSONException, IOException {
		JSONArray matches = jsonResult.getJSONArray("matches");

		for (int i = 0; i < matches.length(); i++) {
			JSONObject eachMatch = matches.getJSONObject(i);
			String imgUrl = ((String) eachMatch.getJSONArray("smallImageUrls")
					.get(0)).replace("=s90", "=s300");
			String recipeName = eachMatch.getString("recipeName");
			ArrayList<String> ingredients = new ArrayList<String>();

			JSONArray tmpIngredients = eachMatch.getJSONArray("ingredients");
			for (int z = 0; z < tmpIngredients.length(); ++z)
				ingredients.add(tmpIngredients.getString(z));

			String category = "Meals";
			if (eachMatch.getJSONObject("attributes").length() > 0) {
				try {
					if (eachMatch.getJSONObject("attributes")
							.getJSONArray("course") != null)
						category = (String) eachMatch.getJSONObject
								("attributes").getJSONArray("course").get(0);
				} catch (Exception e) {
				}
				;
			}

			String description = eachMatch.getString("id");
			Recipe tmpRecipe = new Recipe(imgUrl, recipeName,
					category, description, ingredients, null);
			this.recipes.add(tmpRecipe);
		}

		Collections.sort(this.recipes,
				new Comparator<Recipe>() {
					@Override
					public int compare(Recipe recipe, Recipe recipe2) {
						return recipe.getCategory()
								.compareToIgnoreCase(recipe2.getCategory());
					}
				}
		);

		for (int i = 0; i < this.recipes.size(); i++) {
			if (i == 0 || !this.recipes.get(i - 1).getCategory()
					.equalsIgnoreCase(this.recipes.get(i)
							.getCategory()))
				this.recipes.add(i, new Recipe(
						this.recipes.get(i).getCategory()));
		}

		return;
	}

	public int getCount() {
		return recipes.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public Object getItem(int position) {
		return recipes.get(position);
	}

	@Override
	public int getItemViewType(int position) {
		return recipes.get(position).getTitle() == null ? 1 : 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public void expandCategory(String category) {
		for (int i = 0;
		     i < RecipeListItemAdapter.this.recipes.size(); i++) {
			if (!this.recipes.get(i).getCategory()
					.equalsIgnoreCase(category)) {
				this.recipes.remove(i);
				i--;
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int type = getItemViewType(position);

		// First, let's create a new convertView if needed. You can also
		// create a ViewHolder to speed up changes if you want ;)
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					type == 1 ? R.layout.separator_list_item :
							R.layout.recipe_list_item, parent, false
			);
		}

		// We can now fill the list item view with the appropriate data.
		if (type == 1) {
			((TextView) convertView.findViewById(R.id.separator_list_item))
					.setText(this.recipes.get(position).getCategory());
		} else {
			ImageView img = (ImageView)
					convertView.findViewById(R.id.recipeListItemImage);
			img.setImageBitmap(this.recipes.get(position).getImage());
			img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			((TextView) convertView.findViewById(R.id.recipeListItemTitle))
					.setText(this.recipes.get(position).getTitle());
		}

		return convertView;
	}
}
