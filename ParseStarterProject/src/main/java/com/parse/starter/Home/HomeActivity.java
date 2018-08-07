package com.parse.starter.Home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.starter.R;
import com.parse.util.BottomNavViewHelper;

public class HomeActivity extends AppCompatActivity {

    TextView currentUserTextview;
    private Context mContext = HomeActivity.this;
    private static final int ACTIVITY_NUM = 0;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        setTitle("User List");
//
//        currentUserTextview = findViewById(R.id.currentUserTextview);
//
//        final ListView listView = findViewById(R.id.listView);
//        final ArrayList<String> usernames = new ArrayList<String>();
//        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
//                intent.putExtra("username", usernames.get(position));
//                startActivity(intent);
//            }
//        });
//
//        String currentUsername = ParseUser.getCurrentUser().getUsername().toString();
//
//        currentUserTextview.setText(currentUsername);
//
//        ParseQuery<ParseUser> query  = ParseUser.getQuery();
//
//        //querying all users, except logged in user
//        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
//        query.addAscendingOrder("username");
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//                if(e == null){
//                    if(objects.size() > 0){
//                        for(ParseUser user: objects){
//                            usernames.add(user.getUsername());
//                        }
//                        listView.setAdapter(arrayAdapter);
//                    }
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.share_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.share:{
//                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//                }else{
//                    getPhoto();
//                }
//                break;
//            }
//            case R.id.logout:{
//                logout();
//                break;
//            }
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == 1){
//            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                getPhoto();
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
//            try {
//                Uri selectedImage = data.getData();
//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                Log.i("Image Select", "Selected");
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//                //puts image in correct format
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                byte[] byteArray = stream.toByteArray();
//                ParseFile file = new ParseFile("image.png", byteArray);
//
//                ParseObject obj = new ParseObject("Images");
//
//                obj.put("image", file);
//                obj.put("username", ParseUser.getCurrentUser().getUsername());
//                obj.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e == null) {
//                            Toast.makeText(HomeActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(HomeActivity.this, "Could not post", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            } catch (NullPointerException npe) {
//                npe.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void getPhoto(){
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 1);
//    }
//
//    public void logout(){
//        Intent intent = new Intent(getApplication(), MainActivity.class);
//        ParseUser.logOut();
//        startActivity(intent);
        setUpBottomNavView();
    }
    
    private void setUpBottomNavView(){
        Log.d(TAG, "setUpBottomNavView: init bottom nav");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavViewHelper.setUpBottomNavView(bottomNavigationViewEx);
        BottomNavViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}