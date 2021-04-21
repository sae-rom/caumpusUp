package seoulsoft.kr.campusup.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import seoulsoft.kr.campusup.Common.MainActivity;
import seoulsoft.kr.campusup.R;

public class LoginActivity extends AppCompatActivity {
    private Button login_btn;
    private TextView search_btn, sign_btn;
    private EditText email, passwd;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //로그인 모듈 변수
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser(); //로그인된 유저 정보 담는 변수

    private BackPressHandler backPressHandler = new BackPressHandler(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //findViewById를 통해 가져오기
        search_btn = findViewById(R.id.search_btn);
        sign_btn = findViewById(R.id.sign_btn);
        login_btn = findViewById(R.id.login_btn);

        email = findViewById(R.id.login_email);
        passwd = findViewById(R.id.login_password);

        search_btn.setOnClickListener(new Button.OnClickListener() { // 아이디, 비밀번호 찾기 버튼 클릭 시
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, UserSearchActivity.class));
            }
        });

        sign_btn.setOnClickListener(new Button.OnClickListener() { //회원가입 버튼 클릭 시
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login_btn.setOnClickListener(new Button.OnClickListener() { //로그인 버튼 클릭 시
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString().trim();
                String passwdText = passwd.getText().toString().trim();

                //SharedPreferences 파일에 회원정보 저장
                final SharedPreferences preferences = getSharedPreferences("sFile",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                //유효성 중복 체크
                if(TextUtils.isEmpty(emailText)){
                    Toast.makeText(LoginActivity.this, "이메일(아이디)을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(passwdText)){
                    Toast.makeText(LoginActivity.this, "비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    firebaseAuth.signInWithEmailAndPassword(emailText, passwdText)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e) {
                                            Toast.makeText(LoginActivity.this, "아이디나 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Toast.makeText(LoginActivity.this, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseNetworkException e) {
                                            Toast.makeText(LoginActivity.this, "파이어베이스 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Log.v("에러메시지", e.getMessage());
                                            Toast.makeText(LoginActivity.this, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        if (authUser != null){
                                            mStore.collection("User").document(authUser.getUid())
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        if(task.getResult() != null){
                                                            editor.putString("email", (String) task.getResult().get("email"));
                                                            editor.putString("nickname", (String) task.getResult().get("nickname"));
                                                            editor.putString("password", (String) task.getResult().get("password"));
                                                            editor.putString("phonenumber", (String) task.getResult().get("phonenumber"));
                                                            editor.putString("university", (String) task.getResult().get("university"));
                                                            editor.putString("auto_login", (String) task.getResult().get("auto_login"));
                                                            editor.commit();
                                                        }
                                                    }

                                                }
                                            });
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }else{
                                            Toast.makeText(LoginActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);
    }
}