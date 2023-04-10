package com.datn.finhome.Views.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.datn.finhome.Models.UserModel;
import com.datn.finhome.PayHistory.PayHistoryActivity;
import com.datn.finhome.R;
import com.datn.finhome.Views.Activity.AccountInfoActivity;
import com.datn.finhome.Views.Activity.ChangePassActivity;
import com.datn.finhome.Views.Activity.FavoriteActivity;
import com.datn.finhome.Views.Activity.HostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class AccountViewFragment extends Fragment implements View.OnClickListener {
    private Button btnLogout,btnChangePass,btnFavorite,btnSettingAccount, btnMyRoom,btnPayHistory;
    private TextView tvName,tvPhone,tvDiaChi;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference, getDatabaseReference;
    ImageView ImgAvt;
    private String userId;
    FirebaseAuth firebaseAuth;
    View view;
    UserModel userModel2;
    private Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view =  inflater.inflate(R.layout.fragment_user, container, false);

        getDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        getDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    UserModel  userModel = dataSnapshot.getValue(UserModel.class);
//                    if (Objects.equals(userModel.getUserID(),userId)) {
//                        userModel2 = userModel;
//                        Log.d("aaa",userModel.email);
//                        if (userModel.isGender() == true){
//                            initControl2();
//                            getInformationUser();
//                            return view;
//
//                        }else if(userModel.isGender() == false){
//                            layout = inflater.inflate(R.layout.fragment_user, container, false);
//                            initControl();
//                            getInformationUser();
//
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("error",error.toString());
//            }
//        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        initControl();
        getInformationUser();
        return view;


    }
    private void initControl() {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnMyRoom = view.findViewById(R.id.btn_myroom);
        btnChangePass = view.findViewById(R.id.btnChangePass);
        btnFavorite = view.findViewById(R.id.btnFavourite3);
        btnSettingAccount = view.findViewById(R.id.btnSettingAccount);
        btnPayHistory = view.findViewById(R.id.btnPayHistory);
        tvName = view.findViewById(R.id.tvNameUser);
        tvPhone = view.findViewById(R.id.tvSdtUser);
        tvDiaChi = view.findViewById(R.id.tvAddressUser);
        ImgAvt = view.findViewById(R.id.imgUser);

        btnMyRoom.setOnClickListener(v -> startActivity(new Intent(getActivity(), HostActivity.class)));
        btnFavorite.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavoriteActivity.class)));
        btnSettingAccount.setOnClickListener(v-> {startActivity(new Intent(getActivity(), AccountInfoActivity.class));});
        btnPayHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), PayHistoryActivity.class)));
        btnChangePass.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChangePassActivity.class)));
        btnLogout.setOnClickListener(this);
    }


    private void getInformationUser(){
      databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                if(userModel != null){
                    String fullName = userModel.name;
                    String Phone = userModel.phoneNumber;
                    String avatar = userModel.avatar;
                    String address = userModel.address;

                    tvName.setText(fullName);
                    tvPhone.setText(Phone);
//                    Glide.with(mContext).load(avatar).into(ImgAvt);
                    Picasso.get().load(avatar).placeholder(R.mipmap.ic_launcher).into(ImgAvt);
                    tvDiaChi.setText(address);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, "That bai", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getInformationGoogle(){
//        SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String userName = preferences.getString("username","");
//        String userEmail = preferences.getString("useremail","");
//        String userAvt = preferences.getString("userAvatar","");
//
//        tvName.setText(userName);
//        tvPhone.setText(userEmail);
//        Picasso.get().load(userAvt).into(ImgAvt);
////        Glide.with(this).load(userAvt).into(ImgAvt);
//    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void signout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Bạn có chắc muốn đăng xuất?")
                .setPositiveButton("CÓ", (dialog, which) -> {
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    getActivity().finish();
                })
                .setNegativeButton("HỦY", (dialog, which) -> dialog.dismiss());
        Dialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogout:
                signout();
        }
    }

}
