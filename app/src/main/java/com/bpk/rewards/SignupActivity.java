package com.bpk.rewards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bpk.rewards.model.User;
import com.bpk.rewards.utility.Constants;
import com.bpk.rewards.utility.PrefUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by bkini on 6/27/17.
 */

public class SignupActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    Button btnSignUp;
    TextView txtLogin;
    TextView txtName;
    TextView txtEmail;
    TextView txtPassword;
    TextView txtRePassword;
    private FirebaseAuth mAuth;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp =(Button) findViewById(R.id.signup_button);
        txtLogin = (TextView) findViewById(R.id.link_login);
        txtName = (TextView) findViewById(R.id.input_name);
        txtEmail = (TextView) findViewById(R.id.input_email);

        txtPassword = (TextView) findViewById(R.id.input_password);
        txtRePassword = (TextView) findViewById(R.id.input_reEnterPassword);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //     Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                //      startActivity(intent);
                finish();
                //    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validate()) {
                    Log.d("TAG","KKK VAlidation failed");
                    return;
                }
                createAccount(txtEmail.getText().toString().trim(),txtPassword.getText().toString());
                Log.d("TAG","KKK Signup New User");
            }
        });
    }


    public boolean validate() {
        boolean valid = true;

        String name = txtName.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String reEnterPassword = txtRePassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            txtName.setError("at least 3 characters");
            valid = false;
        } else {
            txtName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("enter a valid email address");
            valid = false;
        } else {
            txtEmail.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            txtPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            txtPassword.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            txtRePassword.setError("Password Do not match");
            valid = false;
        } else {
            txtRePassword.setError(null);
        }

        return valid;
    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        showProgressDialog();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(User.FIREBASE_USER_ROOT);
        mAuth = FirebaseAuth.getInstance();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            sendEmailVerification();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:exception "+task.getException().getClass());

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(SignupActivity.this,
                                        "User with this email already exist.", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "User with this email already exist");


                            }
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()

                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.txtForgotPassword).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(SignupActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {

        hideProgressDialog();
        if (user != null) {
            Log.d(TAG, "Login Success " + user.getDisplayName());
            PrefUtils.saveToPrefs(SignupActivity.this, Constants.USER_ID, user.getUid());
            User u = new User();
            u.setUserId(user.getUid());
            u.setEmail(user.getEmail());
            u.setName(user.getDisplayName());
            u.setPhotoUrl(user.getPhotoUrl().toString());
            newUser(u);
            user.getDisplayName();
            Log.d(TAG, "Login SuccessUser  " + user);


        } else {
            PrefUtils.saveToPrefs(SignupActivity.this, Constants.USER_ID, null);
            Log.d(TAG, "Error in Login");
        }
    }

    private void newUser(final User user){
        Log.d(TAG, "KHUSHI111 new User ");
        mFirebaseDatabase.child(user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "KHUSHI111 snapshot " + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    Log.d(TAG, "KHUSHI111 USER Exists");


                } else {
                    Log.d(TAG, "KHUSHI111 new USER create reward");

                    mFirebaseDatabase.child(user.getUserId()).setValue(user);

                }
                finish();
                startActivity(new Intent(SignupActivity.this,MainActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
