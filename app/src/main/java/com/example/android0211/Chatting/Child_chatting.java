package com.example.android0211.Chatting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android0211.R;
import com.example.android0211.Retrofit.INodeJS;
import com.example.android0211.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Child_chatting extends Fragment {
    private Button btn;
    // private EditText nickname;
    public static final String NICKNAME = "usernickname";
    public static String childName = "sechankid";

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String childname;

    public Child_chatting(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.activity_child_chatting, container, false);
        Retrofit retrofit1 = RetrofitClient.getInstance();
        myAPI = retrofit1.create(INodeJS.class);
        try {
            //call UI component  by id
            btn = rootView.findViewById(R.id.enterchat);

                  //extract_cName(this.getArguments().getString("email"));

            String nickname = childName; //  db가져오면 지워야 하는 부분 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            btn.setOnClickListener(v -> {
                //if the nickname is not empty go to chatbox activity and add the nickname to the intent extra
                //if(!nickname.getText().toString().isEmpty()){
                //    Intent i  = new Intent(Child_chatting.this,ChatBoxActivity.class);
                //    //retreive nickname from textview and add it to intent extra
                //    i.putExtra(NICKNAME,nickname.getText().toString());

                Intent i = new Intent(getActivity(), ChatBoxActivity.class);
                //retreive nickname from textview and add it to intent extra
                i.putExtra(NICKNAME, nickname);

                startActivity(i);
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return rootView;
    }

}
