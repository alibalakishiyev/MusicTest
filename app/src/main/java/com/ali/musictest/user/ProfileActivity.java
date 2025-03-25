package com.ali.musictest.user;



import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.ali.musictest.R;
import com.ali.musictest.autorized.Login;
import com.ali.musictest.autorized.Register;
import com.ali.musictest.splash.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE=1023;
    private TextView email,nname,nphone;
    private ImageButton resendCode,resetPassword,changeProfileImg,backBtn;
    private ImageView verifydImg;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = findViewById(R.id.txtEmail);
        nname = findViewById(R.id.txtName);
        nphone = findViewById(R.id.txtPhone);
        resetPassword =findViewById(R.id.resetPas);
        backBtn = findViewById(R.id.backBtn);


        profileImage = findViewById(R.id.profilImage);
        changeProfileImg = findViewById(R.id.changeProfile);



        resendCode =findViewById(R.id.verfyEmailBtn);
        verifydImg =findViewById(R.id.checkMarkImage);

        // Firebase Auth instance
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userId = fAuth.getCurrentUser().getUid();
         user =fAuth.getCurrentUser();

        if (!user.isEmailVerified()){
            resendCode.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Verification Email Has been Sent! ", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"onFailure: Email not sent"+e.getMessage());
                        }
                    });
                }
            });
        }else {
            verifydImg.setImageResource(R.drawable.greenreceived24);
            resendCode.setVisibility(View.GONE);
        }

        DocumentReference documentReference =fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FirestoreError", "Error fetching document: " + error.getMessage());
                    return;
                }

                if (value != null && value.exists()) {
                    nphone.setText(value.getString("phone") != null ? value.getString("phone") : "No phone");
                    nname.setText(value.getString("fName") != null ? value.getString("fName") : "No name");
                    email.setText(value.getString("email") != null ? value.getString("email") : "No email");
                } else {
                    Log.e("FirestoreError", "Document does not exist");
                    Toast.makeText(ProfileActivity.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetPassword.setOnClickListener((v) -> {
            final EditText resetPassword = new EditText(v.getContext());
            final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());

            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter a new password with at least 6 characters.");
            passwordResetDialog.setView(resetPassword);

            passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newPassword = resetPassword.getText().toString();

                    if (newPassword.length() < 6) {
                        Toast.makeText(ProfileActivity.this, "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ProfileActivity.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, "Password Reset Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

            passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); // Dialoqu bağlayırıq
                }
            });

            passwordResetDialog.create().show();
        });

        changeProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent i = new Intent(view.getContext(), EditProfile.class);
                i.putExtra("name",nname.getText());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",nphone.getText().toString());


                startActivity(i);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }





    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}