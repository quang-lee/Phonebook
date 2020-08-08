package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phonebook.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SQLiteHelper sqlHelper;
    List<Person> personList;
    AdapterPerson adapterPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        sqlHelper = new SQLiteHelper(this);


        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }else {
            personList= sqlHelper.getAllPesron();
            if(personList.size()==0)
                getcontact();
        }

        personList=sqlHelper.getAllPesron();
        adapterPerson=new AdapterPerson(personList);
        binding.listPhone.setAdapter(adapterPerson);

        binding.addPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog();
            }
        });
        binding.listPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+personList.get(position).getNumber().toString()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

    }
    public void displayAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.customdialog, null);
        final EditText etUsername = alertLayout.findViewById(R.id.et_Username);
        final EditText etNumber = alertLayout.findViewById(R.id.et_Number);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Liên hệ mới");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(),"Thêm không thành công",Toast.LENGTH_LONG);
            }
        });

        alert.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = etUsername.getText().toString();
                String number = etNumber.getText().toString();

                Person person = new Person(user,number);

                sqlHelper.insertPerson(person);

                personList.add(person);
                adapterPerson.notifyDataSetChanged();
                binding.listPhone.setAdapter(adapterPerson);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();


    }
    private void getcontact() {
        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        while (cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            sqlHelper.onInsertToTB(name+"",number+"");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getcontact();
            }
        }
    }
}