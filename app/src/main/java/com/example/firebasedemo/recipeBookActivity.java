package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class recipeBookActivity extends AppCompatActivity {

    String name;
    ArrayList<String> s1=new ArrayList<>();
    Map<String,Object> docMapData;
    private String  recipeName, recipeOneLine, searchedData;
    Map<String, Object> recipeListData;
    ArrayList<String> list1, list2, docList;
    ArrayList<Integer> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);

        FirebaseFirestore db2;
        db2 = FirebaseFirestore.getInstance();
        name = FirebaseAuth.getInstance().getCurrentUser().getUid();

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        imageList = new ArrayList<>();
        docList = new ArrayList<>();

        db2.collection("recipes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        recipeListData = document.getData();
                        recipeName = recipeListData.get("Name").toString();
                        recipeOneLine = recipeListData.get("oneline").toString();

                        String docname = document.getId().toString();

                        String ownerName = recipeListData.get("Owner").toString();



                            if(ownerName.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                list1.add(recipeName);
                                list2.add(recipeOneLine);
                                imageList.add(R.drawable.cherries);
                                docList.add(docname);
                            }




                    }

                    if(!list1.isEmpty()){
                        setRecycler(list1, list2, imageList, docList);
                    }
                    else
                    {
                        TextView t=findViewById(R.id.textView21);
                        t.setVisibility(View.VISIBLE);
                    }


                }
            }
        });




    }
    public void setRecycler(ArrayList<String> l1, ArrayList<String> l2, ArrayList<Integer> l3, ArrayList<String> l4) {
        RecyclerView recyclerView;
        Adapter adapter;

        recyclerView = findViewById(R.id.personalrecipebookrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter(this, l1, l2, l3, l4);
        recyclerView.setAdapter(adapter);
    }
}