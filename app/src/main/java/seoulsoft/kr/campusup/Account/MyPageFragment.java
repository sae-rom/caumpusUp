package seoulsoft.kr.campusup.Account;

import seoulsoft.kr.campusup.R;
import seoulsoft.kr.campusup.Roomout.AddressFindActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class MyPageFragment extends Fragment {
    View view;
    private FirebaseUser authUser;
    private FirebaseFirestore mStore;

    private String nickname;
    private TextView profile_modify, mypage_nickname, kakao_textView, mypage_roomout_textView;
    private ImageView kakao_imageView, mypage_roomout_ImageView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.mypage_fragment,container,false);

        //받기
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sFile", getActivity().MODE_PRIVATE);
        final String nickname = sharedPreferences.getString("nickname", null);

        //SharedPreferences 파일에 회원정보 저장
        final SharedPreferences preferences = getActivity().getSharedPreferences("sFile", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mypage_roomout_textView = view.findViewById(R.id.mypage_roomout_textView);
        mypage_roomout_ImageView = view.findViewById(R.id.mypage_roomout_imageView);
        mypage_nickname = view.findViewById(R.id.mypage_nickname);
        profile_modify = view.findViewById(R.id.profile_modify);
        kakao_imageView = view.findViewById(R.id.kakao_imageView);
        kakao_textView = view.findViewById(R.id.kakao_textView);

        authUser = FirebaseAuth.getInstance().getCurrentUser(); //로그인된 유저 정보 담는 변수
        mStore = FirebaseFirestore.getInstance();



        if (authUser != null){
            mStore.collection("User").document(authUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult() != null){
                            mypage_nickname.setText(nickname);

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
        }

        mypage_roomout_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressFindActivity.class);
                startActivity(intent);
            }
        });

        mypage_roomout_ImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressFindActivity.class);
                startActivity(intent);
            }
        });

        profile_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MypageDetailActivity.class);
                startActivity(intent);
            }
        });

        kakao_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/s45ZOi8c")));
            }
        });

        kakao_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/s45ZOi8c")));
            }
        });


        return view;

    }
}
