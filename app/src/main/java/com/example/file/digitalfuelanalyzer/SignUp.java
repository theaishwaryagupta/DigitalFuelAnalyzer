package com.example.file.digitalfuelanalyzer;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatDelegate;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.FirebaseDatabase;

        import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;

     EditText dob;
     RadioGroup radioGroup;
     Boolean radioGroupflag=false;//for checking radio button is clicked or not will be set in on checkedchange listner.
     RadioButton male,female,transender;
     String gender;


     private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhone = findViewById(R.id.edit_text_phone);

        radioGroup=findViewById(R.id.genderrgfillform);
        dob=findViewById(R.id.dateofbirth_fillform);
        male=findViewById(R.id.malerbfillform);
        female=findViewById(R.id.femalerbfillform);
        transender=findViewById(R.id.transgenderrbfillform);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                radioGroupflag=true;

                switch (radioGroup.getCheckedRadioButtonId())
                {
                    case R.id.malerbfillform:
                    {
                        gender="Male";
                        break;
                    }
                    case R.id.femalerbfillform:
                    {
                        gender="Female";
                        break;
                    }
                    case R.id.transgenderrbfillform:
                    {
                        gender="Transgender";
                        break;
                    }
                }

            }
        });




        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_register).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String gen=gender.trim();
        final String dateofbirth = dob.getText().toString().trim();


        if (name.isEmpty()) {
            Toast.makeText(this, "This Field can't be left empty", Toast.LENGTH_SHORT).show();
            editTextName.setError("error");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "This Field can't be left empty", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("empty mail");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            editTextEmail.setError("invalid input mail");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "This Field can't be left empty", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("password is empty");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "length too short", Toast.LENGTH_SHORT).show();
            editTextPassword.setError("length too short");
            editTextPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "This Field can't be left empty", Toast.LENGTH_SHORT).show();
            editTextPhone.setError("phone field is empty");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            editTextPhone.setError("invalid ph no.");
            editTextPhone.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    gen,
                                    dateofbirth
                            );
                            FirebaseDatabase.getInstance().getReference("Info").child("fuel").setValue("0");
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignUp.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                registerUser();
                break;
        }
    }
}

