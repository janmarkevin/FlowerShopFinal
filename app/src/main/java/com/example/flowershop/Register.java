package com.example.flowershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button RegisterButton;
    private EditText Username, Password, FirstName, LastName, PhoneNumber, EmailAddress;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterButton = (Button) findViewById(R.id.join_button);
        FirstName = (EditText) findViewById(R.id.join_firstname);
        LastName = (EditText) findViewById(R.id.join_lastname);
        PhoneNumber = (EditText) findViewById(R.id.join_phonenumber);
        EmailAddress = (EditText) findViewById(R.id.join_email);
        Username = (EditText) findViewById(R.id.join_username);
        Password = (EditText) findViewById(R.id.join_password);
        loadingBar = new ProgressDialog(this);

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount()
    {
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String phone = PhoneNumber.getText().toString();
        String email = EmailAddress.getText().toString();
        String username = Username.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(firstname))
        {
            Toast.makeText(this, "Please input your First Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lastname))
        {
            Toast.makeText(this, "Please input your Last Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please input your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please input your Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please input your Username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please input your Password", Toast.LENGTH_SHORT).show();
        }
        else
            {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                
                ValidateUsername(firstname,lastname,phone,email,username,password);
            }
    }

    private void ValidateUsername(String firstname,String lastname, String phone, String email, String username, String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance("https://flowershop-edd99-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(username).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("FirstName", firstname);
                    userdataMap.put("LastName", lastname);
                    userdataMap.put("PhoneNumber", phone);
                    userdataMap.put("EmailAddress", email);
                    userdataMap.put("Username", username);
                    userdataMap.put("Password", password);

                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) 
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent (Register.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(Register.this, "Network Error, Please Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Register.this, "This " + username + " already exist, Please Try again.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent (Register.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}