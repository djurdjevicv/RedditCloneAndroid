package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;

public class ReportCommentsAdapter extends RecyclerView.Adapter<ReportCommentsAdapter.MyViewHolder>  {

    String data1[], data2[];
    Context context;

    public ReportCommentsAdapter(Context ct, String usernamePost[], String commentPost[]){
        this.context = ct;
        this.data1 = usernamePost;
        this.data2 = commentPost;
    }

    @NonNull
    @Override
    public ReportCommentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reported_comm_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCommentsAdapter.MyViewHolder holder, int position) {
        holder.myText1.setText(data1[position]);
        holder.myText2.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.usernameComm);
            myText2 = itemView.findViewById(R.id.textComment);
        }
    }
}
