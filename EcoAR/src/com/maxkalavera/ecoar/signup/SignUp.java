package com.maxkalavera.ecoar.signup;

import java.util.Calendar;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.maxkalavera.utils.ErrorMesages;
import com.maxkalavera.utils.InternetStatusChecker;
import com.maxkalavera.utils.LogoutChecker;
import com.maxkalavera.utils.database.UserSessionDAO;
import com.maxkalavera.utils.database.jsonmodels.LoginErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.SignUpErrorsJsonModel;
import com.maxkalavera.utils.database.jsonmodels.UserDataJsonModel;
import com.maxkalavera.utils.httprequest.RequestParamsBundle;
import com.maxkalavera.utils.httprequest.ResponseBundle;
import com.squareup.okhttp.Response;

public class SignUp extends BaseActivity implements LoaderCallbacks<ResponseBundle>,
	OnClickListener {

	
	private static final int SEND_DATA = 1;
	
	private static final String MALE = "M";
	private static final String FEMALE = "F";
	private static final int ELDERST_AGE = 115;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.signup);
	    this.setUp();
	}
	
	public void setUp() {
		DatePicker birthday = (DatePicker) findViewById(R.id.signup_birthday);
		Calendar calendar = Calendar.getInstance(); 
		birthday.setMinDate(calendar.get(Calendar.YEAR)-ELDERST_AGE);
	    birthday.setMaxDate(calendar.get(Calendar.YEAR));
	    
	    findViewById(R.id.signup_send_button).setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Button sendButton = (Button) findViewById(R.id.signup_send_button);
		sendButton.setOnClickListener(null);
		sendButton.setVisibility(View.GONE);
		findViewById(R.id.signup_send_progressbar).setVisibility(View.VISIBLE);
		
		this.getSupportLoaderManager().restartLoader(SEND_DATA, null, this);
	}
	
	/************************************************************
	 * 
	 ************************************************************/
	public void lockInputs() {
		findViewById(R.id.signup_username).setEnabled(false);
		findViewById(R.id.signup_password).setEnabled(false);
		findViewById(R.id.signup_password_confirmation).setEnabled(false);
		findViewById(R.id.signup_first_name).setEnabled(false);
		findViewById(R.id.signup_last_name).setEnabled(false);
		findViewById(R.id.signup_email).setEnabled(false);
		findViewById(R.id.signup_birthday).setEnabled(false);
		findViewById(R.id.signup_gender).setEnabled(false);
		findViewById(R.id.signup_gender_male).setEnabled(false);
		findViewById(R.id.signup_gender_female).setEnabled(false);
	}
	
	/************************************************************
	 * 
	 ************************************************************/
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
			username.setText(getResources().getString(R.string.signup_username_error)
					+" "+signUpErros.username.get(0));
		}
		
		if (signUpErros.password != null) {
			TextView password = (TextView) findViewById(R.id.signup_password_message);
			password.setVisibility(View.VISIBLE);
			password.setText(getResources().getString(R.string.signup_password_error)
					+" "+signUpErros.password.get(0));
		}
		
		if (signUpErros.password_confirmation != null) {
			TextView passwordConfirmation = (TextView) findViewById(R.id.signup_password_confirmation_message);
			passwordConfirmation.setVisibility(View.VISIBLE);
			passwordConfirmation.setText(getResources().getString(R.string.signup_password_confirmation_error)
					+" "+signUpErros.password_confirmation.get(0));
		}
		
		if (signUpErros.first_name != null) {
			TextView firstName = (TextView) findViewById(R.id.signup_first_name_message);
			firstName.setVisibility(View.VISIBLE);
			firstName.setText(getResources().getString(R.string.signup_first_name_error)
					+" "+signUpErros.first_name.get(0));
		}
		
		if (signUpErros.last_name != null) {
			TextView lastName = (TextView) findViewById(R.id.signup_last_name_message);
			lastName.setVisibility(View.VISIBLE);
			lastName.setText(getResources().getString(R.string.signup_last_name_error)
					+" "+signUpErros.last_name.get(0));
		}
		
		if (signUpErros.email != null) {
			TextView email = (TextView) findViewById(R.id.signup_email_message);
			email.setVisibility(View.VISIBLE);
			email.setText(getResources().getString(R.string.signup_email_error)
					+" "+signUpErros.email.get(0));
		}
		
		if (signUpErros.birthdate != null) {
			TextView birthday = (TextView) findViewById(R.id.signup_birthday_message);
			birthday.setVisibility(View.VISIBLE);
			birthday.setText(getResources().getString(R.string.signup_birthday_error)
					+" "+signUpErros.birthdate.get(0));
		}
		
		if (signUpErros.gender != null) {
			TextView gender = (TextView) findViewById(R.id.signup_gender_message);
			gender.setVisibility(View.VISIBLE);
			gender.setText(getResources().getString(R.string.signup_gender_error)
					+" "+signUpErros.gender.get(0));
		}
	}
	
	
	/************************************************************
	 * HTTP Request methods
	 ************************************************************/
	@Override
	public Loader<ResponseBundle> onCreateLoader(int loaderID, Bundle args) {
		
		switch (loaderID) {
			case SEND_DATA:
				if(!InternetStatusChecker.checkInternetStauts(this))
					return null;
				
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
						userData.gender = SignUp.MALE;
						break;
						
					case R.id.signup_gender_female:
						userData.gender = SignUp.FEMALE;
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
				
				SingUpPostFormHTTPLoader singUpPostFormHTTPLoader = 
					new SingUpPostFormHTTPLoader(this, paramsBundle);
				singUpPostFormHTTPLoader.forceLoad();
				return singUpPostFormHTTPLoader;
			default:
				return null;
		}
	}

	public void onLoadFinished(Loader<ResponseBundle> loader, ResponseBundle responseBundle) {
		switch (loader.getId()) {
		case SEND_DATA:
			Response response = responseBundle.getResponse();
			if(response != null) {
				if (response.isSuccessful()) {
					findViewById(R.id.signup_send_progressbar).setVisibility(View.GONE);
					TextView successText = (TextView) findViewById(R.id.signup_send_text);
					successText.setVisibility(View.VISIBLE);
					this.lockInputs();
					
				} else {
					Button sendButton = (Button) findViewById(R.id.signup_send_button);
					sendButton.setOnClickListener(this);
					sendButton.setVisibility(View.VISIBLE);
					findViewById(R.id.signup_send_progressbar).setVisibility(View.GONE);
					
					SignUpErrorsJsonModel signUpErrors = (SignUpErrorsJsonModel) responseBundle.getResponseJsonObject();
					if (signUpErrors != null) {
						this.setCorretions(signUpErrors);
					} else {
						ErrorMesages.errorRetrievingJsonData(this);
					}
				}
			} else {
				ErrorMesages.errorSendingHttpRequest(this);
				LogoutChecker.checkSessionOnResponse(this, responseBundle.getResponse());
				
				Button sendButton = (Button) findViewById(R.id.signup_send_button);
				sendButton.setOnClickListener(this);
				sendButton.setVisibility(View.VISIBLE);
				findViewById(R.id.signup_send_progressbar).setVisibility(View.GONE);
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
