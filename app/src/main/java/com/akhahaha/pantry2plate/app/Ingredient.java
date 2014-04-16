package com.akhahaha.pantry2plate.app;

/**
 * Ingredient data structure
 */
public class Ingredient {
	private String name;
	private int quantity;
	private String units;

	public Ingredient(String name, int quantity, String units) {
		this.name = name;
		this.quantity = quantity;
		this.units = units;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
}
