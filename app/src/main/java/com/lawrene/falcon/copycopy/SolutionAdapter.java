//package com.lawrene.falcon.copycopy;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.List;
//
///**
// * Created by lawrene on 4/19/18.
// */
////
////public class SolutionAdapter extends RecyclerView.Adapter<MainViewHolder>{
////
//////    private List<Solution> solutionList;
//////    private LayoutInflater inflater;
//////
//////    public SolutionAdapter(Context context, List<Solution> solutionList) {
//////        this.solutionList = solutionList;
//////        this.inflater = LayoutInflater.from(context);
//////    }
//////
//////
//////    @Override
//////    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//////        View view = inflater.inflate(R.layout.list_item, parent, false);
//////        MainViewHolder holder = new MainViewHolder(view);
//////        return holder;
//////    }
//////
//////    @Override
//////    public void onBindViewHolder(MainViewHolder holder, int position) {
//////        Solution currentSolution = solutionList.get(position);
//////        holder.setdata(currentSolution, position);
//////    }
//////
//////    @Override
//////    public int getItemCount() {
//////        return solutionList.size();
//////    }
////}
//
//package com.lawrene.falcon.copycopy;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.theartofdev.edmodo.cropper.CropImage;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.HashMap;
//
//import id.zelory.compressor.Compressor;
//
//public class AdminActivity extends AppCompatActivity {
//
//    //---------------------All arrays-----------------------------//
//    String[] allschools = {"University of Uyo", "University of Port Harcourt", "University of Lagos"};
//    String[] allfaculties = {"Engineering", "Science", "Medicine", "Law"};
//    String[] alldepartments = {};
//    String[] alldepartmentsEng = {"Mechanical", "Electrical", "Computer", "Chemical"};
//    String[] alldepartmentsSci = {"Physics", "Biochemistry", "Anatomy", "Maths and Stats"};
//    String[] alllevels = {};
//    String[] alllevelsEng = {"100", "200", "300", "400", "500"};
//    String[] alllevelsSci = {"100", "200", "300", "400"};
//
//    //    ---------------------Declaration of the views--------------------
//    Spinner mSchoolSpinner;
//    Spinner mFacultySpinner;
//    Spinner mDepartmentSpinner;
//    Spinner mLevelSpinner;
//    Button mUploadBtn, mSelectBtn;
//    ProgressDialog mProgressDialog;
//    Toolbar mToolbar;
//    Uri mResultUri;
//    String mDownloadURL;
//    String mThumbDownloadURL;
//
//    String postID;
//
//    DatabaseReference mPostDatabase;
//    StorageReference mFilesStorage;
//    StorageReference mThumbFireStorage;
//
//    String mSchool, mFaculty, mDepartment, mlevel;
//    FirebaseAuth mFireAuth;
//
//    private static final int GALLERY_PICK = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin);
//
//        //-------------------------Setting up  firebase---------------------
//        mFireAuth = FirebaseAuth.getInstance();
//
//        loadAllViews();
//        loadOnClick();
//
//        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child("Schools");
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
//            Uri imageUri = data.getData();
//            CropImage.activity(imageUri)
//                    .setAspectRatio(1, 1)
//                    .start(this);
//        }
//
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//
//            if (resultCode == RESULT_OK) {
//                mResultUri = result.getUri();
////
////                File thumb_filePath = new File(resultUri.getPath());
////
////                String current_uid = mCurrentUser.getUid();
////
////                Bitmap thumb_bitmap = new Compressor(this)
////                        .setMaxWidth(200)
////                        .setMaxHeight(200)
////                        .setQuality(75)
////                        .compressToBitmap(thumb_filePath);
////
////                ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
////                final byte[] thumb_byte = baos.toByteArray();
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
//    }
//
//    public void loadAllViews() {
//        //-----------------------------Setting up the views-----------------------
//        mSchoolSpinner = (Spinner) findViewById(R.id.adminschoolspinner);
//        mFacultySpinner = (Spinner) findViewById(R.id.adminfacultyspinner);
//        mDepartmentSpinner = (Spinner) findViewById(R.id.admindept_spinner);
//        mLevelSpinner = (Spinner) findViewById(R.id.adminlevel_spinner);
//        mUploadBtn = (Button) findViewById(R.id.adminPostBtn);
//        mSelectBtn = (Button) findViewById(R.id.admin_select_btn);
//
//
//        //---------------------Setting up the arrayadapter to use for the spinners------------------
//        ArrayAdapter school_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allschools);
//        school_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSchoolSpinner.setAdapter(school_array_adapter);
//
//        ArrayAdapter faculty_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, allfaculties);
//        faculty_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mFacultySpinner.setAdapter(faculty_array_adapter);
//
//        ArrayAdapter dept_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alldepartments);
//        dept_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mDepartmentSpinner.setAdapter(dept_array_adapter);
//
//        ArrayAdapter level_array_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, alllevels);
//        level_array_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mLevelSpinner.setAdapter(level_array_adapter);
//    }
//
//    public void loadOnClick() {
//        mUploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(AdminActivity.this, "The user upload path is: " + mSchool+ mFaculty+ mDepartment+mlevel, Toast.LENGTH_SHORT).show();
////                Toast.makeText(AdminActivity.this, "The user upload path is: " + resultUri, Toast.LENGTH_SHORT).show();
//                if (mResultUri == null) {
//                    Toast.makeText(AdminActivity.this, "Please select at least one image to continue", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    mProgressDialog = new ProgressDialog(AdminActivity.this);
//                    mProgressDialog.setTitle("Please Wait");
//                    mProgressDialog.setMessage("Uploading Image");
//                    mProgressDialog.setCanceledOnTouchOutside(false);
//                    mProgressDialog.show();
//
//                    putFullImage();
//                }
//            }
//        });
//
//
//        mSelectBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setType("image/*");
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image to Upload"), GALLERY_PICK);
//            }
//        });
//
//
//        mSchoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                mSchool = allschools[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        mFacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                mFaculty = allfaculties[position];
//                switch (mFaculty) {
//                    case "Engineering":
//                        alldepartments = alldepartmentsEng;
//                        alllevels = alllevelsEng;
//
//                        ArrayAdapter eng_depts_adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_spinner_item, alldepartments);
//                        eng_depts_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mDepartmentSpinner.setAdapter(eng_depts_adapter);
//
//                        ArrayAdapter eng_levels_adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_spinner_item, alllevels);
//                        eng_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mLevelSpinner.setAdapter(eng_levels_adapter);
//                        break;
//
//
//                    case "Science":
//                        alldepartments = alldepartmentsSci;
//                        alllevels = alllevelsSci;
//
//                        ArrayAdapter science_adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_spinner_item, alldepartments);
//                        science_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mDepartmentSpinner.setAdapter(science_adapter);
//
//                        ArrayAdapter sci_levels_adapter = new ArrayAdapter(AdminActivity.this, android.R.layout.simple_spinner_item, alllevels);
//                        sci_levels_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        mLevelSpinner.setAdapter(sci_levels_adapter);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        mDepartmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                mDepartment = alldepartments[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                mlevel = alllevels[position];
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
//
//    private void putFullImage() {
//
//        File thumb_filePath = new File(mResultUri.getPath());
//        Bitmap thumb_bitmap = new Compressor(AdminActivity.this)
//                .setMaxWidth(200)
//                .setMaxHeight(200)
//                .setQuality(75)
//                .compressToBitmap(thumb_filePath);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        final byte[] thumb_byte = baos.toByteArray();
//
//
//
//
//        mFilesStorage = FirebaseStorage.getInstance().getReference().child(mSchool)
//                .child(mFaculty).child(mDepartment).child(mlevel)
//                .child("bvcbvb" + ".jpg");
//
//        mThumbFireStorage = FirebaseStorage.getInstance().getReference().child(mSchool)
//                .child(mFaculty).child(mDepartment).child(mlevel)
//                .child("thumbnails").child("bvcbvb" + ".jpg");
//
//
//        mFilesStorage.putFile(mResultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//                    mDownloadURL = task.getResult().getDownloadUrl().toString();
//
//                    UploadTask uploadTask = mThumbFireStorage.putBytes(thumb_byte);
//
//                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
//                            if(thumb_task.isSuccessful()){
//                                mThumbDownloadURL = thumb_task.getResult().getDownloadUrl().toString();
//
//                                HashMap<String, String> postmap = new HashMap<>();
//                                postmap.put("title", "New Post");
//                                postmap.put("date", "666 mayy");
//                                postmap.put("image", mDownloadURL);
//                                postmap.put("thumb_image", mThumbDownloadURL);
//
//                                mPostDatabase.child(mSchool).child(mFaculty).child(mDepartment).child(mlevel).push().setValue(postmap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            mProgressDialog.dismiss();
//                                            Toast.makeText(AdminActivity.this, "File Uploaded Succesfully", Toast.LENGTH_SHORT).show();
//                                        }
//
//                                        else{
//                                            mProgressDialog.dismiss();
//                                            Toast.makeText(AdminActivity.this, "Could post in db", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                } else {
//                    mProgressDialog.dismiss();
//                    Toast.makeText(AdminActivity.this, "Could not Upload Image", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//}
