package com.agmcs.ssuussee.Fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import android.widget.Toast;

import com.agmcs.ssuussee.Activity.Login_JwActivity;
import com.agmcs.ssuussee.Adapter.GradesAdapter;
import com.agmcs.ssuussee.BaseApplication;
import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NetUtils;
import com.agmcs.ssuussee.model.Grade;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GradeFrag extends android.support.v4.app.Fragment {
    public static final int LOGIN_REQUEST_CODE = 2;

    @InjectView(R.id.grade_recycler_view)
    RecyclerView gradeView;

    List<Grade> gradeList = null;

    GradesAdapter adapter;

    BaseApplication app;


    public GradeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        app = (BaseApplication)getActivity().getApplication();
        gradeList = app.getGradeList();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        gradeView.setLayoutManager(layoutManager);
        adapter = new GradesAdapter(gradeList);
        gradeView.setItemAnimator(new DefaultItemAnimator());
        gradeView.setAdapter(adapter);
        gradeView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_grade,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.grade_login:
                if(NetUtils.checkNet(getActivity())){
                    Intent i = new Intent(getActivity(), Login_JwActivity.class);
                    i.putExtra(Login_JwActivity.TYPE_KEY, Login_JwActivity.TYPE_GRADE);
                    startActivityForResult(i, LOGIN_REQUEST_CODE);
                }else{
                    Toast.makeText(getActivity(), "你没联网啊~~", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Login_JwActivity.READ_SUCCESS:
                refreshGrade();
                break;
        }
    }

    private void refreshGrade(){
        gradeList = app.getGradeList();
        adapter.reAdd(gradeList);
    }}
