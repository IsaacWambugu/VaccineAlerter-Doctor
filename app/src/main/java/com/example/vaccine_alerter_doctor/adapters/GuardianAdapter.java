package com.example.vaccine_alerter_doctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.models.GuardianModel;

import java.util.ArrayList;

public class GuardianAdapter extends RecyclerView.Adapter<GuardianAdapter.ViewHolder> {
    private ArrayList<GuardianModel> list;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView gender;
        private ImageButton callButton;
        private TextView id;
        private ImageView guardianPic;

        public ViewHolder(View v) {
            super(v);

            callButton = v.findViewById(R.id.btn_call);
            gender = v.findViewById(R.id.guardian_gender_row);
            id = v.findViewById(R.id.guardian_id_row);
            name = v.findViewById(R.id.guardian_name_row);
            guardianPic = v.findViewById(R.id.guardian_image);
        }
    }

    public GuardianAdapter(Context context, ArrayList<GuardianModel> data) {
        this.list = new ArrayList<>();
        this.list = data;
        this.context = context;
    }

    @Override
    public GuardianAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guardian_list_row, parent, false);
        return new GuardianAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(GuardianAdapter.ViewHolder holder, int position) {
        final GuardianModel guardianModelHolder = list.get(position);
        holder.gender.setText(guardianModelHolder.getGender());
        holder.id.setText(guardianModelHolder.getId());

        //set the gender icon

        if (guardianModelHolder.getGender().equals("Male")) {

            holder.guardianPic.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.ic_male,
                    null));

        } else if (guardianModelHolder.getGender().equals("Female")) {

            holder.guardianPic.setBackground(ResourcesCompat.getDrawable(context.getResources(),
                    R.drawable.ic_female,
                    null));

        }

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +"+254"+ guardianModelHolder.getNumber()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        holder.name.setText(guardianModelHolder.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}