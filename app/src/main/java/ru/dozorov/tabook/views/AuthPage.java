package ru.dozorov.tabook.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ru.dozorov.tabook.R;

import static ru.dozorov.tabook.views.MainActivity.UserId;

public class AuthPage extends AppCompatActivity {

    private String TAG = "DebugLOG: ";
    private EditText etLogin;
    private EditText etPassword;
    private FirebaseAuth mAuth;
    private TextView debTV;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_page);
        etLogin = (EditText) findViewById(R.id.et_input_email);
        etPassword = (EditText) findViewById(R.id.et_input_password);
        debTV = (TextView) findViewById(R.id.tv_debug);
        mAuth = FirebaseAuth.getInstance();

        if (UserId != null){
            debTV.setText(UserId);
        }
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.b_login:
                signIn(etLogin.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.b_registration:
                createAccount(etLogin.getText().toString(), etPassword.getText().toString());
                break;
            case R.id.b_decline_login:
                finish();
                break;
            case (R.id.b_login_google):
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 1);
        }

    }

    private void createAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(AuthPage.this, "Registration success.", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthPage.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user){
        //взаимодействие с базой?
        //вызов обновления RV отсюда?
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(AuthPage.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            String info = "Email: " + user.getEmail() + " Display name: " + user.getDisplayName();
                            ((TextView)findViewById(R.id.tv_debug)).setText(info);
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthPage.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void getCurrentUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            UserId = account.getId();
            debTV.setText(UserId);
            setResult(RESULT_OK, new Intent().putExtra("ID", UserId));
            finish();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }
}
