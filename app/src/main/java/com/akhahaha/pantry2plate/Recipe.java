package com.akhahaha.pantry2plate;

import android.graphics.Bitmap;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Recipe Data Structure
 */
public class Recipe {
	private String url;
	private Bitmap image;
	private String title;
	private String category;
	private String description;
	private ArrayList<String> ingredients;
	private ArrayList<String> instructions;

	public Recipe(String title) {
		this.url = null;
		this.image = null;
		this.title = null;
		this.category = title;
		this.description = null;
		this.ingredients = null;
		this.instructions = null;
	}

	public Recipe(String url, String title, String category, String description,
	              ArrayList<String> ingredients, ArrayList<String> instructions) throws IOException {
		this.image = null;
		this.url = url;
		this.title = title;
		this.category = category;
		this.description = description;
		this.ingredients = ingredients;
		this.instructions = instructions;
	}

	public String getUrl() {
		return this.url;
	}

	public Bitmap getImage() {
		return this.image;
	}

	public void setImage(Bitmap bmp) {
		this.image = bmp;
	}

	public String getTitle() {
		return this.title;
	}

	public String getCategory() {
		return this.category;
	}

	public String getDescription() {
		return this.description;
	}

	public ArrayList<String> getIngredients() {
		return this.ingredients;
	}

	public ArrayList<String> getInstructions() {
		return this.instructions;
	}
}
