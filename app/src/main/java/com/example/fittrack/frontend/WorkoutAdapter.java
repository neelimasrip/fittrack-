package com.example.fittrack.frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fittrack.R;
import com.example.fittrack.backend.WorkoutData;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private final Context context;
    private List<WorkoutData.Workout> workouts;

    public WorkoutAdapter(Context context, List<WorkoutData.Workout> workouts) {
        this.context = context;
        this.workouts = workouts;
    }

    public void setWorkouts(List<WorkoutData.Workout> workouts) {
        this.workouts = workouts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutData.Workout workout = workouts.get(position);
        holder.tvTitle.setText(workout.title);
        
        String[] subtitleParts = workout.subtitle.split(" • ");
        if (subtitleParts.length == 2) {
            holder.tvDuration.setText(subtitleParts[0]);
            holder.tvDifficulty.setText(" • " + subtitleParts[1]);
        } else {
            holder.tvDuration.setText(workout.subtitle);
            holder.tvDifficulty.setText("");
        }

        Glide.with(context)
                .load(workout.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_workout)
                .centerCrop()
                .into(holder.ivImg);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WorkoutDetailActivity.class);
            intent.putExtra("workout_title", workout.title);
            intent.putExtra("workout_subtitle", workout.subtitle);
            intent.putExtra("workout_description", workout.description);
            intent.putExtra("workout_exercise", workout.exercise);
            intent.putExtra("workout_image", workout.imageUrl);
            intent.putExtra("workout_reps", workout.reps);
            intent.putExtra("workout_sets", workout.sets);
            if (workout.steps != null) {
                intent.putExtra("workout_steps", workout.steps);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        TextView tvTitle;
        TextView tvDuration;
        TextView tvDifficulty;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_workout_img);
            tvTitle = itemView.findViewById(R.id.tv_workout_title);
            tvDuration = itemView.findViewById(R.id.tv_workout_duration);
            tvDifficulty = itemView.findViewById(R.id.tv_workout_difficulty);
        }
    }
}
