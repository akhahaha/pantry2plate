package com.lahacksrecipeapp.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
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

    public RecipeListItemAdapter(Context context) throws IOException {
        this.context = context;

        /****** Mock Data *****/
        ArrayList<String> swagIngredients = new ArrayList<String>();
        swagIngredients.add("1/2lb eggs");
        swagIngredients.add("yolo swag");
        swagIngredients.add("flouer");

        ArrayList<String> swagInstructions = new ArrayList<String>();
        swagIngredients.add("1. cook the eggs");
        swagIngredients.add("2. get the swag");
        swagIngredients.add("3. toss the flouer");

        RecipeListItem recipe1 = new RecipeListItem("http://vafoodbanks.org/wp-content/uploads/" +
                "2012/06/fresh_food.jpg", "Fresh Foods", "Meal", "This is a shit ton of fresh foods.",
                swagIngredients, swagInstructions);

        RecipeListItem recipe2 = new RecipeListItem("http://enterprisetoday.com/wp-content/" +
                "uploads/2013/03/In-N-Out-Burger.jpg", "In n Out", "Meal", "This is a in n out " +
                "motha fuckas.", swagIngredients, swagInstructions);

        RecipeListItem recipe3 = new RecipeListItem("http://4.bp.blogspot.com/_s--n1TR94Vs/S3O" +
                "MEXER5_I/AAAAAAAANmQ/-CUkDMwv3TY/s400/kale-chips-400x400-kalynskitchen.jpg"
                 ,"Kale Chips", "Snack", "These are Kale chips motha fuckasssssssss",
                swagIngredients, swagInstructions);

        recipeListItems.add(recipe1);
        recipeListItems.add(recipe2);
        recipeListItems.add(recipe3);

        /*** End Mock Data *******/
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recipe_list_item, parent, false);

        ImageView image = (ImageView) rowView.findViewById(R.id.recipeListItemImage);
        TextView title = (TextView) rowView.findViewById(R.id.recipeListItemTitle);

        image.setImageBitmap(this.recipeListItems.get(position).getImage());
        title.setText(this.recipeListItems.get(position).getTitle());

        Log.i("RecipeListItemAdapter.java", this.recipeListItems.get(position).getTitle());

        return rowView;
    }
}
