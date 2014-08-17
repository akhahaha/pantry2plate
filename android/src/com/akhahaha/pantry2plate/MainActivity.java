package com.akhahaha.pantry2plate;

import com.akhahaha.pantry2plate.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageView cover = (ImageView) findViewById(R.id.coverart);
		cover.setImageResource(R.drawable.coverart);
		cover.setScaleType(ImageView.ScaleType.CENTER_CROP);

		final Button selectIngredientsButton = (Button) findViewById(R.id.selectIngredientsButton);
		selectIngredientsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this,
						SelectIngredientsActivity.class);
				startActivity(i);
			}
		});
	}

}
