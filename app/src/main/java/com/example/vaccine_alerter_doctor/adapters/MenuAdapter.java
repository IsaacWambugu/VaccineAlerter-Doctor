package com.example.vaccine_alerter_doctor.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vaccine_alerter_doctor.R;
import com.example.vaccine_alerter_doctor.activites.ChildActivity;
import com.example.vaccine_alerter_doctor.activites.GuardianActivity;
import com.example.vaccine_alerter_doctor.activites.ChildrenListActivity;
import com.example.vaccine_alerter_doctor.models.MenuModel;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    ArrayList<MenuModel> menuList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView image;


        public ViewHolder(@NonNull View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.textItem);
            image = (CardView) v.findViewById(R.id.imageItem);
        }
    }
    public MenuAdapter(Context context, ArrayList<MenuModel> itemsData){

        this.context = context;
        this.menuList  = new ArrayList<>();
        this.menuList  = itemsData;
    }

    @Override
    public int getItemCount() {

        return menuList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home,parent,false);
        context = parent.getContext();
        return  new MenuAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final MenuModel menuModelHolder  = menuList.get(position);

        holder.image.setBackgroundColor(Color.parseColor(menuModelHolder.getColor()));
        holder.textView.setText(menuModelHolder.getMenuItem());
        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String title = menuModelHolder.getMenuItem();
                int option = menuModelHolder.getOption();
                Intent intent = null;

                switch(option){
                    case 0:
                       intent = new Intent(context, ChildrenListActivity.class);
                       intent.putExtra("title",title);
                    break;

                    case 1:
                        intent = new Intent(context, ChildActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("option",0);
                    break;

                    case 2:
                        intent = new Intent(context, ChildActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("option",1);

                    break;
                    case 3:
                        intent = new Intent(context, GuardianActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("option",0);

                        break;
                    case 4:
                        intent = new Intent(context, GuardianActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("option",1);
                        break;
                        case 5:
                        intent = new Intent(context, GuardianActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("option",option);

                }
                context.startActivity(intent);

            }
        });

    }
}
