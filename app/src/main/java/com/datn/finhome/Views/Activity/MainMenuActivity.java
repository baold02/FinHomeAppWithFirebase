package com.datn.finhome.Views.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.datn.finhome.Controllers.UserDao;
import com.datn.finhome.Interfaces.IAfterGetAllObject;
import com.datn.finhome.Models.UserModel;
import com.datn.finhome.R;
import com.datn.finhome.Views.Fragment.HomeFragment;
import com.datn.finhome.Views.Fragment.AccountViewFragment;
import com.datn.finhome.Views.Fragment.SearchFragment;
import com.datn.finhome.chat.Chat2Fragment;
import com.datn.finhome.chat.ChatFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    FrameLayout fragmentContainer;
    HomeFragment HomeView;
    AccountViewFragment AccountView;
    SearchFragment searchFragment;
//    ChatFragment chatFragment;
    Chat2Fragment chatFragment;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                 UserModel  userModel = dataSnapshot.getValue(UserModel.class);
                    if (Objects.equals(userModel.getUserID(),uid)) {
                        Log.d("aaa",userModel.email);
                        if (userModel.isGender() == true){
                            setContentView(R.layout.activity_main_menu2);
                            HomeView = new HomeFragment();
                            initControl();
                            setFragment(HomeView);

                        }else if(userModel.isGender() == false){
                            setContentView(R.layout.activity_main_menu);
                            HomeView = new HomeFragment();
                            initControl();
                            setFragment(HomeView);

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                 Log.d("error",error.toString());
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();


    }


    private void initControl(){
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigation = findViewById(R.id.bottom_nav_home);

        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();

            switch (id) {
                case R.id.nav_home:
                    HomeView = new HomeFragment();
                    setFragment(HomeView);
                    return true;
                case R.id.nav_account:
                    AccountView = new AccountViewFragment();
                    setFragment(AccountView);
                    return true;
                case R.id.nav_chat:
                    chatFragment = new Chat2Fragment();
                    setFragment(chatFragment);
                    return true;

//                case R.id.nav_post:
//                    postRoomFragment = new PostRoomFragment();
//                    setFragment(postRoomFragment);
//                    return true;

                case R.id.nav_search:
                    searchFragment = new SearchFragment();
                    setFragment(searchFragment);
                    return true;

                default:
                    return false;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    public void btnadd(View view) {
        UserModel user = new UserModel();
        if(user.isGender() == true){
            Toast.makeText(MainMenuActivity.this, "Tài khoản của bạn là khách hàng", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainMenuActivity.this, "Đăng bài", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,AddRoomActivity.class);
            startActivity(intent);
        }

    }

}