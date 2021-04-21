package seoulsoft.kr.campusup.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import seoulsoft.kr.campusup.R;

public class LoginEtcActivity extends AppCompatActivity {
    private Button loginEtc_email_btn;
    private TextView loginEtc_search_btn, loginEtc_sign_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_etc);

        //findViewById를 통해 가져오기
        loginEtc_email_btn = findViewById(R.id.loginEtc_email_btn);
        loginEtc_sign_btn = findViewById(R.id.loginEtc_sign_btn);
        loginEtc_search_btn = findViewById(R.id.loginEtc_search_btn);

        loginEtc_email_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEtcActivity.this, LoginActivity.class));
            }
        });

        loginEtc_sign_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEtcActivity.this, SignupActivity.class));
            }
        });

        loginEtc_search_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEtcActivity.this, UserSearchActivity.class));
            }
        });


    }
}