package com.agmcs.ssuussee.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.agmcs.ssuussee.R;

public class LibrarySearchActivity extends AppCompatActivity {
    String query_data;
    public static final String SEARCH_DATA = "search_data";
    Toolbar toolbar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libary_search);
        recyclerView = (RecyclerView)findViewById(R.id.libray_searchRecyclerView);

        toolbar = (Toolbar)findViewById(R.id.libray_search_toolbar);
        Intent data = getIntent();
        if(data != null){
            query_data = data.getStringExtra(SEARCH_DATA);
            toolbar.setTitle(query_data);
        }else{
            finish();
        }
        Toast.makeText(LibrarySearchActivity.this,query_data,Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_libary_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
