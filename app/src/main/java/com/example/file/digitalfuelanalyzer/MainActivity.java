package com.example.file.digitalfuelanalyzer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.support.v7.app.AppCompatDelegate.MODE_NIGHT_YES;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText cfa,cfp,cfc;
    String info="0.0";
    double FuelPrice=71;
    double var=1.2;
    double x=0.0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        // adding firebase authentication
        auth = FirebaseAuth.getInstance();

        // connecting with UI
        cfa = (EditText) this.findViewById(R.id.current_amount);
        cfp = (EditText) this.findViewById(R.id.current_price);
        cfc = (EditText) this.findViewById(R.id.current_cost);

        //disabling fields
        cfa.setEnabled(false);
        cfp.setEnabled(false);
        cfc.setEnabled(false);

        //checking if user has verified his/her email
        if (auth.getCurrentUser().isEmailVerified()) {
           // Toast.makeText(this, "Email Verified", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Please Verify Your email id", Toast.LENGTH_SHORT).show();
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref1 = database.getReference("Info").child("fuel");

        cfp.setText(""+FuelPrice);
        cfa.setText("1.0");
        cfc.setText(""+(FuelPrice));
        ValueEventListener Listener = new ValueEventListener() {
            @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String info = (String) ds.getValue();
                    var = Double.parseDouble(info);
                    cfa.setText(""+info);
                    cfc.setText(""+(var*FuelPrice));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to update value....", Toast.LENGTH_SHORT).show();
            }

        };
        ref1.addValueEventListener(Listener);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.lg) {
            FirebaseAuth.getInstance().signOut(); //End user session
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); //Go back to home page
            finish();
        }

        if (id == R.id.inf) {
            startActivity(new Intent(MainActivity.this, Info.class)); //Go back to home page

        }

        if (id == R.id.help) {
            startActivity(new Intent(MainActivity.this, help.class)); //Go back to home page

        }

        if (id == R.id.ver) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.dfaicon);
        builder.setTitle("Digital Fuel Analyzer");
        builder.setMessage("Do you want to EXIT??");
        builder.setCancelable(false);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
            }
}
