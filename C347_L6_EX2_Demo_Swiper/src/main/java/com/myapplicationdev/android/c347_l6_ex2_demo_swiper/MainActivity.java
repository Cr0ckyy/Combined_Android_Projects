package com.myapplicationdev.android.c347_l6_ex2_demo_swiper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // TODO : In the same way that ListView requires relevant components,
    ArrayList<Fragment> fragments;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    FragmentManager fragmentManager;
    ViewPager viewPager;
    Button btnBack, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewpager1);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);


        // TODO : Create an ArrayList collection of Fragments.
        fragments = new ArrayList<>();
        fragments.add(new Frag1());
        fragments.add(new Frag2());

        // TODO : Pass the FragmentManager and the collection of Fragments to the Adapter.
        fragmentManager = getSupportFragmentManager();
        // set fragments and fragmentManager into the MyFragmentPagerAdapter
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragments);
        viewPager.setAdapter(myFragmentPagerAdapter);


        btnBack.setOnClickListener((View v) -> {
            if (viewPager.getCurrentItem() > 0) {
                int previousPage = viewPager.getCurrentItem() - 1;
                viewPager.setCurrentItem(previousPage, true);
            }
        });

        btnNext.setOnClickListener((View v) -> {
            int max = viewPager.getChildCount();
            if (viewPager.getCurrentItem() < max - 1) {
                int nextPage = viewPager.getCurrentItem() + 1;
                viewPager.setCurrentItem(nextPage, true);
            }
        });


    }

}

