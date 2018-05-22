package com.example.tipix1998.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.screen_pop_up_registro)
public class popUpRegistro extends DialogFragment {

    @Click(R.id.btnReturn)
    void btnReturn(){
        Intent intent = new Intent( getActivity(), MainActivity_.class );
        startActivity( intent );
    }

}

