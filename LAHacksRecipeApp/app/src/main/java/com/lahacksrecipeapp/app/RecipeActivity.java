package com.lahacksrecipeapp.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jeremykao on 4/12/14.
 */
public class RecipeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ImageView image = (ImageView) findViewById(R.id.recipeImage);
        TextView title = (TextView) findViewById(R.id.recipeTitle);
        TextView ingredients = (TextView) findViewById(R.id.ingredients);
        TextView directions = (TextView) findViewById(R.id.directions);

        Intent recipeInfo = getIntent();
        if (recipeInfo != null){
            Bitmap bmp = (Bitmap) recipeInfo.getParcelableExtra("bmp");
            if (bmp != null)
                image.setImageBitmap(bmp);
            title.setText(recipeInfo.getStringExtra("recipeTitle"));
            ArrayList<String> ingredientsArr = recipeInfo.getStringArrayListExtra("recipeIngredients");
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
        }

    }
}
