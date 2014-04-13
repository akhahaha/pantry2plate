package com.lahacksrecipeapp.app;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jeremykao on 4/12/14.
 */
public class RecipeListItemAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<RecipeListItem> recipeListItems = new ArrayList<RecipeListItem>();
    private ListView lv;

    public RecipeListItemAdapter(Context context, ListView lv) throws IOException {
        this.context = context;
        this.lv = lv;
        /****** Mock Data ****
        ArrayList<String> swagIngredients = new ArrayList<String>();
        swagIngredients.add("1/2lb eggs");
        swagIngredients.add("yolo swag");
        swagIngredients.add("flouer");

        ArrayList<String> swagInstructions = new ArrayList<String>();
        swagInstructions.add("1. cook the eggs");
        swagInstructions.add("2. get the swag");
        swagInstructions.add("3. toss the flouer");

        RecipeListItem recipe1 = new RecipeListItem("http://vafoodbanks.org/wp-content/uploads/" +
                "2012/06/fresh_food.jpg", "Fresh Foods", "Meal", "This is a shit ton of fresh foods.",
                swagIngredients, swagInstructions);

        RecipeListItem recipe2 = new RecipeListItem("http://enterprisetoday.com/wp-content/" +
                "uploads/2013/03/In-N-Out-Burger.jpg", "In n Out", "Snack", "This is a in n out " +
                "motha fuckas.", swagIngredients, swagInstructions);

        RecipeListItem recipe3 = new RecipeListItem("http://4.bp.blogspot.com/_s--n1TR94Vs/S3O" +
                "MEXER5_I/AAAAAAAANmQ/-CUkDMwv3TY/s400/kale-chips-400x400-kalynskitchen.jpg"
                 ,"Kale Chips", "Meal", "These are Kale chips motha fuckasssssssss",
                swagIngredients, swagInstructions);

        recipeListItems.add(recipe1);
        recipeListItems.add(recipe2);
        recipeListItems.add(recipe3);

         ** End Mock Data *******/

    }
    public void addRecipeItems(JSONObject jsonResult) throws JSONException, IOException {

        Log.i("RecipeListItemAdapter.java", "addRecipeItems - this is jsonResult: " + jsonResult.toString());
        JSONArray matches = jsonResult.getJSONArray("matches");

        for(int i = 0; i < matches.length(); ++i){
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - i: " + i);
            JSONObject eachMatch = matches.getJSONObject(i);
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - eachMatch: " + eachMatch);
            String imgUrl = ((String)eachMatch.getJSONArray("smallImageUrls").get(0)).replace("=s90","=s300");
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - imgUrl: " + imgUrl);
            String recipeName = eachMatch.getString("recipeName");
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - recipeName: " + recipeName);
            ArrayList<String> ingredients = new ArrayList<String>();
            JSONArray tmpIngredients = eachMatch.getJSONArray("ingredients");
            for(int z = 0; z < tmpIngredients.length(); ++z){
                ingredients.add(tmpIngredients.getString(z));
            }
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - ingredients: " + ingredients);
            String category = "Meal";
            if (eachMatch.getJSONObject("attributes").length() > 0)
                category = (String) eachMatch.getJSONObject("attributes").getJSONArray("course").get(0);
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - category: " + category);
            String description = eachMatch.getString("id");
            Log.i("RecipeListItemAdapter.java", "addRecipeItems - description: " + description);
            RecipeListItem tmpRecipe = new RecipeListItem(imgUrl,recipeName,category,description,ingredients, null);
            this.recipeListItems.add(tmpRecipe);
        }

        Collections.sort(this.recipeListItems, new Comparator<RecipeListItem>() {
            @Override
            public int compare(RecipeListItem recipeListItem, RecipeListItem recipeListItem2) {
                return recipeListItem.getCategory().compareToIgnoreCase(recipeListItem2.getCategory());
            }
        });

        for(int i = 0; i < this.recipeListItems.size(); ++i){
            if (i == 0 || !this.recipeListItems.get(i-1).getCategory().equalsIgnoreCase(this.recipeListItems.get(i).getCategory())){
                this.recipeListItems.add(i, new RecipeListItem(this.recipeListItems.get(i).getCategory()));
            }
        }

        Log.i("RecipeListItemAdapter.java", "addRecipeItems - num in recipeListItems: " + this.recipeListItems.size());
        return;
    }
    public int getCount(){
        return recipeListItems.size();
    }
    public long getItemId(int position){
        return position;
    }
    public Object getItem(int position){
        return recipeListItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return recipeListItems.get(position).getTitle() == null ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    public void expandCategory(String category){
        for(int i = 0; i < RecipeListItemAdapter.this.recipeListItems.size(); ++i){
            if (!this.recipeListItems.get(i).getCategory().equalsIgnoreCase(category)){
                this.recipeListItems.remove(i);
                i--;
            }
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("RecipeListItemAdapter.java", "getView - getView called");
        final int type = getItemViewType(position);

        // First, let's create a new convertView if needed. You can also
        // create a ViewHolder to speed up changes if you want ;)
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(
                    type == 1 ? R.layout.separator_list_item : R.layout.recipe_list_item, parent, false);
        }

        // We can now fill the list item view with the appropriate data.
        if (type == 1) {
            ((TextView) convertView.findViewById(R.id.separator_list_item)).setText(this.recipeListItems.get(position).getCategory());
        } else {
            ((ImageView) convertView.findViewById(R.id.recipeListItemImage)).setImageBitmap(this.recipeListItems.get(position).getImage());
            ((TextView) convertView.findViewById(R.id.recipeListItemTitle)).setText(this.recipeListItems.get(position).getTitle());
        }

        return convertView;
    }
}
