package com.bistu.secondexperiment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Target;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    String userName = "";
    String userTheme = "";
    String userDate = "";
    String userData = "";
    String userPicturePath = "0";
    private Uri imageUri;
    private ImageView picture;

    //创建数据库
    private MyDatabaseHelper_user dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //从sharepreference中获取作者信息
        SharedPreferences editor_1 = this.getSharedPreferences("user", MODE_PRIVATE);
        userName = editor_1.getString("name",null);

        dbHelper = new MyDatabaseHelper_user(this, "note.db", null, 1);

        EditText name = findViewById(R.id.ed_2);
        Button button = findViewById(R.id.but_1);
        button.setOnClickListener(this);
        Button button_1 = findViewById(R.id.but_camera);
        button_1.setOnClickListener(this);
        Button button_2 = findViewById(R.id.but_picture);
        button_2.setOnClickListener(this);
        picture = findViewById(R.id.image_1);


        //获取上个界面传送来的date信息
        Intent intent = getIntent();
        String key = intent.getStringExtra("date");

        if(key!=null){
            //查看信息
            look(key);
        }
        name.setText(userName);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.but_1:
                //判断是否输入了日期
                EditText date2 = findViewById(R.id.ed_4);
                userDate = date2.getText().toString();
                if(userDate.equals("")){
                    Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
                }
                else{
                    //判断输入日期的格式是否正确
                    int j = 0;
                    char str[] = userDate.toCharArray();
                    for(int i = 0;i < str.length;i++){
                        j = 0;
                        if(str[i] < '0' || str[i] > '9'){
                            if(str[i] != '/'){
                                Toast.makeText(this,"请输入正确格式的日期",Toast.LENGTH_SHORT).show();
                                j++;
                            }
                        }
                    }
                    if(j == 0){
                        write();
                    }
                }
                break;
            case R.id.but_camera:
                //判断日期是否为空
                EditText date = findViewById(R.id.ed_4);
                userDate = date.getText().toString();
                if(userDate.equals("")){
                    Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
                }
                else{
                    //启动相机
                    camera(userDate);
                }
                break;
            case R.id.but_picture:
                //判断是否授予读写权限
                if(ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(WriteActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                EditText date1 = findViewById(R.id.ed_4);
                userDate = date1.getText().toString();
                if(userDate.equals("")){
                    Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
                }
                else{
                    //启动相册
                    openAlbum();
                }
                break;
        }
    }

    //打开相册，筛选照片
    public void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int [] grantResults){
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }

    }


    //
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        //解析document类型的uri，并将真实的地址送到imagepath中
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            //进一步判断是否是media格式
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
            else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath = getImagePath(uri,null);
            }
            else if("file".equalsIgnoreCase(uri.getScheme())){
                imagePath = uri.getPath();
            }
            userPicturePath = imagePath;
            displayImage(imagePath);
        }

        //显示图片
    private void displayImage(String imagePath) {
       //安卓10规定，只能访问当前应用目录下的文件，所以在清单文件重要加入android:requestLegacyExternalStorage="true"，用于访问全部内存
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    //获取图片的真是路径
    private String getImagePath(Uri uri, String selection ) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void camera(String date){
        //在当前文件的目录下新建文件，用于存储照片
        File outputImage = new File(getExternalCacheDir(),date+".jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果SDK版本大于24，对照片地址进行封装
        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(WriteActivity.this,"com.bistu.secondexperiment.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }
        //打开相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.addFlags(intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

        userPicturePath = outputImage.getPath();
}


//对action的结果进行分析
protected  void onActivityResult(int requestCode,int resultCode,Intent data) {

    switch (requestCode) {
        case TAKE_PHOTO:
            if (resultCode == RESULT_OK) {
                //将拍摄的照片解析成bitmap，显示出来
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    picture.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            break;
        case CHOOSE_PHOTO:
            if(resultCode == RESULT_OK){
                //如果SDK版本大于19，则调用方法解析封装过的uri
                if(Build.VERSION.SDK_INT >= 19){
                    handleImageOnKitKat(data);
                }
            }
            break;
        default:
            break;
    }

}

    //往数据库写入数据
    public void write(){

        EditText name = findViewById(R.id.ed_2);
        EditText theme = findViewById(R.id.ed_3);
        EditText date = findViewById(R.id.ed_4);
        EditText data = findViewById(R.id.ed_1);

        userTheme = theme.getText().toString();
        userData = data.getText().toString();
        userDate = date.getText().toString();
        userName = name.getText().toString();


        if(userDate.equals("")){
            Toast.makeText(this, "请输入日期", Toast.LENGTH_SHORT).show();
        }

        else{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String s = "select * from note where date ='"+userDate+"'";
            Cursor cursor = db.rawQuery(s,null);
            cursor.moveToFirst();

            ContentValues values = new ContentValues();
            values.put("name", userName);
            values.put("data", userData);
            values.put("date", userDate);
            values.put("theme", userTheme);
            values.put("picture",userPicturePath);

            //判断是更新还是新增
            if(cursor.getCount() != 0){
                db.update("note", values, "date = ?", new String[]{userDate});
                Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            }
            else{
                db.insert("note", null, values);
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            }
            values.clear();
            Intent intent = new Intent(WriteActivity.this,ListActivity.class);
            startActivity(intent);
        }
    }

    //根据日期查看数据库信息
    public void look(String str){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        EditText theme = findViewById(R.id.ed_3);
        EditText date = findViewById(R.id.ed_4);
        EditText data = findViewById(R.id.ed_1);
        ImageView image = findViewById(R.id.image_1);
        String path = "";

        String s = "select * from note where date ='"+str+"'";
        Cursor cursor = db.rawQuery(s,null);
        cursor.moveToFirst();
        userTheme = cursor.getString(cursor.getColumnIndex("theme"));
        userData = cursor.getString(cursor.getColumnIndex("data"));
        path = cursor.getString(cursor.getColumnIndex("picture"));
        image.setImageBitmap(BitmapFactory.decodeFile(path));
        theme.setText(userTheme);
        data.setText(userData);
        date.setText(str);
        cursor.close();
    }
}