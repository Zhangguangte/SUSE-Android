package com.agmcs.ssuussee.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agmcs.ssuussee.R;
import com.agmcs.ssuussee.Util.NetUtils;

/**
 * Created by agmcs on 2015/5/22.
 */
public class LoginFragmentDialog extends android.support.v4.app.DialogFragment{


    public static final int TSG_DIALOG = 0;
    public static final int JW_DIALOG = 1;
    private static final String DIALOG_TYPE = "dialog_type";


    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";


    public static final int WRONG_RESULT = 0;
    public static final int SUCCES_RESULT = 1;

    LinearLayout layoutView;
    EditText student_numEdit;
    EditText passwordEdit;


    private int dialog_type;


    public LoginFragmentDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            dialog_type = getArguments().getInt(DIALOG_TYPE);
        }
    }

    public static LoginFragmentDialog newInstance(int dialogType){
        LoginFragmentDialog dialog = new LoginFragmentDialog();
        Bundle args = new Bundle();
        args.putInt(DIALOG_TYPE, dialogType);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        layoutView = (LinearLayout) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.login_dialog_layout, null);
        student_numEdit = (EditText)layoutView.findViewById(R.id.login_student_num);
        passwordEdit = (EditText)layoutView.findViewById(R.id.login_dialog_password);
        Toast.makeText(getActivity(),"密码默认和学号一样",Toast.LENGTH_SHORT).show();

        return new AlertDialog.Builder(getActivity())
                .setView(layoutView)
                .setPositiveButton("Log in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String password = passwordEdit.getText().toString();
                        String student_num = student_numEdit.getText().toString();
                        if(NetUtils.checkNet(getActivity())){
                            if(!TextUtils.isEmpty(password) || !TextUtils.isEmpty(student_num)){
                                if(dialog_type == JW_DIALOG){

                                }else{
                                    setResult(SUCCES_RESULT, student_num, password);
                                }
                            }
                        }else{
                            Toast.makeText(getActivity(), "你没联网啊~~", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }


    private void setResult(int result, @Nullable String username, @Nullable String password){
        if(getTargetFragment() ==  null){
            return;
        }
        Intent i = new Intent();
        if(result == SUCCES_RESULT){
            i.putExtra(USERNAME, username);
            i.putExtra(PASSWORD, password);
            getTargetFragment().onActivityResult(getTargetRequestCode(),SUCCES_RESULT, i);
        }else if(result == WRONG_RESULT){
            getTargetFragment().onActivityResult(getTargetRequestCode(),WRONG_RESULT, i);
        }else{
            return;
        }
    }


}
