package com.example.aplikasikalkulatorandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//Bagus Surya Mahendra
//A11.2021.13315
public class MainActivity extends AppCompatActivity {

    Spinner spinnerOperasi;
    RecyclerView record;
    Gson gerson;
    SharedPreferences pref;
    EditText Input1, Input2;
    TextView operator, outputHasil;
    ArrayList<item> recordList, ListInit;
    String jsonStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = this.getSharedPreferences(getString(R.string.shared_pref), Context.MODE_PRIVATE);
        gerson = new GsonBuilder().create();

        operator = findViewById(R.id.tandaOP);
        outputHasil = findViewById(R.id.hasil);

        Input1 = findViewById(R.id.input1);
        Input2 = findViewById(R.id.input2);

        spinnerOperasi = findViewById(R.id.spinnerOperasi);
        spinnerOperasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = (String) adapterView.getItemAtPosition(i);
                if (temp == "Tambah (+)"){
                    operator.setText("+");
                }else if(temp == "Kurang (-)"){
                    operator.setText("-");
                }else if(temp == "Kali (x)"){
                    operator.setText("x");
                }else{
                    operator.setText(":");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                operator.setText("");
            }
        });

        ArrayList <String> listOperasi = new ArrayList<>();
        listOperasi.add("Tambah (+)");
        listOperasi.add("Kurang (-)");
        listOperasi.add("Kali (x)");
        listOperasi.add("Bagi (:)");

        ArrayAdapter iniAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOperasi);
        spinnerOperasi.setAdapter(iniAdapter);

        jsonStr = pref.getString(getString(R.string.data_simpanan), "[]");
        ListInit = gerson.fromJson(jsonStr, new TypeToken<ArrayList<item>>(){}.getType());
        if (ListInit == null) ListInit = new ArrayList<item>();


        record = findViewById(R.id.record);
        record.setAdapter(new AdapterRecycleView(ListInit, this));
        record.setLayoutManager(new LinearLayoutManager(this));

    }

    public void buttonEnter(View v){
        int Angka1, Angka2;
        try {
            Angka1 = Integer.parseInt(Input1.getText().toString());
            Angka2 = Integer.parseInt(Input2.getText().toString());
        }catch (Exception e){
            Toast.makeText(this,"Pastikan input yang dimasukkan benar!", Toast.LENGTH_SHORT).show();

            return;

        }
        String pilihan = (String) spinnerOperasi.getSelectedItem();

        int hasil;
        String op;
        if (pilihan == "Tambah (+)"){
            hasil = Angka1 + Angka2;
            op = "+";
            outputHasil.setText(Integer.toString(hasil));
        }else if(pilihan == "Kurang (-)"){
            hasil = Angka1 - Angka2;
            op = "-";
            outputHasil.setText(Integer.toString(hasil));
        }else if(pilihan == "Kali (x)"){
            hasil = Angka1*Angka2;
            op = "x";
            outputHasil.setText(Integer.toString(hasil));
        }else{
            hasil = Angka1/Angka2;
            op = ":";
            outputHasil.setText(Integer.toString(hasil));
        }

        String temp2 = pref.getString(getString(R.string.data_simpanan), "[]");
        recordList = gerson.fromJson(temp2, new TypeToken<ArrayList<item>>(){}.getType());
        if (temp2 == null) recordList = new ArrayList<item>();

        recordList.add(new item(Angka1, Angka2, op, hasil));

        temp2 = gerson.toJson(recordList);

        pref.edit().putString(getString(R.string.data_simpanan), temp2).apply();

        //mengubah tampilan saat diklik
        record = findViewById(R.id.record);
        record.setAdapter(new AdapterRecycleView(recordList, this));
        record.setLayoutManager(new LinearLayoutManager(this));

        // menyembunyikan keyboard
        hideKeyboard(this);

    }


    private static void hideKeyboard(Activity activity) {  // kode dari github buat menyembunyikan keyboard
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}