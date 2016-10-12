package com.example.administrator.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AppBarLayout barLayout;
    private CollapsingToolbarLayout coordinatorLayout;
    private ImageView imageView;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewpager;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int mOSVersion = Build.VERSION.SDK_INT;
        //沉浸式
        if (mOSVersion >= Build.VERSION_CODES.KITKAT) {
            Log.i("------","11---------------");
            setTranslucentStatus(true);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.i("--------","22---------------");
            int color = getResources().getColor(R.color.yellow);
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);

                color = getResources().getColor(R.color.yellow);
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(this));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }


        if (mOSVersion >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Log.i("------","333---------------");
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }


        setStatusBarDarkMode(true);

        assignViews();
        setToolBar();

        setUpViewPager();


    }

    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }




    /**
     * 设置状态条透明
     *
     * @param on
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 设置状态栏图标和文本是否为深色
     * 注:仅支持小米和魅族
     */
    protected void setStatusBarDarkMode(boolean darkMode) {
        setStatusBarDarkModeForMUI(darkMode);
        setStatusBarDarkModeForMZ(darkMode);
    }

    private void setStatusBarDarkModeForMUI(boolean darkmode) {
        Class<? extends Window> clazz = this.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(this.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setStatusBarDarkModeForMZ(boolean darkmode) {
        Window window = this.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkmode) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (NoSuchFieldException e) {
//                LogUtils.e("MeiZu", "setStatusBarDarkIcon: failed");
            } catch (IllegalAccessException e) {
//                LogUtils.e("MeiZu", "setStatusBarDarkIcon: failed");
            }
        }
    }

    public void setToolBar() {

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("hello world");
//        getSupportActionBar().setSplitBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
//        getSupportActionBar().setStackedBackgroundDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        toolBar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TabFragment(), "选项1");
//        adapter.addFrag(new TabFragment(), "选项2");
//        adapter.addFrag(new TabFragment(), "选项3");
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        coordinatorLayout.setTitleEnabled(false);
        imageView.setBackgroundResource(R.mipmap.icon_luanch_bottom);
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
