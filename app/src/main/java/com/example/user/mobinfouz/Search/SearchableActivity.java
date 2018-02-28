package com.example.user.mobinfouz.Search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.mobinfouz.LoadData;
import com.example.user.mobinfouz.MainActivity;
import com.example.user.mobinfouz.MainAdapter;
import com.example.user.mobinfouz.R;
import com.melnykov.fab.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class SearchableActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    TextView Error;
    int totalItemCount =  20;
    Toolbar mToolbar;
    LinearLayoutManager mManager;
    MainAdapter mAdapter;
    RecyclerView mRecycler;
    TextView Loading;

    String checkQuery = "begin", rememberQuery;
    int page = 1;
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchable);

        Loading = (TextView)findViewById(R.id.loading_text);
        Error = (TextView)findViewById(R.id.error_text);


        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.light));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.basic));
        mToolbar.setNavigationOnClickListener(this);

        mRecycler = (RecyclerView)findViewById(R.id.searchRecycler);
        mRecycler.setLayoutManager(mManager = new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new MainAdapter());

        mFab = (FloatingActionButton)findViewById(R.id.search_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycler.scrollToPosition(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchable, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

            View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            v.setBackgroundColor(getResources().getColor(R.color.basic));
            searchView.onActionViewExpanded();
            searchView.setOnQueryTextListener(this);
            searchView.setQueryHint(getResources().getString(R.string.hint));
            searchView.setIconifiedByDefault(false);
            changeSearchViewTextColor(searchView);

            ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                    .setHintTextColor(getResources().getColor(R.color.light));

            ImageView searchCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
            searchCloseButton.setImageResource(R.drawable.ic_clear);

            View searchBadge = searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
            ((ViewManager)searchBadge.getParent()).removeView(searchBadge);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        if (!query.equals(checkQuery) && !query.equals(rememberQuery)){
            mAdapter.mList.clear();
            mAdapter.notifyDataSetChanged();

            Error.setText("");
            Loading.setText("");

            new LoadSearch(this, query, page, 0).execute();
            rememberQuery = query;
        }

        mFab.attachToRecyclerView(mRecycler, null, new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = mManager.getChildCount();
                int pastVisibleItems = mManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                    totalItemCount += 20;
                    page++;
                    Error.setText("");
                    Loading.setText("");
                    new LoadSearch(SearchableActivity.this, query, page, 1).execute();
                }
            }
        });
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
}
