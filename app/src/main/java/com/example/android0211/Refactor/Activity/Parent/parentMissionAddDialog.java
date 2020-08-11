package com.example.android0211.Refactor.Activity.Parent;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android0211.R;
import com.example.android0211.Refactor.Util.Retrofit.INodeJS;
import com.example.android0211.Refactor.Util.Retrofit.RetrofitClient;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class parentMissionAddDialog {
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Integer missiontime;


    private Context context;

    public parentMissionAddDialog(Context context) {
        this.context = context;
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);
    }
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        ArrayList arrayList = new ArrayList<String>();
        arrayList.add("10분");
        arrayList.add("20분");
        arrayList.add("30분");

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_mission_add);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        //EditText misTitle = dlg.findViewById(R.id.misTitle);
        EditText misContent = dlg.findViewById(R.id.misContent);
        EditText misEmail = dlg.findViewById(R.id.misEmail);
        EditText misName = dlg.findViewById(R.id.misName);
        EditText misTime = dlg.findViewById(R.id.misTime);
        Button misOK = dlg.findViewById(R.id.misOK);
        Button misCancel = dlg.findViewById(R.id.misCancel);

        misOK.setOnClickListener(view -> {
            mission_add(misEmail.getText().toString(),misName.getText().toString(),misContent.getText().toString(),Integer.parseInt(misTime.getText().toString()));
            dlg.dismiss();
        });
        misCancel.setOnClickListener(view -> {
            Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
            // 커스텀 다이얼로그를 종료한다.
            dlg.dismiss();
        });

    }
    private void mission_add(final String email,final String child_name,final String content,final Integer time){
        compositeDisposable.add(myAPI.mission_add(email,child_name,content,time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s->{
                    Log.e("asdf",""+s);


                })
        );
    }
}
