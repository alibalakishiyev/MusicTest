package com.ali.musictest.user;



import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ali.musictest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private EditText profileEmail,nname,nphone;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase Auth instance
        fAuth = FirebaseAuth.getInstance();


        // UI elementləri tapın
        profileEmail = findViewById(R.id.profileEmail);
        nname = findViewById(R.id.nname);
        nphone = findViewById(R.id.nphone);

        // İstifadəçi məlumatlarını göstər
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            nname.setText(user.getDisplayName());
            profileEmail.setText(user.getEmail()); // İstifadəçi emailini göstər
            nphone.setText(user.getPhoneNumber());

        }
    }
}