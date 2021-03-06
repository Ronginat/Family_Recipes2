package com.ronginat.family_recipes.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.TaskStackBuilder;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.ronginat.family_recipes.R;
import com.ronginat.family_recipes.layout.cognito.AppHelper;
import com.ronginat.family_recipes.ui.baseclasses.MyBaseActivity;
import com.ronginat.family_recipes.utils.Constants;
import com.ronginat.family_recipes.utils.logic.CrashLogger;
import com.ronginat.family_recipes.utils.logic.SharedPreferencesHandler;

import java.util.Locale;
import java.util.Map;

@SuppressLint("SetTextI18n")
public class LoginActivity extends MyBaseActivity {
    private final String TAG = getClass().getSimpleName();

    private ProgressBar waitDialog;

    // Screen fields
    private EditText inUsername;
    private EditText inPassword;

    //Continuations
    //private MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation;
    private ForgotPasswordContinuation forgotPasswordContinuation;
    private NewPasswordContinuation newPasswordContinuation;
    //private ChooseMfaContinuation mfaOptionsContinuation;

    // User Details
    private String username;
    private String password;

    // User attribute name
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initApp();
        findCurrent();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                // Register user
                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    if (name != null && !name.isEmpty()) {
                        inUsername.setText(name);
                        inPassword.setText("");
                        inPassword.requestFocus();
                    }
                    String userPasswd = data.getStringExtra("password");
                    if (userPasswd != null && !userPasswd.isEmpty()) {
                        inPassword.setText(userPasswd);
                    }
                    if (name != null && !name.isEmpty() && userPasswd != null && !userPasswd.isEmpty()) {
                        // We have the user details, so sign in!
                        username = name;
                        password = userPasswd;
                        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
                    }
                }
                break;
            case 2:
                // Confirm register user
                if(resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    if (name != null && !name.isEmpty()) {
                        inUsername.setText(name);
                        inPassword.setText("");
                        inPassword.requestFocus();
                    }
                }
                break;
            case 3:
                // Forgot password
                if(resultCode == RESULT_OK) {
                    String newPass = data.getStringExtra("newPass");
                    String code = data.getStringExtra("code");
                    if (newPass != null && code != null) {
                        if (!newPass.isEmpty() && !code.isEmpty()) {
                            showWaitDialog("Setting new password...");
                            forgotPasswordContinuation.setPassword(newPass);
                            forgotPasswordContinuation.setVerificationCode(code);
                            forgotPasswordContinuation.continueTask();
                        }
                    }
                }
                break;
            case 4:
                // User
                if(resultCode == RESULT_OK) {
                    clearInput();
                    String name = data.getStringExtra("TODO");
                    if(name != null) {
                        if (!name.isEmpty()) {
                            //name.equals("exit");
                            onBackPressed();
                        }
                    }
                }
                break;
            case 6:
                //New password
                closeWaitDialog();
                boolean continueSignIn = false;
                if (resultCode == RESULT_OK) {
                   continueSignIn = data.getBooleanExtra("continueSignIn", false);
                }
                if (continueSignIn) {
                    continueWithFirstTimeSignIn();
                }
                break;
        }
    }

    // App methods
    /*// Register user - start process
    public void signUp(View view) {
        signUpNewUser();
    }*/

    // Login if a user is already present
    public void logIn(View view) {
        signInUser();
    }

    // Forgot password processing
    public void forgotPassword(View view) {
        forgotpasswordUser();
    }


    // Private methods

    private void signInUser() {
        username = inUsername.getText().toString();
        if(inPassword.getText() == null || username.length() < 1) {
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText(new StringBuilder(inUsername.getHint()).append((" cannot be empty")));
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        AppHelper.setUser(username);

        showWaitDialog("Signing in...");
        AppHelper.getPool().getUser(username).getSessionInBackground(authenticationHandler);
    }

    private void forgotpasswordUser() {
        username = inUsername.getText().toString();
        if(inPassword.getText() == null || username.length() < 1) {
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText(new StringBuilder(inUsername.getHint()).append(" cannot be empty"));
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));
            return;
        }

        showWaitDialog("");
        AppHelper.getPool().getUser(username).forgotPasswordInBackground(forgotPasswordHandler);
    }

    private void getForgotPasswordCode(ForgotPasswordContinuation forgotPasswordContinuation) {
        this.forgotPasswordContinuation = forgotPasswordContinuation;
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra("destination",forgotPasswordContinuation.getParameters().getDestination());
        intent.putExtra("deliveryMed", forgotPasswordContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(intent, 3);
    }

    /*private void mfaAuth(MultiFactorAuthenticationContinuation continuation) {
        multiFactorAuthenticationContinuation = continuation;
        Intent mfaActivity = new Intent(this, MFAActivity.class);
        mfaActivity.putExtra("mode", multiFactorAuthenticationContinuation.getParameters().getDeliveryMedium());
        startActivityForResult(mfaActivity, 5);
    }*/

    private void firstTimeSignIn() {
        Intent newPasswordActivity = new Intent(this, NewPassword.class);
        startActivityForResult(newPasswordActivity, 6);
    }

    /*private void selectMfaToSignIn(List<String> options, Map<String, String> parameters) {
        Intent chooseMfaActivity = new Intent(this, ChooseMFA.class);
        AppHelper.setMfaOptionsForDisplay(options, parameters);
        startActivityForResult(chooseMfaActivity, 7);
    }

    private void conitnueWithSelectedMfa(String option) {
        // mfaOptionsContinuation.setChallengeResponse("ANSWER", option);
        mfaOptionsContinuation.setMfaOption(option);
        mfaOptionsContinuation.continueTask();
    }*/

    private void continueWithFirstTimeSignIn() {
        newPasswordContinuation.setPassword(AppHelper.getPasswordForFirstTimeLogin());
        Map<String, String> newAttributes = AppHelper.getUserAttributesForFirstTimeLogin();
        if (newAttributes != null) {
            for(Map.Entry<String, String> attr: newAttributes.entrySet()) {
                //Log.d(TAG, String.format(" -- Adding attribute: %s, %s", attr.getKey(), attr.getValue()));
                newPasswordContinuation.setUserAttribute(attr.getKey(), attr.getValue());
                if (attr.getKey().equals(getString(R.string.preference_key_preferred_name))) {
                    name = attr.getValue();
                }
            }
        }
        try {
            newPasswordContinuation.continueTask();
        } catch (Exception e) {
            closeWaitDialog();
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inPassword.setBackground(getDrawable(R.drawable.text_border_error));

            label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }
    }

/*    private void confirmUser() {
        Intent confirmActivity = new Intent(this, SignUpConfirm.class);
        confirmActivity.putExtra("source","main");
        startActivityForResult(confirmActivity, 2);

    }*/

    private void launchMain() {
        if (getIntent() != null) {
            Intent receivedIntent = getIntent();
            String action = receivedIntent.getStringExtra(Constants.SPLASH_ACTIVITY_CODE);
            if (action != null) {
                switch (action) {
                    case Constants.SPLASH_ACTIVITY_CODE_RECIPE:
                        // Open a specific recipe from deep link. Open as a single activity, without back stack
                        if (receivedIntent.getExtras() != null) {
                            Intent recipeIntent = new Intent(this, RecipeActivity.class);
                            recipeIntent.putExtras(receivedIntent);
                            recipeIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                            startActivity(recipeIntent);
                            finish();
                        }
                        break;
                    case Constants.SPLASH_ACTIVITY_CODE_POST:
                        // Post recipe shortcut, open with MainActivity in back stack
                        TaskStackBuilder.create(this)
                                .addNextIntent(new Intent(this, MainActivity.class)
                                        .putExtra(Constants.MAIN_ACTIVITY_FIRST_FRAGMENT, Constants.MAIN_ACTIVITY_FRAGMENT_ALL))
                                .addNextIntent(new Intent(this, PostRecipeActivity.class))
                                .startActivities();
                        finish();
                        break;
                    case Constants.SPLASH_ACTIVITY_CODE_MAIN:
                        // open main activity but not with the default fragment
                        Intent mainIntent = new Intent(this, MainActivity.class);
                        mainIntent.putExtra(Constants.MAIN_ACTIVITY_FIRST_FRAGMENT, receivedIntent.getSerializableExtra(Constants.MAIN_ACTIVITY_FIRST_FRAGMENT));
                        mainIntent.putExtra("name", username);
                        mainIntent.putExtra("from_login", true);
                        startActivity(mainIntent);
                        finish();
                        break;
                }
            } else {
                loginFromMainFlow();
            }
        } else {
            // regular open of the app from launcher
            loginFromMainFlow();
        }
    }

    /**
     * triggered when user launch the application from the app icon in the app drawer
     */
    private void loginFromMainFlow() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.MAIN_ACTIVITY_FIRST_FRAGMENT, Constants.MAIN_ACTIVITY_FRAGMENT_ALL);
        intent.putExtra("name", username);
        intent.putExtra("from_login", true);
        startActivity(intent);
        finish();
    }

    private void findCurrent() {
        CognitoUser user = AppHelper.getPool().getCurrentUser();
        username = user.getUserId();
        if(username != null) {
            AppHelper.setUser(username);
            inUsername.setText(user.getUserId());
            user.getSessionInBackground(authenticationHandler);
        }
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        if(username != null) {
            this.username = username;
            AppHelper.setUser(username);
        }
        //if(this.password == null) {
            inUsername.setText(username);
            password = inPassword.getText().toString();
            if(inPassword.getText() == null || password.length() < 1) {
                TextView label = findViewById(R.id.textViewUserPasswordMessage);
                label.setText(inPassword.getHint().toString().concat(" enter password"));
                inPassword.setBackground(getDrawable(R.drawable.text_border_error));
                return;
            }
        //}
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(this.username, password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    // initialize app
    private void initApp() {
        inUsername = findViewById(R.id.editTextUserId);
        inUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserIdLabel);
                    label.setText(R.string.Username);
                    inUsername.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = findViewById(R.id.textViewUserIdMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserIdLabel);
                    label.setText("");
                }
            }
        });

        inPassword = findViewById(R.id.editTextUserPassword);
        inPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserPasswordLabel);
                    label.setText(R.string.Password);
                    inPassword.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = findViewById(R.id.textViewUserPasswordMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    TextView label = findViewById(R.id.textViewUserPasswordLabel);
                    label.setText("");
                }
            }
        });
    }


    // Callbacks
    ForgotPasswordHandler forgotPasswordHandler = new ForgotPasswordHandler() {
        @Override
        public void onSuccess() {
            closeWaitDialog();
            showDialogMessage("Password successfully changed!","");
            inPassword.setText("");
            inPassword.requestFocus();
        }

        @Override
        public void getResetCode(ForgotPasswordContinuation forgotPasswordContinuation) {
            closeWaitDialog();
            getForgotPasswordCode(forgotPasswordContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            showDialogMessage("Forgot password failed",AppHelper.formatException(e));
        }
    };

    //
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession cognitoUserSession, CognitoDevice device) {
            CrashLogger.d(TAG, " -- Auth Success");
            AppHelper.setCurrSession(cognitoUserSession);
            AppHelper.setUserDetailsBackground(getApplicationContext());
            AppHelper.newDevice(device);
            //Log.e(TAG, "IDToken: " + cognitoUserSession.getIdToken().getJWTToken());
            CrashLogger.e(TAG, "AccessToken: " + cognitoUserSession.getAccessToken().getJWTToken());

            AppHelper.setIdentityProvider(getApplicationContext(), cognitoUserSession);

            closeWaitDialog();
            writeCredentialsToSharedPref();

            launchMain();
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String username) {
            //closeWaitDialog();
            Locale.setDefault(Locale.US);
            getUserAuthentication(authenticationContinuation, username);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            closeWaitDialog();
            //mfaAuth(multiFactorAuthenticationContinuation);
        }

        @Override
        public void onFailure(Exception e) {
            closeWaitDialog();
            TextView label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inPassword.setBackground(getDrawable(R.drawable.text_border_error));

            label = findViewById(R.id.textViewUserIdMessage);
            label.setText("Sign-in failed");
            inUsername.setBackground(getDrawable(R.drawable.text_border_error));

            showDialogMessage("Sign-in failed", AppHelper.formatException(e));
        }

        /**
         * For Custom authentication challenge, implement your logic to present challenge to the
         * user and pass the user's responses to the continuation.
         */
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            if ("NEW_PASSWORD_REQUIRED".equals(continuation.getChallengeName())) {
                // This is the first sign-in attempt for an admin created user
                newPasswordContinuation = (NewPasswordContinuation) continuation;
                AppHelper.setUserAttributeForDisplayFirstLogIn(newPasswordContinuation.getCurrentUserAttributes(),
                        newPasswordContinuation.getRequiredAttributes());
                closeWaitDialog();
                firstTimeSignIn();
            } else if ("SELECT_MFA_TYPE".equals(continuation.getChallengeName())) {
                closeWaitDialog();
                //mfaOptionsContinuation = (ChooseMfaContinuation) continuation;
                //List<String> mfaOptions = mfaOptionsContinuation.getMfaOptions();
                //selectMfaToSignIn(mfaOptions, continuation.getParameters());
            }
        }
    };

    private void clearInput() {
        if(inUsername == null) {
            inUsername = findViewById(R.id.editTextUserId);
        }

        if(inPassword == null) {
            inPassword = findViewById(R.id.editTextUserPassword);
        }

        inUsername.setText("");
        inUsername.requestFocus();
        inUsername.setBackground(getDrawable(R.drawable.text_border_selector));
        inPassword.setText("");
        inPassword.setBackground(getDrawable(R.drawable.text_border_selector));
    }

    private void showWaitDialog(String message) {
        //closeWaitDialog();
        //waitDialog.setTitle(message);
        if (waitDialog == null) {
            waitDialog = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            waitDialog.setIndeterminate(true);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            ((RelativeLayout) findViewById(R.id.activity_login_container)).addView(waitDialog, params);
        }
        waitDialog.setVisibility(View.VISIBLE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog userDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(body)
                .setNeutralButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .create();

        userDialog.show();
    }

    private void closeWaitDialog() {
        if (waitDialog != null)
            waitDialog.setVisibility(View.GONE);
    }

    private void writeCredentialsToSharedPref() {
        SharedPreferencesHandler.writeString(this, com.ronginat.family_recipes.layout.Constants.USERNAME, username);
        SharedPreferencesHandler.writeString(this, com.ronginat.family_recipes.layout.Constants.PASSWORD, password);
        if (name != null) {
            SharedPreferencesHandler.writeString(this, getString(R.string.preference_key_preferred_name), name);
            SharedPreferencesHandler.writeString(this, com.ronginat.family_recipes.layout.Constants.FIRESTORE_SAVE_NAME, name);
        }
        //SharedPreferencesHandler.writeBoolean(getApplication(), "rememberMe", true);
    }
}
