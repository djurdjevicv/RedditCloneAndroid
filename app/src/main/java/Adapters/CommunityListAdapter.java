package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.CommunityPageActivity;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.Post;

import java.util.List;

public class CommunityListAdapter  extends RecyclerView.Adapter<CommunityListAdapter.MyViewHolder>{

    private List<Community> communityList;
    private Context context;

    public CommunityListAdapter(List<Community> communityList, Context context) {

        this.communityList = communityList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_card, parent, false);

        TextView communityTxtBtn = view.findViewById(R.id.communityName);
        communityTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String communityName = communityTxtBtn.getText().toString();

                Intent intent= new Intent(context, CommunityPageActivity.class);
                intent.putExtra("communityName",communityName);
                context.startActivity(intent);
            }
        });

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityListAdapter.MyViewHolder holder, int position) {

        Community community = communityList.get(position);

        String communityId = community.getName();

        holder.communityName.setText(communityId);

    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView communityName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            communityName = itemView.findViewById(R.id.communityName);
        }
    }
}
