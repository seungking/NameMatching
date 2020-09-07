package com.e.namematching;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e.namematching.model.functions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetAccount extends AppCompatActivity {

    private TextInputLayout layoutName,layoutLastname;
    private TextInputEditText txtName,txtLastname;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_account);

        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){

            Uri imgUri = data.getData();
            circleImageView.setImageURI(imgUri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void init() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = PreferenceManager.getDefaultSharedPreferences(this);
        layoutName = findViewById(R.id.txtLayoutNameUserInfo);
        txtName = findViewById(R.id.txtNameUserInfo);
        txtSelectPhoto = findViewById(R.id.txtSelectPhoto);
        btnContinue = findViewById(R.id.btnContinue);
        circleImageView = findViewById(R.id.imgUserInfo);

        if(userPref.getString("photo","").length()>1){
            circleImageView.setImageBitmap(new functions().StringToBitmap(userPref.getString("photo","")));
            txtName.setText(userPref.getString("name",""));
        }

        //갤러리에서 사진 가져옴
        txtSelectPhoto.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_ADD_PROFILE);
        });


        btnContinue.setOnClickListener(v->{
            if(validate()){
                saveUserInfo();
            }
        });
    }

    private boolean validate(){
        if (txtName.getText().toString().isEmpty()){
            layoutName.setErrorEnabled(true);
            layoutName.setError("사용할 이름을 입력해 주세요");
            return false;
        }
        return true;
    }


    private void saveUserInfo(){
        SharedPreferences.Editor editor = userPref.edit();
        if(bitmap!=null)editor.putString("photo",new functions().bitmapToString(bitmap));
        editor.putString("name",txtName.getText().toString().trim());
        editor.apply();
        if(getIntent().getIntExtra("from",0)==0){
            startActivity(new Intent(SetAccount.this,MainActivity.class));
        }
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }



    public Bitmap StringToBitmap(String str) {
        try {
            byte[] decode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
