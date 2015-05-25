package com.maxkalavera.ecoar.signup;

import java.util.Calendar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.maxkalavera.ecoar.BaseActivity;
import com.maxkalavera.ecoar.R;
import com.maxkalavera.ecoar.home.Home;
import com.maxkalavera.ecoar.login.LoginFragmentHTTPLoader;
import com.maxkalavera.ecoar.login.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.SignUpErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.squareup.okhttp.Response;

public class SignUp extends BaseActivity implements LoaderCallbacks<ResponseBundle> {

	private static final String Male = "male";
	private static final String Female = "female";
	private static final int SEND_DATA = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.signup);
	}
	
	public void sendData() {
		this.getSupportLoaderManager().initLoader(SEND_DATA, null, this);
	}
	
	public void lockInputs() {
		findViewById(R.id.signup_username).setEnabled(false);
		findViewById(R.id.signup_password).setEnabled(false);
		findViewById(R.id.signup_password_confirmation).setEnabled(false);
		findViewById(R.id.signup_first_name).setEnabled(false);
		findViewById(R.id.signup_last_name).setEnabled(false);
		findViewById(R.id.signup_email).setEnabled(false);
		findViewById(R.id.signup_birthday).setEnabled(false);
		findViewById(R.id.signup_gender).setEnabled(false);
	}
	
	private int[] errorMessageIdArray = {
			R.id.signup_username_message,
			R.id.signup_password_message,
			R.id.signup_password_confirmation_message,
			R.id.signup_first_name_message,
			R.id.signup_last_name_message,
			R.id.signup_email_message,
			R.id.signup_birthday_message,
			R.id.signup_gender_message
	};
	
	public void clearCorretions() {
		for (int i = 0; i < errorMessageIdArray.length; i++) {
			findViewById(errorMessageIdArray[i]).setVisibility(View.GONE);
		}
	}
	
	public void setCorretions(SignUpErrorsJsonModel signUpErros) {
		
		if (signUpErros.username != null) {
			TextView username = (TextView) findViewById(R.id.signup_username_message);
			username.setVisibility(View.VISIBLE);
			username.setText(signUpErros.username);
		}
		
		if (signUpErros.password != null) {
			TextView password = (TextView) findViewById(R.id.signup_password_message);
			password.setVisibility(View.VISIBLE);
		}
		
		if (signUpErros.password_confirmation != null) {
			TextView passwordConfirmation = (TextView) findViewById(R.id.signup_password_confirmation_message);
			passwordConfirmation.setVisibility(View.VISIBLE);
			passwordConfirmation.setText(signUpErros.password_confirmation);
		}
		
		if (signUpErros.first_name != null) {
			TextView firstName = (TextView) findViewById(R.id.signup_first_name_message);
			firstName.setVisibility(View.VISIBLE);
			firstName.setText(signUpErros.first_name);
		}
		
		if (signUpErros.last_name != null) {
			TextView lastName = (TextView) findViewById(R.id.signup_last_name_message);
			lastName.setVisibility(View.VISIBLE);
			lastName.setText(signUpErros.last_name);
		}
		
		if (signUpErros.email != null) {
			TextView email = (TextView) findViewById(R.id.signup_email_message);
			email.setVisibility(View.VISIBLE);
			email.setText(signUpErros.email);
		}
		
		if (signUpErros.birthdate != null) {
			TextView birthday = (TextView) findViewById(R.id.signup_birthday_message);
			birthday.setVisibility(View.VISIBLE);
			birthday.setText(signUpErros.birthdate);
		}
		
		if (signUpErros.gender != null) {
			TextView gender = (TextView) findViewById(R.id.signup_gender_message);
			gender.setVisibility(View.VISIBLE);
			gender.setText(signUpErros.gender);
		}
	}
	
	
	/************************************************************
	 * HTTP Request methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		switch (loaderID) {
			case 0:
				clearCorretions();
				
				UserDataJsonModel userData = new UserDataJsonModel();
				//userDataJsonModel.username = 
				EditText username = (EditText) findViewById(R.id.signup_username);
				userData.username = username.getText().toString();
				
				EditText password = (EditText) findViewById(R.id.signup_password);
				userData.password = password.getText().toString();
				
				EditText passwordConfirmation = (EditText) findViewById(R.id.signup_password_confirmation);
				userData.password_confirmation = passwordConfirmation.getText().toString();
				
				EditText firstName = (EditText) findViewById(R.id.signup_first_name);
				userData.first_name = firstName.getText().toString();
				
				EditText lastName = (EditText) findViewById(R.id.signup_last_name);
				userData.last_name = lastName.getText().toString();
				
				EditText email = (EditText) findViewById(R.id.signup_email);
				userData.email = email.getText().toString();
				
				RadioGroup gender = (RadioGroup) findViewById(R.id.signup_gender);
				switch(gender.getCheckedRadioButtonId()) {
					case R.id.signup_gender_male:
						userData.gender = Male;
						break;
						
					case R.id.signup_gender_female:
						userData.gender = Female;
						break;
						
					default:
						userData.gender = "";
						break;
				}
				
				DatePicker birthday = (DatePicker) findViewById(R.id.signup_birthday);
				Calendar birthdayCalendar = Calendar.getInstance();
				birthdayCalendar.set(birthday.getYear(), birthday.getMonth(), birthday.getDayOfMonth());
				userData.birthdate = birthdayCalendar;
				
				RequestParamsBundle paramsBundle = new RequestParamsBundle();
				paramsBundle.AddJsonModel("userdata", userData);
				
				// set loader on 
				ProgressBar loading = (ProgressBar) findViewById(R.id.signup_send_progressbar);
				loading.setVisibility(View.VISIBLE);
				Button sendButton = (Button) findViewById(R.id.signup_send_button);
				sendButton.setVisibility(View.GONE);
				
				//LoginFragmentHTTPLoader loader = 
				//	new LoginFragmentHTTPLoader(getActivity(), this.paramsBundle);
				//loader.forceLoad();
				//return loader;
				return null;
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch (loader.getId()) {
		case 0:
			Response response = responseBundle.getResponse();
			if(response != null) {
				if (response.isSuccessful()) {
					ProgressBar loading = (ProgressBar) findViewById(R.id.signup_send_progressbar);
					loading.setVisibility(View.GONE);
					TextView successText = (TextView) findViewById(R.id.signup_send_text);
					successText.setVisibility(View.GONE);
					this.lockInputs();
					
				} else {
					ProgressBar loading = (ProgressBar) findViewById(R.id.signup_send_progressbar);
					loading.setVisibility(View.GONE);
					Button sendButton = (Button) findViewById(R.id.signup_send_button);
					sendButton.setVisibility(View.VISIBLE);
					
					SignUpErrorsJsonModel signUpErrors = (SignUpErrorsJsonModel) responseBundle.getResponseJsonObject();
					if (signUpErrors != null) {
						this.setCorretions(signUpErrors);
					}
				}
			} else {
				ProgressBar loading = (ProgressBar) findViewById(R.id.signup_send_progressbar);
				loading.setVisibility(View.GONE);
				Button sendButton = (Button) findViewById(R.id.signup_send_button);
				sendButton.setVisibility(View.VISIBLE);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<ResponseBundle> loader) {		
	}
	
};
