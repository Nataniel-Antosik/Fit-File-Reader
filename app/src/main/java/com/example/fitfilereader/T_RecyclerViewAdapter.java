package com.example.fitfilereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class T_RecyclerViewAdapter extends RecyclerView.Adapter<T_RecyclerViewAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<TrainingModel> trainingModels;

    public T_RecyclerViewAdapter(Context context, ArrayList<TrainingModel> trainingModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.trainingModels = trainingModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public T_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return new T_RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull T_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tData.setText(trainingModels.get(position).getTrainingData());
        holder.tPace.setText(trainingModels.get(position).getTrainingPace());
        holder.tDistance.setText(trainingModels.get(position).getTrainingDistance());
    }

    @Override
    public int getItemCount() {
        return trainingModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tData, tPace, tDistance;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            
            tData = itemView.findViewById(R.id.textViewTrainingData);
            tPace = itemView.findViewById(R.id.textViewTrainingPace);
            tDistance = itemView.findViewById(R.id.textViewTrainingDistance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null){
                        int position = getAdapterPosition();

                        if(position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
