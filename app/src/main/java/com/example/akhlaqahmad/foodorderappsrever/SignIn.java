package com.example.akhlaqahmad.foodorderappsrever;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.akhlaqahmad.foodorderappsrever.Common.Common;
import com.example.akhlaqahmad.foodorderappsrever.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText editPhone,editPassword;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText) findViewById(R.id.editPassword);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //init firebase
        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(editPhone.getText().toString(),editPassword.getText().toString());
            }
        });
    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please Wait...");
        mDialog.show();

        final String localPhone = phone;
        final String localPassword = password;

//        users.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mDialog.dismiss();
//                //Check If user exists in database
//
//                if(dataSnapshot.child(editPhone.getText().toString()).exists()){
//                    // get User Information
//                    User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
//                    user.setPhone(editPhone.getText().toString());
//
//                    if(user.getPassword().equals(editPassword.getText().toString())){
//                        Intent intent = new Intent(SignIn.this,Home.class);
//                        Common.currentUser = user;
//                        startActivity(intent);
//                    }else{
//                        Toast.makeText(SignIn.this,"Sign in failed",Toast.LENGTH_SHORT).show();
//                    }
//
//                }else{
//                    Toast.makeText(SignIn.this,"User Not Exist",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(localPhone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsAdmin()))
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //login OK
                            Intent home = new Intent(SignIn.this,Home.class);
                            Common.currentUser = user;
                            startActivity(home);

                        }else
                        {
                            Toast.makeText(SignIn.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(SignIn.this, "Please Login With Admin Account", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "User not exists in database...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
