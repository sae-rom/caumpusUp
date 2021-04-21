package seoulsoft.kr.campusup.Roomout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import seoulsoft.kr.campusup.R;

public class Roomout1Fragment extends Fragment {
    ViewGroup viewGroup;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_roomout1,container,false);
        return viewGroup;
    }
}
