package com.lahacksrecipeapp.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jeremykao on 4/12/14.
 */
public class RecipeListItem {
    private String url;
    private Bitmap image;
    private String title;
    private String category;
    private String description;
    private ArrayList<String> ingredients;
    private ArrayList<String> instructions;

    public RecipeListItem(String title){
        this.url = null;
        this.image = null;
        this.title = null;
        this.category = title;
        this.description = null;
        this.ingredients = null;
        this.instructions = null;
    }
    public RecipeListItem(String url, String title, String category, String description,
                          ArrayList<String> ingredients, ArrayList<String> instructions) throws IOException {
        this.image = null;
        this.url = url;
        this.title = title;
        this.category = category;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }
    public String getUrl(){
        return this.url;
    }
    public Bitmap getImage(){
        return this.image;
    }
    public void setImage(Bitmap bmp){
        this.image = bmp;
    }
    public String getTitle(){
        return this.title;
    }
    public String getCategory(){
        return this.category;
    }
    public String getDescription(){
        return this.description;
    }
    public ArrayList<String> getIngredients(){
        return this.ingredients;
    }
    public ArrayList<String> getInstructions(){
        return this.instructions;
    }
}
