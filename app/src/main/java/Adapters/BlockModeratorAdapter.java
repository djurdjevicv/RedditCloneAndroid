package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;

public class BlockModeratorAdapter extends RecyclerView.Adapter<BlockModeratorAdapter.MyViewHolder>{

    String data1[];
    Context context;

    public BlockModeratorAdapter(Context ct, String username[]){
        this.context = ct;
        this.data1 = username;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.block_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.myText1.setText(data1[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView myText1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
