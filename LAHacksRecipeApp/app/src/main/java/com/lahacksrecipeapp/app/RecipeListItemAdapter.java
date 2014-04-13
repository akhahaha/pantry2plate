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
    private ListView lv;

    public RecipeListItemAdapter(Context context, ListView lv) throws IOException {
        this.context = context;
        this.lv = lv;
        /****** Mock Data *****/
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
        //LayoutInflater inflater = (LayoutInflater) context
        //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View rowView = inflater.inflate(R.layout.recipe_list_item, parent, false);

        //ImageView image = (ImageView) rowView.findViewById(R.id.recipeListItemImage);
        //TextView title = (TextView) rowView.findViewById(R.id.recipeListItemTitle);

        /*if (this.recipeListItems.get(position).getTitle() == null){
            Log.i("RecipeListItemAdapter", "position swag: " + (position - this.lv.getFirstVisiblePosition() + 1) );
            if (this.lv.getChildAt(position - this.lv.getFirstVisiblePosition() + 1) == null)
                this.lv.getAdapter().getView(position, this.lv.getChildAt(position - this.lv.getFirstVisiblePosition() + 1), this.lv);
            else{
                this.lv.getChildAt(position - this.lv.getFirstVisiblePosition() + 1).getLayoutParams().height = 0;
            }
            image.setVisibility(View.INVISIBLE);
            title.setBackgroundColor(Color.WHITE);
            title.setText(this.recipeListItems.get(position).getCategory());
        }
        else{
            image.setImageBitmap(this.recipeListItems.get(position).getImage());
            title.setText(this.recipeListItems.get(position).getTitle());
        }

        //Log.i("RecipeListItemAdapter.java", this.recipeListItems.get(position).getTitle());

        return rowView;*/

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
