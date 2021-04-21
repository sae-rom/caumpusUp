package seoulsoft.kr.campusup.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import seoulsoft.kr.campusup.Account.BackPressHandler;
import seoulsoft.kr.campusup.Account.HomeFragment;
import seoulsoft.kr.campusup.Account.MapFragment;
import seoulsoft.kr.campusup.Account.ClassFragment;
import seoulsoft.kr.campusup.Account.CommunityFragment;
import seoulsoft.kr.campusup.Account.MyPageFragment;
import seoulsoft.kr.campusup.Model.User;
import seoulsoft.kr.campusup.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private MapFragment mapFragment;
    private ClassFragment classFragment;
    private CommunityFragment communityFragment;
    private MyPageFragment myPageFragment;

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    BottomNavigationView bottomNavigationView;

    private BackPressHandler backPressHandler = new BackPressHandler(MainActivity.this);

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigationview);

        fragmentManager = getSupportFragmentManager();

        //fragment 생성
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        classFragment = new ClassFragment();
        communityFragment = new CommunityFragment();
        myPageFragment = new MyPageFragment();


        //제일 처음 띄워줄 화면을 설정 (commit(); 까지 선언해주어야함)
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();

        //bottomNavigationview의 아이콘을 선택했을 때 원하는 fragment 화면이 뜰 수 있도록 추가
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.home: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.map: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, mapFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.circles: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, classFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.community: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, communityFragment).commitAllowingStateLoss();
                        return true;
                    }
                    case R.id.mypage: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_layout, myPageFragment).commitAllowingStateLoss();
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);
    }


}