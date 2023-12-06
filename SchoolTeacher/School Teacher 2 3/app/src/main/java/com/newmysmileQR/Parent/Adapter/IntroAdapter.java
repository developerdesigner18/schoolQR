package com.newmysmileQR.Parent.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewpager.widget.PagerAdapter;

import com.newmysmileQR.R;
import com.newmysmileQR.Utility.Config;
import com.newmysmileQR.Utility.Preference;

public class IntroAdapter extends PagerAdapter {

    private Context context;

    public IntroAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout;
        switch (position) {
            case 0: {
                layout = (ViewGroup) inflater.inflate(R.layout.step1, container, false);
                break;
            }
            case 1: {
                layout = (ViewGroup) inflater.inflate(R.layout.step2, container, false);
                break;
            }
            case 2: {
                layout = (ViewGroup) inflater.inflate(R.layout.step3, container, false);
                AppCompatCheckBox cbShow = layout.findViewById(R.id.cbShow);
                cbShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Preference.setBoolean(context, Config.INTRO, isChecked);
                    }
                });
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }


        container.addView(layout);
        return layout;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }
}
