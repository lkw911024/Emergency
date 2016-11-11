package com.emergency.lecture.emergency;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    private EditText id;
    private EditText password;
    private EditText passwordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }
    public void onClickSignUpBtn(View v)
    {
        System.out.println("등록을 눌렀습니다.");
    }

}
