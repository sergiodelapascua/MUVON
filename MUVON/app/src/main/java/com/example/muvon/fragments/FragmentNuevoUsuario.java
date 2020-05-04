package com.example.muvon.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.muvon.R;

public class FragmentNuevoUsuario extends Fragment {

    public FragmentNuevoUsuario() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIntanceState){

        View view = inflater.inflate(R.layout.nuevo_usuario, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        /*try {
            callback = context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" deberia implementar el interfaz OnChampionSent");
        }*/
    }

}
