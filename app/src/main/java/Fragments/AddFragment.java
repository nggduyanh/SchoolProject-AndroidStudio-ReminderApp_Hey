package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hey.R;


public class AddFragment extends Fragment {

    private int mode;

    public AddFragment() {

    }

    public AddFragment(int mode){
        this.mode =mode;
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    public static AddFragment newInstance(int mode) {

        AddFragment fragment = new AddFragment(mode);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add, container, false);
        return v;
    }

}