package com.sp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText email;
    private EditText password;
    private EditText passwordHint;
    private FirebaseAuth auth;
    private static final String TAG = "RegisterActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        setViews();
        setListeners();
    }

    private void setViews() {
        registerBtn = findViewById(R.id.activity_register_register_button);
        email = findViewById(R.id.activity_register_email_edit_text);
        password = findViewById(R.id.activity_register_password_edit_text);
        passwordHint = findViewById(R.id.activity_register_password_hint_edit_text);
    }

    private void setListeners() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String passwordHintString = passwordHint.getText().toString();

                if (emailString.isEmpty() || passwordString.isEmpty() ||
                        passwordHintString.isEmpty()
                ) {
                    displayToast("All fields must be answer, please try again");
                } else {
                    registerUser(emailString, passwordString);
                }
            }
        });
    }

    private void displayToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void registerUser(final String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e);
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            storePasswordHint();
                            displayToast("Register succeed");
                            goToNavigationActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            displayToast("Register failed, please try again");
                        }
                    }
                });
    }

    private void goToNavigationActivity() {
        Intent intent = new Intent(RegisterActivity.this, Aftino.class);
        startActivity(intent);
    }

    private void storePasswordHint() {
        // Create a new user with a first and last name
        Map<String, Object> hint = new HashMap<>();
        hint.put("hint", passwordHint.getText().toString());

// Add a new document with a generated ID
        db.collection("hints").document(email.getText().toString())
                .set(hint)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
