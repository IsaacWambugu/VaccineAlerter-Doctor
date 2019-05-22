package com.example.vaccine_alerter_doctor.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
        private Button callButton;
        private TextView id;

        public ViewHolder(View v) {
            super(v);

            callButton = v.findViewById(R.id.btn_call);
            gender = v.findViewById(R.id.guardian_gender_row);
            id = v.findViewById(R.id.guardian_id_row);
            name = v.findViewById(R.id.guardian_name_row);

        }
    }

    public GuardianAdapter(Context context, ArrayList<GuardianModel> data) {
        this.list = new ArrayList<>();
        this.list = data;
        this.context = context;
    }
    @Override
    public GuardianAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.guardian_list_row,parent,false);

        return  new GuardianAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(GuardianAdapter.ViewHolder holder, int position) {
        final GuardianModel guardianModelHolder  = list.get(position);
        holder.gender.setText(guardianModelHolder.getGender());
        holder.id.setText(guardianModelHolder.getId());
        holder.callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + guardianModelHolder.getNumber()));
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