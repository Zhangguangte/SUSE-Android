package com.agmcs.ssuussee.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by agmcs on 2015/5/24.
 */
public class SetCurrentWeekDialog extends DialogFragment {
    public static final int WRONG_RESULT = 0;
    public static final int SUCCES_RESULT = 1;
    private int current_choice = -1;
    public static final String CHOICE  = "choice";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] week = new String[]{"第1周","第2周","第3周","第4周","第5周","第6周","第7周","第8周","第9周",
                "第10周","第11周","第12周","第13周","第14周","第15周","第16周","第17周","第18周","第19周",
                "第20周","第21周","第22周","第23周","第24周","第25周"};
        final ListView listView = new ListView(getActivity());
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,week));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current_choice = position + 1;
                if (current_choice > 0) {
                    setResult();
                }
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("设置当前周")
                .setView(listView)
                .create();
    }

    private void setResult(){
        if(getTargetFragment() == null){
            return;
        }
        Intent i = new Intent();
        i.putExtra(CHOICE, current_choice);
        getTargetFragment().onActivityResult(getTargetRequestCode(),SUCCES_RESULT, i);
        this.dismiss();
    }
}
