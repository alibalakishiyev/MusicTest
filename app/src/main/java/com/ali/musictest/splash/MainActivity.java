package com.ali.musictest.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.musictest.R;
import com.ali.musictest.autorized.Login;
import com.ali.musictest.autorized.Register;
import com.ali.musictest.service.SinaqQuest;
import com.ali.musictest.user.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button startQuizButton;
    private ImageView userIcon;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase Auth instance
        fAuth = FirebaseAuth.getInstance();

        // UI elementləri tapın
        startQuizButton = findViewById(R.id.startQuizButton);
        userIcon = findViewById(R.id.userIcon);

        // **İstifadəçi yoxlanışı (anonim və ya daxil olunmuş)**
        if (fAuth.getCurrentUser() != null) {
            // İstifadəçi daxil olubsa, "Qeydiyyat" düyməsini gizlət
            userIcon.setVisibility(View.VISIBLE); // Profil ikonu görünsün
        } else {
            // Anonim istifadəçidirsə
            userIcon.setVisibility(View.VISIBLE); // Profil ikonu gizlənsin
        }

        // Quizə Başla Düyməsi
        startQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SinaqQuest.class));
            }
        });


        // İstifadəçi İkonu
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PopupMenu yarat
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, userIcon);
                popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());

                // **Anonim istifadəçi yoxlanışı**
                if (fAuth.getCurrentUser() == null) {
                    // Anonim istifadəçidirsə, yalnız Login və Sign Up göstər
                    popupMenu.getMenu().findItem(R.id.menu_profile).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_logout).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_login).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.menu_signup).setVisible(true);
                } else {
                    // İstifadəçi daxil olubsa, yalnız Profil və Logout göstər
                    popupMenu.getMenu().findItem(R.id.menu_profile).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.menu_logout).setVisible(true);
                    popupMenu.getMenu().findItem(R.id.menu_login).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.menu_signup).setVisible(false);
                }

                // PopupMenu itemlərinə klik hadisəsi
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_profile) {
                            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            return true;
                        } else if (item.getItemId() == R.id.menu_logout) {
                            fAuth.signOut();
                            Toast.makeText(MainActivity.this, "Uğurla çıxış edildi!", Toast.LENGTH_SHORT).show();
                            userIcon.setVisibility(View.VISIBLE);
                            return true;
                        } else if (item.getItemId() == R.id.menu_login) {
                            startActivity(new Intent(MainActivity.this, Login.class));
                            return true;
                        } else if (item.getItemId() == R.id.menu_signup) {
                            startActivity(new Intent(MainActivity.this, Register.class));
                            return true;
                        }
                        return false;
                    }
                });

                // PopupMenu göstər
                popupMenu.show();
            }
        });
    }
}
