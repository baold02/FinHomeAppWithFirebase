package com.datn.finhome.Controllers;

import com.datn.finhome.Interfaces.IAfterInsertObject;
import com.datn.finhome.Interfaces.IAfterUpdateObject;
import com.datn.finhome.Models.RoomModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class RoomController {
    private static RoomController instance;

    private RoomController() {
    }

    public static RoomController getInstance() {
        if (instance == null) {
            instance = new RoomController();
        }
        return instance;
    }

    public void insertProduct(RoomModel room, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("Room").child(room.getId())
                .setValue(room, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(room);
                    } else {
                        iAfterInsertObject.onError(error);
                    }

                });
    }

    public void updateRoom(RoomModel room, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("Room").child(room.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(room); // trả về user đã được update
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }
}
