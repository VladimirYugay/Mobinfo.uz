package com.example.user.mobinfouz.Shopping;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.mobinfouz.R;

public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    Toolbar mToolbar;
    RecyclerView mRecycler;
    ShoppingAdapter mAdapter;
    LinearLayoutManager mManager;
    ProgressBar mProgress;
    TextView mError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        mProgress = (ProgressBar)findViewById(R.id.shop_progress);
        mProgress.setVisibility(View.GONE);

        mError = (TextView)findViewById(R.id.error_text);
        mError.setVisibility(View.GONE);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.light));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.basic));
        mToolbar.setNavigationOnClickListener(this);

        mRecycler = (RecyclerView)findViewById(R.id.shop_recycler);
        mRecycler.setLayoutManager(mManager = new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new ShoppingAdapter());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);

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
            searchView.setQueryHint(getResources().getString(R.string.hint_sale));
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
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onBackPressed();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.mList.clear();
        mAdapter.notifyDataSetChanged();
        new LoadShopping(ShoppingActivity.this, query).execute();
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
