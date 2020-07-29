package com.me.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.me.R;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Admin_Add_New_Product_Activity extends AppCompatActivity {


    private EditText pName, pDescription, pPrice;
    private Button upload_product;
    private ImageView imageView;
    private static final int GALLARY_PICK = 1;
    private Uri imageUri;
    String product_name, product_price, product_description;
    String randomKey, downloadImageURL,category;
    private StorageReference productImageRef;
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        category = getIntent().getStringExtra("category");
        pName = findViewById(R.id.product_name);
        pDescription = findViewById(R.id.product_description);
        pPrice = findViewById(R.id.product_price);
        imageView = (ImageView) findViewById(R.id.select_product_image);
        upload_product = (Button) findViewById(R.id.add_new_product);

        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });

        upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }

    private void validateProductData() {
        if(imageUri == null){
            Toast.makeText(Admin_Add_New_Product_Activity.this,"Enter Image..",Toast.LENGTH_SHORT).show();
        }
        product_price = pPrice.getText().toString();
        if (product_price.isEmpty()) {
            pPrice.setError("Enter valid price");
            pPrice.requestFocus();
            return;
        }
        product_name = pName.getText().toString();
        if (product_name.isEmpty()) {
            pName.setError("Enter valid price");
            pName.requestFocus();
            return;
        }
        product_description = pDescription.getText().toString();
        if(product_description.isEmpty()){
            pDescription.setError("Enter some description");
            pDescription.requestFocus();
            return;
        }
        storeProductInformation();
    }

    private void storeProductInformation() {
         randomKey = product_name + product_price;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + randomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Admin_Add_New_Product_Activity.this,"Error: "+message,Toast.LENGTH_SHORT);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Admin_Add_New_Product_Activity.this,"Image Uploaded Successfully.",Toast.LENGTH_SHORT);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageURL = task.getResult().toString();
                            Toast.makeText(Admin_Add_New_Product_Activity.this,"getting ImageUrl successfully...",Toast.LENGTH_SHORT);
                            saveProductDatatoDatabase();
                        }
                    }
                });
            }
        });
    }
    private void saveProductDatatoDatabase(){
        HashMap<String, Object> productdataMap = new HashMap<>();
        productdataMap.put("pid",randomKey);
        productdataMap.put("name",product_name);
        productdataMap.put("price",product_price);
        productdataMap.put("description",product_description);
        productdataMap.put("image",downloadImageURL);
        productdataMap.put("category",category);

        productRef.child(randomKey).updateChildren(productdataMap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Admin_Add_New_Product_Activity.this,"Product added...",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Admin_Add_New_Product_Activity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                        else{
                            String message = task.getException().toString();
                            Toast.makeText(Admin_Add_New_Product_Activity.this,message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openGallary() {
        Intent gallaryIntent = new Intent();
        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
        gallaryIntent.setType("image/*");
        startActivityForResult(gallaryIntent, GALLARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLARY_PICK && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}