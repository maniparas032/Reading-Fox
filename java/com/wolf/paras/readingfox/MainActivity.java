package com.wolf.paras.readingfox;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    String BOOK_STORE_URL = "https://www.googleapis.com/books/v1/volumes?q=android";
    public static String search;
    private static final int BOOK_LOADER_ID = 1;

    BookAdapter mAdapter;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView bookList = (ListView) findViewById(R.id.list);
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookList.setAdapter(mAdapter);

        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        bookList.setEmptyView(mEmptyTextView);
        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getSupportLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);






        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book current =(Book) bookList.getItemAtPosition(position);
                String url = current.getPreviewLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });






    }



    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        Log.e(LOG_TAG, "Error here");

        return new BookLoader(this, BOOK_STORE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        progressBar.setVisibility(View.INVISIBLE);

        if (isConnected == false) {
            mEmptyTextView.setText("Oops!! No Connectivity ");
        } else {
            mEmptyTextView.setText("No Book Found");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        MenuItem searchItem = menu.findItem(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(LOG_TAG,query);

                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);
                String[] textSelect = query.split(" ");
                String newstr = "";
                // Log.e(LOG_TAG,textSelect[0]);
                BOOK_STORE_URL = "https://www.googleapis.com/books/v1/volumes?q=" + textSelect[0];
                for (int i = 1; i < textSelect.length; i++)
                    newstr = newstr + "%20" + textSelect[i];

                BOOK_STORE_URL = BOOK_STORE_URL + newstr;

                restartLoader();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(LOG_TAG,newText);
                return false;
            }
        });
        return true;
    }


}


