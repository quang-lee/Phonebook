package com.example.phonebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.phonebook.databinding.ItemPersonBinding;

import java.util.List;

public class AdapterPerson extends BaseAdapter {

    List<Person> personList;
    ItemPersonBinding binding;

    public AdapterPerson(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_person,parent,false);

        Person person = personList.get(position);

        binding.tvName.setText(person.getName());
        binding.tvNumber.setText(person.getNumber());

        return view;
    }
}
