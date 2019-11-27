package com.example.deudas_ort.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deudas_ort.R;
import com.example.deudas_ort.Utils.RoundImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactAdapter extends BaseAdapter {

    public List<Contact> contacts;
    private ArrayList<Contact> allContacts;
    private Context context;
    private ViewWrapper viewWrapper;
    private RoundImage roundedThumbnailPhoto;

    public ContactAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
        this.allContacts = new ArrayList<>();
        this.allContacts.addAll(this.contacts);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int i) {
        return contacts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View appView, ViewGroup viewGroup) {
        if (appView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            appView = li.inflate(R.layout.contacts_list_view, null);
            Log.e("Inside", "here--------------------------- In view");
        } else {
            Log.e("Inside", "here--------------------------- In other View");
        }

        viewWrapper = new ViewWrapper();

        viewWrapper.fullName = appView.findViewById(R.id.fullName);
        viewWrapper.phone = appView.findViewById(R.id.phone);
        viewWrapper.imageView = appView.findViewById(R.id.photo);

        final Contact data = contacts.get(i);

        viewWrapper.fullName.setText(data.getName());
        viewWrapper.phone.setText(data.getPhone());

        /** Set contact or default photo */
        try {
            /** Loading default photo */
            Bitmap photoThumbnailBitMap = BitmapFactory.decodeResource(appView.getResources(), R.drawable.image);
            if (data.getBitMap() != null) {
                photoThumbnailBitMap = data.getBitMap();
            }

            /** Setting round photo */
            roundedThumbnailPhoto = new RoundImage(photoThumbnailBitMap);
            viewWrapper.imageView.setImageDrawable(roundedThumbnailPhoto);
        } catch (OutOfMemoryError e) {
            // Add default picture
            viewWrapper.imageView.setImageDrawable(this.context.getDrawable(R.drawable.image));
            e.printStackTrace();
        }

        Log.e("Image Thumb", "--------------" + data.getBitMap());


        appView.setTag(data);
        return appView;
    }

    /** Search */
    public void search(String searchString) {
        final Locale defaultLocale = Locale.getDefault();
        searchString = searchString.toLowerCase(defaultLocale);
        contacts.clear();

        if (searchString.length() == 0) {
            contacts.addAll(allContacts);
        } else {
            for (Contact contact : allContacts) {
                if (contact.getName().toLowerCase(defaultLocale).contains(searchString)) {
                    contacts.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewWrapper {
        ImageView imageView;
        TextView fullName;
        TextView phone;
    }
}
