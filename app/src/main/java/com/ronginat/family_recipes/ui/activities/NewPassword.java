/*
 * Copyright 2013-2017 Amazon.com,
 * Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the
 * License. A copy of the License is located at
 *
 *      http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, express or implied. See the License
 * for the specific language governing permissions and
 * limitations under the License.
 */

package com.ronginat.family_recipes.ui.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ronginat.family_recipes.R;
import com.ronginat.family_recipes.layout.cognito.AppHelper;
import com.ronginat.family_recipes.recycler.adapters.FirstTimeLoginAttributesDisplayAdapter;

public class NewPassword extends AppCompatActivity {
    //private String TAG = "NewPassword";
    private EditText newPassword;

    @SuppressWarnings("FieldCanBeLocal")
    private Button continueSignIn;
    private AlertDialog userDialog;
    //private ProgressDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar_NewPassword);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> exit(false));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView main_title = findViewById(R.id.newpassword_toolbar_title);
        main_title.setText(getTitle());

        init();
    }

    @Override
    public void onBackPressed() {
        exit(false);
    }

    private void init() {
        newPassword = findViewById(R.id.editTextNewPassPass);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewNewPassPassLabel);
                    label.setText(newPassword.getHint());
                    newPassword.setBackground(getDrawable(R.drawable.text_border_selector));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = findViewById(R.id.textViewNewPassPassMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = findViewById(R.id.textViewNewPassPassLabel);
                    label.setText("");
                }
            }
        });

        continueSignIn = findViewById(R.id.buttonNewPass);
        continueSignIn.setOnClickListener(v -> {
            String newUserPassword = null;
            if (newPassword.getText() != null)
                newUserPassword = newPassword.getText().toString();
            if (newUserPassword != null) {
                AppHelper.setPasswordForFirstTimeLogin(newUserPassword);
                if (checkAttributes()) {
                    exit(true);
                } else
                    showDialogMessage("Error", "Enter all required attributed");
            } else
                showDialogMessage("Error", "Enter all required attributed");
        });
        refreshItemsDisplayed();
    }

    private void refreshItemsDisplayed() {
        final FirstTimeLoginAttributesDisplayAdapter attributesAdapter =
                new FirstTimeLoginAttributesDisplayAdapter(getApplicationContext());
        final ListView displayListView;
        displayListView = findViewById(R.id.listViewCurrentUserDetails);
        displayListView.setAdapter(attributesAdapter);
        displayListView.setOnItemClickListener((parent, view, position, id) -> {
            TextView data = view.findViewById(R.id.editTextUserDetailInput);
            String attributeType = data.getHint().toString();
            String attributeValue = data.getText().toString();
            showAttributeDetail(attributeType, attributeValue);
        });
    }

    private void showAttributeDetail(final String attributeType, final String attributeValue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(attributeType);
        final EditText input = new EditText(NewPassword.this);
        input.setText(attributeValue);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        input.requestFocus();
        builder.setView(input);

        builder.setNeutralButton("OK", (dialog, which) -> {
            try {
                String newValue = input.getText().toString();
                if (!newValue.equals(attributeValue)) {
                    AppHelper.setUserAttributeForFirstTimeLogin(attributeType, newValue);
                    refreshItemsDisplayed();
                }
                userDialog.dismiss();
            } catch (Exception e) {
                // Log failure
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private boolean checkAttributes() {
        // Check if all required attributes have values
        return AppHelper.isUserAttributeForFirstTimeLoginContainsAttribute(getString(R.string.preference_key_preferred_name));
        //return true;
    }

    @SuppressWarnings("SameParameterValue")
    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", (dialog, which) -> {
            try {
                userDialog.dismiss();
                /*if (exit) {
                    exit(false);
                }*/
            } catch (Exception e) {
                exit(false);
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void exit(Boolean continueWithSignIn) {
        Intent intent = new Intent();
        intent.putExtra("continueSignIn", continueWithSignIn);
        setResult(RESULT_OK, intent);
        finish();
    }
}
