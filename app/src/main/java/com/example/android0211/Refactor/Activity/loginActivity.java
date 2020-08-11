package com.example.android0211.Refactor.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android0211.R;
import com.example.android0211.Refactor.Activity.Child.childMain;
import com.example.android0211.Refactor.Util.Retrofit.INodeJS;
import com.example.android0211.Refactor.Util.Retrofit.RetrofitClient;
import com.example.android0211.Refactor.Util.UserInfo.UserInfo;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class loginActivity extends AppCompatActivity {

    UserInfo user;//유저정보를 저장하는 객체 생성

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MaterialEditText edt_email, edt_password;
    MaterialButton btn_register, btn_login;

    Button btnCheat; // cheat

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
            Log.i("TAGGG", token);
            //init API
            Retrofit retrofit = RetrofitClient.getInstance();
            myAPI = retrofit.create(INodeJS.class);

            //View
            btn_login = findViewById(R.id.login_button);
            btn_register = findViewById(R.id.register_button);
            //btnCheat = findViewById(R.id.cheat); // cheat

            edt_email = findViewById(R.id.edt_email);
            edt_password = findViewById(R.id.edt_password);

            btn_login.setOnClickListener(v -> loginUser(edt_email.getText().toString(), edt_password.getText().toString()));

            btnCheat.setOnClickListener(v -> { // cheat
                Intent intent = new Intent(this, childMain.class);
                startActivity(intent);
            });

            btn_register.setOnClickListener(v -> registerUser(edt_email.getText().toString(), edt_password.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //파라미터 안에 final String macAdd

    private void registerUser(final String email, final String password) {
        final View enter_name_view = LayoutInflater.from(this).inflate(R.layout.enter_name_layout, null);

        new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("One more step!")
                .setCustomView(enter_name_view)
                .setIcon(R.drawable.ic_user)
                .setNegativeText("Cancel")
                .onNegative((dialog, which) -> dialog.dismiss())
                .setPositiveText("Register")
                .onPositive((dialog, which) -> {
                    MaterialEditText edt_name = enter_name_view.findViewById(R.id.edt_name);
                    compositeDisposable.add(myAPI.registerUser(email, edt_name.getText().toString(), password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(str -> showToast(str)));
                }).show();

    }

    private void showToast(@NonNull final String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void loginUser(String email, String password) {
        /*if(email.equals("Test"))
        {Intent intent = new Intent(getApplicationContext(),SelectActivty.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }*/
        compositeDisposable.add(myAPI.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (s.contains("encrypted_password")) {
                        //Toast.makeText(MainActivity.this, "로그인 성공" + s, Toast.LENGTH_LONG).show();
                        //이메일 넘김
                        Intent intent = new Intent(getApplicationContext(), selectActivty.class);
                        intent.putExtra("email", email);
                        //여기에 유저 정보를 저장하는 객체 삽입
                        startActivity(intent);
                    }
                })
        );
    }

    //기기의 맥주소 출력

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();

                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();

                for (int idx = 0; idx < mac.length; idx++)

                    buf.append(String.format("%02X:", mac[idx]));

                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);

                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions

        return "";
    }


    String HardwareAdd = getMACAddress("wlan0");//여기에 저장


}
