package com.lawrene.falcon.copycopy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AdminActivity extends AppCompatActivity {

    //---------------------All arrays-------------------//
    String[] allschools = {"University of Uyo", "University of Port Harcourt", "University of Lagos"};
    String[] allfaculties = {"Engineering", "Science", "Medicine", "Law"};
    String[] alldepartments = {};
    String[] alldepartmentsEng = {"Mechanical", "Electrical", "Computer", "Chemical"};
    String[] alldepartmentsSci = {"Physics", "Biochemistry", "Anatomy", "Maths and Stats"};
    String[] alllevels = {};
    String[] alllevelsEng = {"100", "200", "300", "400", "500"};
    String[] alllevelsSci = {"100", "200", "300", "400"};


    //    ---------------------Declaration of the views--------------------
    Spinner mSchoolSpinner;
    Spinner mFacultySpinner;
    Spinner mDepartmentSpinner;
    Spinner mLevelSpinner;
    Button mUploadBtn, mSelectBtn;
    ProgressDialog mProgressDialog;
    EditText mPostTitleEdt;
    ImageView mImageOne, mImageTwo, mImageThree, mImageFour, mImageFive, mImageSix;


    Toolbar mToolbar;
    Uri mUriOne;
    Uri mUriTwo;
    Uri mUriThree;
    Uri mUriFour;
    Uri mUriFive;
    Uri mUriSix;
    String mThumbDownloadURLOne;
    String mDownloadURLOne = "";
    String mDownloadURLTwo = "";
    String mDownloadURLThree = "";
    String mDownloadURLFour = "";
    String mDownloadURLFive = "";
    String mDownloadURLSix = "";


    DatabaseReference mPostDatabase;
    DatabaseReference mUserPostDatabase;
    DatabaseReference mNotificationsDatabase;
    DatabaseReference mUsersDatabase;
    StorageReference mFilesStorage;
    StorageReference mThumbFireStorage;

    String mSchool, mFaculty, mDepartment, mlevel;
    FirebaseAuth mFireAuth;
    String userWhoPosted = "";

    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //-------------------------Setting up  firebase---------------------
        mFireAuth = FirebaseAuth.getInstance();

        loadAllViews();
        loadOnClick();
        checkUser();

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools");
        mUserPostDatabase = FirebaseDatabase.getInstance().getReference().child("PostsByUsers");
        mNotificationsDatabase = FirebaseDatabase.getInstance().getReference().child("Notifications");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        Log.i("TAG", "The user who posted is" + userWhoPosted);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setInitialCropWindowPaddingRatio(0)
                    .start(this);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                if (mUriOne != null && mUriTwo == null) {
                    mUriTwo = result.getUri();
                    mImageTwo.setImageURI(mUriTwo);
                } else if (mUriTwo != null && mUriThree == null) {
                    mUriThree = result.getUri();
                    mImageThree.setImageURI(mUriThree);
                } else if (mUriThree != null && mUriFour == null) {
                    mUriFour = result.getUri();
                    mImageFour.setImageURI(mUriFour);
                } else if (mUriFour != null && mUriFive == null) {
                    mUriFive = result.getUri();
                    mImageFive.setImageURI(mUriFive);
                } else if (mUriFive != null && mUriSix == null) {
                    mUriSix = result.getUri();
                    mImageSix.setImageURI(mUriSix);
                } else {
                    mUriOne = result.getUri();
                    mImageOne.setImageURI(mUriOne);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void loadAllViews() {

        mToolbar = findViewById(R.id.admin_appBar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setTitle("Administrator");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setTitle("Upload Files");

        //-----------------------------Setting up the views-----------------------
        mSchoolSpinner = findViewById(R.id.adminschoolspinner);
        mFacultySpinner = findViewById(R.id.adminfacultyspinner);
        mDepartmentSpinner = findViewById(R.id.admindept_spinner);
        mLevelSpinner = findViewById(R.id.adminlevel_spinner);
        mUploadBtn = findViewById(R.id.adminPostBtn);
        mSelectBtn = findViewById(R.id.admin_select_btn);
        mPostTitleEdt = findViewById(R.id.admin_post_ttittle);

        mImageOne = findViewById(R.id.imageOne);
        mImageTwo = findViewById(R.id.imageTwo);
        mImageThree = findViewById(R.id.imageThree);
        mImageFour = findViewById(R.id.imageFour);
        mImageFive = findViewById(R.id.imageFive);
        mImageSix = findViewById(R.id.imageSix);

        //---------------------Setting up the arrayadapter to use for the spinners------------------
        ArrayAdapter<String> school_array_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allschools);
        school_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSchoolSpinner.setAdapter(school_array_adapter);

        ArrayAdapter<String> faculty_array_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allfaculties);
        faculty_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFacultySpinner.setAdapter(faculty_array_adapter);

        ArrayAdapter<String> dept_array_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, alldepartments);
        dept_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDepartmentSpinner.setAdapter(dept_array_adapter);

        ArrayAdapter<String> level_array_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, alllevels);
        level_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLevelSpinner.setAdapter(level_array_adapter);
    }

    public void loadOnClick() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUriOne == null) {
                    Toast.makeText(AdminActivity.this, "Please select at least one image to continue", Toast.LENGTH_SHORT).show();
                } else {

                    mProgressDialog = new ProgressDialog(AdminActivity.this);
                    mProgressDialog.setTitle("Please Wait");
                    mProgressDialog.setMessage("Uploading Image");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();

                    putFullImage();
                }
            }
        });


        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image to Upload"), GALLERY_PICK);
            }
        });


        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSchool = allschools[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mFaculty = allfaculties[position];
                switch (mFaculty) {
                    case "Engineering":
                        alldepartments = alldepartmentsEng;
                        alllevels = alllevelsEng;

                        ArrayAdapter<String> eng_depts_adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_spinner_item, alldepartments);
                        eng_depts_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mDepartmentSpinner.setAdapter(eng_depts_adapter);

                        ArrayAdapter<String> eng_levels_adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_spinner_item, alllevels);
                        eng_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mLevelSpinner.setAdapter(eng_levels_adapter);
                        break;


                    case "Science":
                        alldepartments = alldepartmentsSci;
                        alllevels = alllevelsSci;

                        ArrayAdapter<String> science_adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_spinner_item, alldepartments);
                        science_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mDepartmentSpinner.setAdapter(science_adapter);

                        ArrayAdapter<String> sci_levels_adapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_spinner_item, alllevels);
                        sci_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mLevelSpinner.setAdapter(sci_levels_adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mDepartment = alldepartments[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mlevel = alllevels[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void putFullImage() {

        File thumb_filePath = new File(mUriOne.getPath());
        Bitmap thumb_bitmap = new Compressor(AdminActivity.this)
                .setMaxWidth(200)
                .setMaxHeight(200)
                .setQuality(75)
                .compressToBitmap(thumb_filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] thumb_byte = baos.toByteArray();


        final String postTitle = mPostTitleEdt.getText().toString();

        final String uidforimage = mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).push().getKey();

        mFilesStorage = FirebaseStorage.getInstance().getReference().child(mSchool)
                .child(mFaculty).child(mDepartment).child(mlevel)
                .child(uidforimage);

        mThumbFireStorage = FirebaseStorage.getInstance().getReference().child(mSchool)
                .child(mFaculty).child(mDepartment).child(mlevel)
                .child("thumbnails").child(uidforimage).child(uidforimage + ".jpg");

//                    -------------------------Uploading image one-----------------------------
        mFilesStorage.child(uidforimage + "1.jpg").putFile(mUriOne).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mDownloadURLOne = task.getResult().getDownloadUrl().toString();
                    UploadTask uploadTask = mThumbFireStorage.putBytes(thumb_byte);

                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                            if (thumb_task.isSuccessful()) {
                                mThumbDownloadURLOne = thumb_task.getResult().getDownloadUrl().toString();


//                                            -------------------------------Uploading image two---------------------------------
                                if (mUriTwo != null) {
                                    mFilesStorage.child(uidforimage + "2.jpg").putFile(mUriTwo).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                mDownloadURLTwo = task.getResult().getDownloadUrl().toString();


//                                                        -----------------------Uploading image three-----------------------------------
                                                if (mUriThree != null) {
                                                    mFilesStorage.child(uidforimage + "3.jpg").putFile(mUriThree).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                mDownloadURLThree = task.getResult().getDownloadUrl().toString();


//                                                                    -------------------------------Uploading image four-------------------------
                                                                if (mUriFour != null) {
                                                                    mFilesStorage.child(uidforimage + "4.jpg").putFile(mUriFour).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                mDownloadURLFour = task.getResult().getDownloadUrl().toString();


//                                                                                ----------------------------------Uplaoding image five-------------------------
                                                                                if (mUriFive != null) {
                                                                                    mFilesStorage.child(uidforimage + "5.jpg").putFile(mUriFive).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                mDownloadURLFive = task.getResult().getDownloadUrl().toString();


//                                                                                            --------------------------------Uploading image six-------------------
                                                                                                if (mUriSix != null) {
                                                                                                    mFilesStorage.child(uidforimage + "6.jpg").putFile(mUriSix).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                                            if (task.isSuccessful()) {
                                                                                                                mDownloadURLSix = task.getResult().getDownloadUrl().toString();

                                                                                                                HashMap<String, Object> newpostmap = new HashMap<>();
                                                                                                                newpostmap.put("title", postTitle);
                                                                                                                newpostmap.put("date", ServerValue.TIMESTAMP);
                                                                                                                newpostmap.put("url1", mDownloadURLOne);
                                                                                                                newpostmap.put("url2", mDownloadURLTwo);
                                                                                                                newpostmap.put("url3", mDownloadURLThree);
                                                                                                                newpostmap.put("url4", mDownloadURLFour);
                                                                                                                newpostmap.put("url5", mDownloadURLFive);
                                                                                                                newpostmap.put("url6", mDownloadURLSix);
                                                                                                                newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                                                                                                newpostmap.put("posted_by", userWhoPosted);
                                                                                                                newpostmap.put("image_uid", uidforimage);

                                                                                                                if (userWhoPosted.equals("admin")) {
                                                                                                                    mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                            sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                                                                                            mProgressDialog.dismiss();
                                                                                                                            Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                                } else if (!userWhoPosted.equals("admin")) {
                                                                                                                    newpostmap.put("school", mSchool);
                                                                                                                    newpostmap.put("faculty", mFaculty);
                                                                                                                    newpostmap.put("department", mDepartment);
                                                                                                                    newpostmap.put("level", mlevel);

                                                                                                                    mUserPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                            mProgressDialog.dismiss();
                                                                                                                            Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    });
                                                                                                                }


                                                                                                            }
                                                                                                        }
                                                                                                    });
                                                                                                } else {

                                                                                                    HashMap<String, Object> newpostmap = new HashMap<>();
                                                                                                    newpostmap.put("title", postTitle);
                                                                                                    newpostmap.put("date", ServerValue.TIMESTAMP);
                                                                                                    newpostmap.put("url1", mDownloadURLOne);
                                                                                                    newpostmap.put("url2", mDownloadURLTwo);
                                                                                                    newpostmap.put("url3", mDownloadURLThree);
                                                                                                    newpostmap.put("url4", mDownloadURLFour);
                                                                                                    newpostmap.put("url5", mDownloadURLFive);
                                                                                                    newpostmap.put("url6", mDownloadURLSix);
                                                                                                    newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                                                                                    newpostmap.put("posted_by", userWhoPosted);
                                                                                                    newpostmap.put("image_uid", uidforimage);

                                                                                                    if (userWhoPosted.equals("admin")) {
                                                                                                        mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                                                                                mProgressDialog.dismiss();
                                                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        });
                                                                                                    } else if (!userWhoPosted.equals("admin")) {
                                                                                                        newpostmap.put("school", mSchool);
                                                                                                        newpostmap.put("faculty", mFaculty);
                                                                                                        newpostmap.put("department", mDepartment);
                                                                                                        newpostmap.put("level", mlevel);

                                                                                                        mUserPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                mProgressDialog.dismiss();
                                                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {

                                                                                    HashMap<String, Object> newpostmap = new HashMap<>();
                                                                                    newpostmap.put("title", postTitle);
                                                                                    newpostmap.put("date", ServerValue.TIMESTAMP);
                                                                                    newpostmap.put("url1", mDownloadURLOne);
                                                                                    newpostmap.put("url2", mDownloadURLTwo);
                                                                                    newpostmap.put("url3", mDownloadURLThree);
                                                                                    newpostmap.put("url4", mDownloadURLFour);
                                                                                    newpostmap.put("url5", mDownloadURLFive);
                                                                                    newpostmap.put("url6", mDownloadURLSix);
                                                                                    newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                                                                    newpostmap.put("posted_by", userWhoPosted);
                                                                                    newpostmap.put("image_uid", uidforimage);

                                                                                    if (userWhoPosted.equals("admin")) {
                                                                                        mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                                                                mProgressDialog.dismiss();
                                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    } else if (!userWhoPosted.equals("admin")) {
                                                                                        newpostmap.put("school", mSchool);
                                                                                        newpostmap.put("faculty", mFaculty);
                                                                                        newpostmap.put("department", mDepartment);
                                                                                        newpostmap.put("level", mlevel);
                                                                                        mUserPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                mProgressDialog.dismiss();
                                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                                } else {

                                                                    HashMap<String, Object> newpostmap = new HashMap<>();
                                                                    newpostmap.put("title", postTitle);
                                                                    newpostmap.put("date", ServerValue.TIMESTAMP);
                                                                    newpostmap.put("url1", mDownloadURLOne);
                                                                    newpostmap.put("url2", mDownloadURLTwo);
                                                                    newpostmap.put("url3", mDownloadURLThree);
                                                                    newpostmap.put("url4", mDownloadURLFour);
                                                                    newpostmap.put("url5", mDownloadURLFive);
                                                                    newpostmap.put("url6", mDownloadURLSix);
                                                                    newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                                                    newpostmap.put("posted_by", userWhoPosted);
                                                                    newpostmap.put("image_uid", uidforimage);

                                                                    if (userWhoPosted.equals("admin")) {
                                                                        mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                                                mProgressDialog.dismiss();
                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    } else if (!userWhoPosted.equals("admin")) {
                                                                        newpostmap.put("school", mSchool);
                                                                        newpostmap.put("faculty", mFaculty);
                                                                        newpostmap.put("department", mDepartment);
                                                                        newpostmap.put("level", mlevel);
                                                                        mUserPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                mProgressDialog.dismiss();
                                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                } else {

                                                    HashMap<String, Object> newpostmap = new HashMap<>();
                                                    newpostmap.put("title", postTitle);
                                                    newpostmap.put("date", ServerValue.TIMESTAMP);
                                                    newpostmap.put("url1", mDownloadURLOne);
                                                    newpostmap.put("url2", mDownloadURLTwo);
                                                    newpostmap.put("url3", mDownloadURLThree);
                                                    newpostmap.put("url4", mDownloadURLFour);
                                                    newpostmap.put("url5", mDownloadURLFive);
                                                    newpostmap.put("url6", mDownloadURLSix);
                                                    newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                                    newpostmap.put("posted_by", userWhoPosted);
                                                    newpostmap.put("image_uid", uidforimage);

                                                    if (userWhoPosted.equals("admin")) {
                                                        mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                                mProgressDialog.dismiss();
                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else if (!userWhoPosted.equals("admin")) {
                                                        newpostmap.put("school", mSchool);
                                                        newpostmap.put("faculty", mFaculty);
                                                        newpostmap.put("department", mDepartment);
                                                        newpostmap.put("level", mlevel);

                                                        mUserPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mProgressDialog.dismiss();
                                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    });
                                } else {

                                    HashMap<String, Object> newpostmap = new HashMap<>();
                                    newpostmap.put("title", postTitle);
                                    newpostmap.put("date", ServerValue.TIMESTAMP);
                                    newpostmap.put("url1", mDownloadURLOne);
                                    newpostmap.put("url2", mDownloadURLTwo);
                                    newpostmap.put("url3", mDownloadURLThree);
                                    newpostmap.put("url4", mDownloadURLFour);
                                    newpostmap.put("url5", mDownloadURLFive);
                                    newpostmap.put("url6", mDownloadURLSix);
                                    newpostmap.put("thumb_image", mThumbDownloadURLOne);
                                    newpostmap.put("posted_by", userWhoPosted);
                                    newpostmap.put("image_uid", uidforimage);

                                    if (userWhoPosted.equals("admin")) {
                                        mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendRequiredUsersNotifications(postTitle, mSchool, mFaculty, mDepartment, mlevel);
                                                mProgressDialog.dismiss();
                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else if (!userWhoPosted.equals("admin")) {
                                        newpostmap.put("school", mSchool);
                                        newpostmap.put("faculty", mFaculty);
                                        newpostmap.put("department", mDepartment);
                                        newpostmap.put("level", mlevel);
                                        mUserPostDatabase.child(uidforimage).setValue(newpostmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    });

                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(AdminActivity.this, "Could not Upload Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendRequiredUsersNotifications(final String title, final String userSchool, final String userFaculty, final String userDepartment, final String userLevel) {

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String key = ds.getKey();
                    String school = ds.child("school").getValue().toString();
                    String faculty = ds.child("faculty").getValue().toString();
                    String department = ds.child("department").getValue().toString();
                    String level = ds.child("level").getValue().toString();

                    Toast.makeText(AdminActivity.this, key + school + faculty + department + level, Toast.LENGTH_SHORT).show();


                    HashMap<String, Object> notificationHashmap = new HashMap<>();
                    notificationHashmap.put("title", title);
                    notificationHashmap.put("message", "Click here to see");
                    notificationHashmap.put("time", ServerValue.TIMESTAMP);


                    if (school.equals(userSchool) && faculty.equals(userFaculty) && department.equals(userDepartment) && level.equals(userLevel)) {
                        mNotificationsDatabase.child(key).push().setValue(notificationHashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminActivity.this, "Notification sent to required users", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkUser() {
        userWhoPosted = getIntent().getStringExtra("user_id");
        if (userWhoPosted == null) {
            userWhoPosted = "admin";
        }
    }
}
