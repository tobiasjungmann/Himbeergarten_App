package com.example.rpicommunicator_v1.Plants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rpicommunicator_v1.Database.Plant;
import com.example.rpicommunicator_v1.R;
import com.example.rpicommunicator_v1.ViewAndModels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;


public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantHolder> {
    private List<Plant> plants = new ArrayList<>();
    private OnItemClickListener mListener;
    private OnItemLongClickListener mlongListener;
    private MainActivityViewModel mainActivityViewModel;


    public void setViewModel(MainActivityViewModel mainActivityViewModel) {
        this.mainActivityViewModel = mainActivityViewModel;
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longListener) {
        this.mlongListener = longListener;
    }


    @Override
    public PlantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
        PlantHolder evh = new PlantHolder(v, mListener, mlongListener);
        return evh;
    }


    @Override
    public void onBindViewHolder(PlantHolder holder, int position) {
        Plant currentItem = plants.get(position);

        if (currentItem.getImageID() != -1) {
            holder.mImageView.setImageResource(currentItem.getIconID());
            float alpha = (float) 1;
            holder.mImageView.setAlpha(alpha);
        } else {
            //holder.blurHelper.setCardBackgroundColor(R.color.dark_yellow);//setBackgroundColor(R.color.dark_yellow);
            holder.mImageView.setImageResource(R.drawable.icon_pump);
            float alpha = (float) 0.1;
            holder.mImageView.setAlpha(alpha);
        }
        holder.mTextView1.setText(currentItem.getName());
        holder.mTextView2.setText(currentItem.getInfo());
        holder.datumView.setText(currentItem.getWatered());
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                mainActivityViewModel.remove(plants.get(position));

            }
        });
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }


    public static class PlantHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView datumView;
        public Button button_delete;

        public PlantHolder(View itemView, final OnItemClickListener listener, final OnItemLongClickListener longListener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.title);
            mTextView2 = itemView.findViewById(R.id.textView);
            datumView = itemView.findViewById(R.id.textView2);
            button_delete = itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClicked(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            if (button_delete.getVisibility()==View.GONE)
                            {
                                button_delete.setVisibility(View.VISIBLE);
                            }else {
                                button_delete.setVisibility(View.GONE);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });


        }
    }

}

