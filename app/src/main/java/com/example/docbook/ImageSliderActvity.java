package com.example.docbook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderActvity extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    private List<Integer> mData = new ArrayList<>();
    private ViewPagerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider_actvity);
                mViewPager2 = findViewById(R.id.viewPager2);

                mData.add(R.drawable.health1);
                mData.add(R.drawable.health2);
                mData.add(R.drawable.health3);

                mAdapter = new ViewPagerAdapter(mData, mViewPager2);
                mViewPager2.setAdapter(mAdapter);
            }
        }