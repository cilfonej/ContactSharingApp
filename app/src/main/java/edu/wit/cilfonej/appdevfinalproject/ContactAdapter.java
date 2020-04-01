package edu.wit.cilfonej.appdevfinalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;

    public ContactAdapter(List<Contact> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Contact ci = contactList.get(i);

        if(!ci.isValid()) {
            contactViewHolder.title.setText("Invalid Contact!");

            contactViewHolder.vName.setText("");
            contactViewHolder.vSurname.setText("");
            contactViewHolder.vEmail.setText("");
            contactViewHolder.vAddress.setText("");
            return;
        }

        contactViewHolder.vName.setText(ci.getFirstName());
        contactViewHolder.vSurname.setText(ci.getLastName());
        contactViewHolder.vEmail.setText(ci.getEmail());
        contactViewHolder.vAddress.setText(ci.getAddress());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);


        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;

        protected TextView vName;
        protected TextView vSurname;
        protected TextView vEmail;
        protected TextView vAddress;

        public ContactViewHolder(View v) {
            super(v);

            title =  (TextView) v.findViewById(R.id.title);

            vName =  (TextView) v.findViewById(R.id.txtName);
            vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
            vAddress = (TextView) v.findViewById(R.id.txtAdd);
        }
    }
}