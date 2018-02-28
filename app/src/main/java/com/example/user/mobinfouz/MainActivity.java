package com.example.user.mobinfouz;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mobinfouz.Search.SearchableActivity;
import com.example.user.mobinfouz.Shopping.ShoppingActivity;
import com.melnykov.fab.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    final String[] section_code = {"category/novosti", "category/novinki", "category/ratings",
            "tag/сравнение", "category/obzory","category/prilozheniya", "tag/poleznaya",
         "category/seny", "tag/beeline"};
    RecyclerView mRecycler;
    MainAdapter mAdapter;
    LinearLayoutManager mManager;
    NavigationView mNavigation;
    Toolbar mToolbar;
    SwipeRefreshLayout mSwipe;
    ProgressBar mProgress;
    FloatingActionButton mFab;
    int totalItemCount =  20;
    TextView Error_Text;
    int current_page = 1;
    String section = "category/novosti";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Error_Text = (TextView)findViewById(R.id.error_text);

        mProgress = (ProgressBar)findViewById(R.id.progress_bar);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Mobinfo.uz");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.light));
        mToolbar.setNavigationIcon(R.drawable.menu_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout mDrawable = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawable.openDrawer(mNavigation);
            }
        });
        mToolbar.setBackgroundColor(getResources().getColor(R.color.basic));


        mRecycler = (RecyclerView)findViewById(R.id.mainRecycler);
        mRecycler.setLayoutManager(mManager = new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new MainAdapter());
        mRecycler.setBackgroundColor(getResources().getColor(R.color.background));

        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.attachToRecyclerView(mRecycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mManager.getChildCount();
                int pastVisibleItems = mManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    totalItemCount += 20;
                    current_page++;
                    new LoadData(MainActivity.this, section, current_page, 1).execute();
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycler.scrollToPosition(0);
            }
        });

        mSwipe = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipe.setColorSchemeColors(getResources().getColor(R.color.basic), getResources().getColor(R.color.secondary));
        mSwipe.setOnRefreshListener(this);

        mNavigation = (NavigationView)findViewById(R.id.navigation_drawer);
        mNavigation.setBackgroundColor(getResources().getColor(R.color.light));
        mNavigation.setNavigationItemSelectedListener(this);
        mNavigation.setPadding(0, 72, 0 , 0);

        new LoadData(MainActivity.this, section, current_page, 0).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.start_search) {
            Intent intent = new Intent(this, SearchableActivity.class);
            startActivity(intent);
        }
        if (id == R.id.start_shopping){
            Intent intent = new Intent(this, ShoppingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mSwipe.setRefreshing(true);
        mSwipe.postDelayed(new Runnable() {
            @Override
            public void run() {
                current_page = 1;
                totalItemCount = 20;
                mAdapter.mList.clear();
                mAdapter.notifyDataSetChanged();
                new LoadData(MainActivity.this, section, current_page, 1).execute();
                mSwipe.setRefreshing(false);
            }
        }, 3000);
    }

    public int getPosition(MenuItem menuItem){
        int answer = 0;
        Menu menu = mNavigation.getMenu();
        for (int i = 0; i < 11; i++){
            if (menu.getItem(i) == menuItem){
                answer = i;
            }
        }
        return answer;
    }

    @Override
    public void onClick(View v) {
        DrawerLayout mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.openDrawer(mNavigation);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        DrawerLayout mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawer.closeDrawer(mNavigation);
        int position = getPosition(menuItem);
        if (position < 9){
            current_page = 1;
            totalItemCount = 20;
            section = section_code[position];
            mAdapter.mList.clear();
            mAdapter.notifyDataSetChanged();
            new LoadData(MainActivity.this, section, current_page, 0).execute();
        }else if (position == 9){
            Intent intent = new Intent(MainActivity.this, Contacts.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(MainActivity.this, Advertisement.class);
            startActivity(intent);
        }


        Menu menu = mNavigation.getMenu();
        for (int i = 0; i < 11; i++){
            if (menu.getItem(i) != menuItem && menu.getItem(i).isChecked()){
                menu.getItem(i).setChecked(false);
            }
        }
        menuItem.setChecked(true);
        return true;
    }
}
