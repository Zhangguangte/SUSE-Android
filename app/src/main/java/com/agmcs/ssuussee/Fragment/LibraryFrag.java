package com.agmcs.ssuussee.Fragment;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.agmcs.ssuussee.Activity.LibrarySearchActivity;
import com.agmcs.ssuussee.Adapter.LibraryAdapter;
import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NetUtils;
import com.agmcs.ssuussee.Util.TsgBookBiz;
import com.agmcs.ssuussee.Util.TsgUtil;
import com.agmcs.ssuussee.db.Dao;
import com.agmcs.ssuussee.model.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFrag extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.library_recyclerView)
    RecyclerView library_recyclerView;

    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    public static final int LOGIN_REQUEST_CODE = 0;

    private LibraryAdapter adapter;
    private List<Book> book_list = new ArrayList<>();
    private Dao dao = Dao.getInstanse(getActivity());
    private TsgUtil tsgUtil = null;

    private static final int REFRESH = 0;
    private static final int FIRST_REFRESH = 1;
    BaseApplication app = null;


    public LibraryFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        ButterKnife.inject(this, view);
        app = (BaseApplication)getActivity().getApplication();
        String password = app.getPassword_tsg();
        String username = app.getStudent_num_tsg();
        if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(password)){
            tsgUtil = new TsgUtil(username,password);
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        book_list = app.getBookList();

        adapter = new LibraryAdapter(book_list);
        adapter.setRenewClickListener(new LibraryAdapter.RenewClickListener() {
            @Override
            public void onClick(View view, int position, final String url) {
                if (NetUtils.checkNet(getActivity())) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("是否续借")
                            .setPositiveButton("续借", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (tsgUtil != null) {
                                        new AsyncReNewTask().execute(url);
                                    }
                                }
                            })
                            .setNegativeButton("下次", null)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "你没联网啊~~", Toast.LENGTH_SHORT).show();
                }
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        library_recyclerView.setLayoutManager(layoutManager);
        library_recyclerView.setAdapter(adapter);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1,0.8f,1,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f,1.0f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(200);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(animationSet);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
        library_recyclerView.setLayoutAnimation(layoutAnimationController);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.library_menu, menu);
        MenuItem loginItem = menu.findItem(R.id.libary_login);
        MenuItem logoutItem = menu.findItem(R.id.libary_logout);

        if(tsgUtil == null){//未登陆
            loginItem.setVisible(true);
            logoutItem.setVisible(false);
        }else{
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        }

        MenuItem searchItem = menu.findItem(R.id.libary_search);
        searchItem.setVisible(false);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("馆藏查询");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), LibrarySearchActivity.class);
                intent.putExtra(LibrarySearchActivity.SEARCH_DATA,query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    @Override
    public void onRefresh() {
        if(NetUtils.checkNet(getActivity())){
            new AsyncRefreshTask().execute(REFRESH);
        }else {
            Toast.makeText(getActivity(), "你没联网啊~~", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    //从网络获取
    class AsyncRefreshTask extends AsyncTask<Integer,Void,Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            refresh_data();
            return null;
        }

        @Override
        protected void onPostExecute(Void void0) {
            super.onPostExecute(void0);
            adapter.reAdd(book_list);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.libary_login:
                LoginFragmentDialog dialog = LoginFragmentDialog.newInstance(LoginFragmentDialog.TSG_DIALOG);

                dialog.setTargetFragment(this,LOGIN_REQUEST_CODE);
                dialog.show(getFragmentManager(), "TSG_LOGIN");

                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.libary_logout:
                tsgUtil = null;

                app.savePassword_tsg("");
                app.saveStudent_num_tsg("");
                dao.deleteAllBook();
                book_list.clear();

                adapter.reAdd(book_list);
                adapter.notifyDataSetChanged();

                getActivity().invalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh_data() {
        Log.d("hihi1995", "REFRESHDATA");
        List<Book> list;
        if(tsgUtil == null){
            return;
        }
        book_list = new TsgBookBiz().getBookList(tsgUtil);
        dao.deleteAllBook();
        dao.addBookList(book_list);
    }

    class AsyncReNewTask extends AsyncTask<String,Void,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return tsgUtil.reNew(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result){
                Toast.makeText(getActivity(), "续借成功", Toast.LENGTH_SHORT).show();
                new AsyncRefreshTask().execute();
            }else{
                Toast.makeText(getActivity(), "已达到续借次数上限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case LoginFragmentDialog.SUCCES_RESULT:
                final String user = data.getStringExtra(LoginFragmentDialog.USERNAME);
                final String password = data.getStringExtra(LoginFragmentDialog.PASSWORD);
                tsgUtil = new TsgUtil(user,password);

                new AsyncTask<Void,Void,String>(){
                    @Override
                    protected String doInBackground(Void... params) {
                        return tsgUtil.login();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if(s == null){
                            Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            tsgUtil = null;
                        }else{
                            app.savePassword_tsg(password);
                            app.saveStudent_num_tsg(user);
                            getActivity().invalidateOptionsMenu();
                            new AsyncRefreshTask().execute(REFRESH);
                        }
                    }
                }.execute();
                break;
        }
    }

}
