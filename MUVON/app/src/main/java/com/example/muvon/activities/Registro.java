package com.example.muvon.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.muvon.R;
import com.example.muvon.fragments.FragmentNuevoUsuario;

public class Registro extends AppCompatActivity {

    FragmentNuevoUsuario fragmentNuevoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        fragmentNuevoUsuario = new FragmentNuevoUsuario();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.activity_registro, fragmentNuevoUsuario).commit();

    }
}
