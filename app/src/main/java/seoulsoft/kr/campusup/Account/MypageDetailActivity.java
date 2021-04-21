package seoulsoft.kr.campusup.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seoulsoft.kr.campusup.R;

public class MypageDetailActivity extends AppCompatActivity {
    public static final Pattern VALID_PASSWOLD_REGEX_ALPHA_NUM = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,20}$"); // 8자리 ~ 20자리까지 가능
    private FirebaseUser authUser;
    private FirebaseFirestore mStore;

    private EditText mypage_detail_nickname_input, mypage_detail_id_input, mypage_detail_univ_input;
    private EditText current_psw_editText, change_psw_editText, change_psw_editText2, phone_number_editText;
    private Switch autologin_switch;

    private TextView current_psw_confirm, change_psw_confirm, change_psw_confirm2;

    private Boolean isAuto = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_detail);

        authUser = FirebaseAuth.getInstance().getCurrentUser(); //로그인된 유저 정보 담는 변수
        mStore = FirebaseFirestore.getInstance();

        mypage_detail_nickname_input = findViewById(R.id.mypage_detail_nickname_input);
        mypage_detail_id_input = findViewById(R.id.mypage_detail_id_input);
        mypage_detail_univ_input = findViewById(R.id.mypage_detail_univ_input);
        current_psw_editText = findViewById(R.id.current_psw_editText);
        change_psw_editText = findViewById(R.id.change_psw_editText);
        change_psw_editText2 = findViewById(R.id.change_psw_editText2);
        phone_number_editText = findViewById(R.id.phone_number_editText);
        autologin_switch = findViewById(R.id.autologin_switch);

        current_psw_confirm = findViewById(R.id.current_psw_confirm);
        change_psw_confirm = findViewById(R.id.change_psw_confirm);
        change_psw_confirm2 = findViewById(R.id.change_psw_confirm2);

        //받기
        SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
        final String nickname = sharedPreferences.getString("nickname", null);
        final String email = sharedPreferences.getString("email", null);
        final String password = sharedPreferences.getString("password", null);
        final String phonenumber = sharedPreferences.getString("phonenumber", null);
        final String university = sharedPreferences.getString("university", null);

        //현재 비밀번호 맞는지 확인하는 textView
        current_psw_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                current_psw_confirm.setText("불일치");
                current_psw_confirm.setTextColor(Color.parseColor("#D7020D"));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(password.equals(charSequence.toString())){
                    current_psw_confirm.setText("일치");
                    current_psw_confirm.setTextColor(Color.parseColor("#84A765"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //비밀번호 변경한 것 확인하는 textView
        change_psw_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                change_psw_confirm.setText("형식 오류");
                change_psw_confirm.setTextColor(Color.parseColor("#D7020D"));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((validatePassword(charSequence.toString()))){
                    change_psw_confirm.setText("확인");
                    change_psw_confirm.setTextColor(Color.parseColor("#84A765"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //비밀번호 변경한 것 재확인하는 textView
        change_psw_confirm2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                change_psw_confirm2.setText("불일치");
                change_psw_confirm2.setTextColor(Color.parseColor("#D7020D"));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(change_psw_editText.getText().toString().equals(change_psw_confirm2.getText().toString())){
                    change_psw_confirm2.setText("일치");
                    change_psw_confirm2.setTextColor(Color.parseColor("#84A765"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //유저정보 받아왔는지 확인 후 넘겨받은 값을 넣어줌
        if (authUser != null){
            mStore.collection("User").document(authUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult() != null){
                            mypage_detail_nickname_input.setText(nickname);
                            mypage_detail_id_input.setText(email);
                            mypage_detail_univ_input.setText(university);

                        }
                    }
                }});
        }


        autologin_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                } else{
                }
            }
        });
    }
    public static boolean validatePassword(String pwStr) {
        Matcher matcher = VALID_PASSWOLD_REGEX_ALPHA_NUM.matcher(pwStr);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}