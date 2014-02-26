package com.maxkalavera.ecoar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class WellcomeView extends Activity implements OnClickListener{

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loginview_intro);
		
		
		Button button= (Button) findViewById(R.id.skip_login_button);
		button.setOnClickListener(this);
	}
	
    @Override
    public void onClick(View button) {
        Intent intent = new Intent(WellcomeView.this, Home.class);
        startActivity(intent);
    }
	
}