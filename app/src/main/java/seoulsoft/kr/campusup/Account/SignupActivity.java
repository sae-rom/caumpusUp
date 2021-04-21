package seoulsoft.kr.campusup.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seoulsoft.kr.campusup.R;

public class SignupActivity extends AppCompatActivity {
    private Spinner spinner, spinner2;
    private ArrayAdapter arrayAdapter;

    private EditText sign_email, sign_passwd, sign_passwd_confirm, sign_nickname, sign_phone;
    private Button phone_btn, signup_btn;
    private TextView sign_backbtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어스토어에 접근하기 위한 객체 생성

    private ArrayList<String> universityArrayList;
    private ArrayAdapter<String> areaAdapter;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private Boolean phoneCheck = false;

    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,20}$"); // 8자리 ~ 20자리까지 가능

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        spinner = findViewById(R.id.sign_type);
        spinner2 = findViewById(R.id.sign_university);

        phone_btn = findViewById(R.id.phone_btn);
        sign_backbtn = findViewById(R.id.sign_backbtn);
        signup_btn = findViewById(R.id.signup_btn);

        sign_email = findViewById(R.id.sign_email);
        sign_passwd = findViewById(R.id.sign_passwd);
        sign_passwd_confirm = findViewById(R.id.sign_passwd_confirm);
        sign_nickname = findViewById(R.id.sign_nickname);
        sign_phone = findViewById(R.id.sign_phone);


        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.type, R.layout.spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        //textView = findViewById(R.id.textView8);
        //컬렉션에 접근 (대학교 이름 가져와서 spinner에 추가하는 부분)
        universityArrayList = new ArrayList<>();
        CollectionReference local_reference = db.collection("Local");
        local_reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //성공적으로 읽어온 경우
                try {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot local_document : task.getResult()) {
                            //하위 컬렉션
                            CollectionReference univ_reference = db.collection("Local").document(String.valueOf(local_document.getId())).collection("Univ");
                            univ_reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //하위 컬렉션을 성공적으로 읽어온 경우
                                    try {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot univ_document : task.getResult()) {
                                                universityArrayList.add((String) univ_document.getData().get("univ_name"));
                                            }
                                        } else {
                                            Log.v("에러메시지", String.valueOf(task.getException()));
                                        }

                                    } catch (Exception e) {
                                        Log.v("에러메시지", e.getMessage());
                                    }
                                }
                            }); //하위컬렉션 문서 읽어오는 문장 End
                        }
                    } else {
                        Log.v("에러메시지", String.valueOf(task.getException()));
                    }
                } catch (Exception e) {
                    Log.v("에러메시지", e.getMessage());
                }
            }
        });

        areaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, universityArrayList);


        Handler mHandler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog mProgressDialog = ProgressDialog.show(SignupActivity.this,"", "잠시만 기다려 주세요.",true);
                mHandler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mProgressDialog!=null&&mProgressDialog.isShowing()){
                                mProgressDialog.dismiss();
                            }
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }
                    }, 4000);
            }
        }); //스피너 로딩 시간으로 인해 로딩 다이얼로그 추가


        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(areaAdapter);
            }
        }, 2000);


        phone_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference user_reference = db.collection("User");
                user_reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //성공적으로 읽어온 경우
                        try {
                            if (task.isSuccessful()) {
                                int checkCount = 0;
                                int documentCount = 0;
                                for (QueryDocumentSnapshot user_document : task.getResult()) {
                                    String phoneText = sign_phone.getText().toString().trim();
                                    if (user_document.getData().get("phone").equals(phoneText)){
                                        checkCount += 1;
                                    }
                                    if(phoneText.length() < 7){
                                        Toast.makeText(SignupActivity.this, "휴대폰 번호를 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    } else if (checkCount > 0){
                                        Toast.makeText(SignupActivity.this, "이미 존재하는 번호입니다.", Toast.LENGTH_SHORT).show();
                                    } else if (checkCount == 0){
                                        Toast.makeText(SignupActivity.this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        phoneCheck = true;
                                    }
                                    documentCount++;
                                }
                                if(documentCount == 0){
                                    Toast.makeText(SignupActivity.this, "인증에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                    phoneCheck = true;
                                }
                            }
                        } catch (Exception e) {
                            Log.v("에러메시지", e.getMessage());
                        }
                    }
                });
            }
        });

        //이전 버튼 클릭시
        sign_backbtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //회원가입 버튼 클릭시
        signup_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = (Spinner) findViewById(R.id.sign_type);
                Spinner spinner2 = (Spinner) findViewById(R.id.sign_university);
                String emailText = sign_email.getText().toString().trim();
                String passwdText = sign_passwd.getText().toString().trim();
                String passwdConfirmText = sign_passwd_confirm.getText().toString().trim();
                String nicknameText = sign_nickname.getText().toString().trim();
                String phoneText = sign_phone.getText().toString().trim();
                String typeText = spinner.getSelectedItem().toString();
                String universityText = spinner2.getSelectedItem().toString();


                //유효성 중복 체크
                if (!phoneCheck){
                    Toast.makeText(SignupActivity.this, "휴대폰 인증을 진행해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(emailText)) {
                    Toast.makeText(SignupActivity.this, "이메일(아이디)을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwdText)) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwdConfirmText)) {
                    Toast.makeText(SignupActivity.this, "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }  else if (!passwdConfirmText.equals(passwdText)) {
                    Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (!validatePassword(passwdText)) {
                    Toast.makeText(SignupActivity.this, "비밀번호 형식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(nicknameText)) {
                    Toast.makeText(SignupActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phoneText)) {
                    Toast.makeText(SignupActivity.this, "휴대폰번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }  else {
                    try{
                        firebaseAuth.createUserWithEmailAndPassword(emailText, passwdText)
                                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            try {
                                                throw task.getException();
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                Toast.makeText(SignupActivity.this, "이메일 형식이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                            } catch (FirebaseNetworkException e) {
                                                Toast.makeText(SignupActivity.this, "파이어베이스 네트워크 오류입니다.\n 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                Log.v("에러메시지", e.getMessage());
                                                Toast.makeText(SignupActivity.this, "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                                            } //이메일 중복 error

                                    }else{
                                        try {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            Map<String, Object> userMap = new HashMap<>(); //firestore 사용
                                            userMap.put("documnetID", user.getUid()); //사용자 관리하기 위해
                                            userMap.put("email", emailText);
                                            userMap.put("autoLogin", true);
                                            userMap.put("password", passwdText);
                                            userMap.put("nickname", nicknameText);
                                            userMap.put("phone", phoneText);
                                            userMap.put("university", universityText);
                                            userMap.put("type", typeText);
                                            if(typeText == "학생")
                                                userMap.put("permission", 1);
                                            else if(typeText == "집주인")
                                                userMap.put("permission", 0);
                                            userMap.put("state",1);
                                            userMap.put("modify_count",0);
                                            userMap.put("session_date",null);

                                            mStore.collection("User").document(user.getUid()).set(userMap, SetOptions.merge());
                                            finish();
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            return;
                                        }catch (Exception e){
                                            Log.v("error", e.getMessage());
                                        }
                                    }
                                }
                            });
                    }catch(Exception e){
                        Log.v("error", e.getMessage());
                    }

                }
            }

        });
    }

    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }
}