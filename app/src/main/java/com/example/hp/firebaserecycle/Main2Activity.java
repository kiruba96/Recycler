package com.example.hp.firebaserecycle;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    public ArrayList<Adapter> itemAdapter =new ArrayList<>();
    public ItemAdapter itemArrayAdapter;
    String url="https://recyclerexample-71934.firebaseio.com/";
    Firebase fb;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    GridLayoutManager gridLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this,2);


        recyclerView.setLayoutManager(gridLayoutManager );

        fb = new Firebase(url);
        Firebase.setAndroidContext(this);
        new CardRetreival_Task().execute();

    }
   public class CardRetreival_Task extends AsyncTask<String, Integer, String>{

       @Override
       protected String doInBackground(String... strings) {
           fb.child("RECYCLER").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("THe children is : "+dataSnapshot.getChildrenCount());
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        ImgAdapter imgAdapter = child.getValue(ImgAdapter.class);
                        System.out.println("The url is : "+imgAdapter.getUrl());
                        itemAdapter.add(0,new Adapter(imgAdapter.getUrl()));

                    }

                    itemArrayAdapter = new ItemAdapter(R.layout.card,itemAdapter);
                    recyclerView.setAdapter(itemArrayAdapter);


               }

               @Override
               public void onCancelled(FirebaseError firebaseError) {

               }
           });
           return null;
       }
   }

}
