package com.platzerworld.biergartenfinder.social.flickr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActivity;
import com.platzerworld.biergartenfinder.BiergartenActivity;
import com.platzerworld.biergartenfinder.R;
import com.platzerworld.biergartenfinder.network.NetworkActivity;

import org.scribe.model.Token;

public class LoginActivity extends OAuthLoginActivity<FlickrClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flickr_activity_login);

		Button btnFlickrLogin = (Button)findViewById(R.id.btnFlickrLogin);
		btnFlickrLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "no you will be forward for login!",Toast.LENGTH_SHORT).show();
				loginToRest();
			}
		});

		Button btnFlickrLogout = (Button)findViewById(R.id.btnFlickrLogout);
		btnFlickrLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(LoginActivity.this, "no you will be logged out!",Toast.LENGTH_SHORT).show();
				FlickrClient flickrClient = getClient();
				Token token = flickrClient.getRequestToken();
				flickrClient.clearAccessToken();

			}
		});

		Button btnShowPhotos = (Button)findViewById(R.id.btnShowPhotos);
		btnShowPhotos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FlickrClient flickrClient = getClient();
				if(flickrClient.isAuthenticated()) {
					Intent i = new Intent(LoginActivity.this, PhotosActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(LoginActivity.this, "no you will be forwarded to login",Toast.LENGTH_SHORT).show();
					loginToRest();
				}

			}
		});

		Button btnGoBack = (Button)findViewById(R.id.btnGoBack);
		btnGoBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void finish() {
		// Prepare data intent
		Intent data = new Intent();
		data.putExtra("login", "true");
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		super.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_flickr_login, menu);
		return true;
	}
	
    @Override
    public void onLoginSuccess() {
    	FlickrClient flickrClient = getClient();
		Token token = flickrClient.getRequestToken();

    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    public void loginToRest() {
        getClient().connect();
    }

}
