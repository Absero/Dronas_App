package com.puslapis.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.puslapis.myapplication.databinding.MainBinding;

public class Main extends AppCompatActivity {

    public ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.main);

        mViewModel = new ViewModel(this);
        mainBinding.setViewModel(mViewModel);
    }

    public void Button_Click(View view) {
        mViewModel.mMatomas.set(!mViewModel.mMatomas.get());
    }
}