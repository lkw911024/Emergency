package com.emergency.lecture.emergency;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.emergency.lecture.emergency.webserver.WebServerSignUp;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    public InputMethodManager imm;     // 바탕화면 클릭 시, 키보드 없어지게 하기 위한 필드.

    public EditText idEt;
    public EditText pwdEt;
    public EditText pwdConfirmEt;
    public EditText emailEt;

    public Button signupBtn;

    public AppCompatActivity appCompatActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        idEt = (EditText) findViewById(R.id.signUp_idInput_et);
        pwdEt = (EditText) findViewById(R.id.signUp_pwdInput_et);
        pwdConfirmEt = (EditText) findViewById(R.id.signUp_pwdConfirmInput_et);
        emailEt = (EditText) findViewById(R.id.signUp_emailInput_et);

        signupBtn = (Button) findViewById(R.id.signUp_signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = idEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                String pwdConfirm = pwdConfirmEt.getText().toString();
                String email = emailEt.getText().toString();

                if(id.equals("") || pwd.equals("") || pwdConfirm.equals("") || email.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!pwd.equals(pwdConfirm))
                {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$",email))
                {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                WebServerSignUp web = new WebServerSignUp(appCompatActivity, getApplicationContext(), id, pwd, email);
                web.start();

            }
        });
    }

    /*
    키보드 들어가게 하기 위해 Layout의 onClick 메소드로 설정
    */
    public void layoutOnClick(View v)
    {
       imm.hideSoftInputFromWindow(idEt.getWindowToken(), 0);
    }

}
