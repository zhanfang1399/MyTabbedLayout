package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AppBarLayout barLayout;
    private CollapsingToolbarLayout coordinatorLayout;
    private ImageView imageView;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        assignViews();

        setToolBar();

        setUpViewPager();



    }



    public void setToolBar(){

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("hello world");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public void setUpViewPager(){
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TabFragment(),"选项1");
        adapter.addFrag(new TabFragment(),"选项2");
        adapter.addFrag(new TabFragment(),"选项3");
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        coordinatorLayout.setTitleEnabled(false);
    }



    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    private void assignViews() {
        barLayout = (AppBarLayout) findViewById(R.id.barLayout);
        coordinatorLayout = (CollapsingToolbarLayout) findViewById(R.id.coordinatorLayout);
        imageView = (ImageView) findViewById(R.id.imageView);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }

}
