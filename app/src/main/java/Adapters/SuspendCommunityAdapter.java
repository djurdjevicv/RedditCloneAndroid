package Adapters;

import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.HomeActivity;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Banned;
import com.example.redditcloneandroid.model.Comment;
import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.BannedService;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuspendCommunityAdapter extends RecyclerView.Adapter<SuspendCommunityAdapter.MyViewHolder> {

    private List<Community> communityList;
    private Context context;

    public SuspendCommunityAdapter(List<Community> communityList, Context context) {

        this.communityList = communityList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_card, parent, false);

        return new SuspendCommunityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RetrofitService retrofitService = new RetrofitService();
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);
        BannedService bannedService = retrofitService.getRetrofit().create(BannedService.class);


        Community community = communityList.get(position);

        holder.communityName.setText(community.getName());

        holder.blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.change_post_title_popup, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText suspendReasonTxt = popupView.findViewById(R.id.newTitleForPost);
                suspendReasonTxt.setHint("SUSPEND REASON");

                AppCompatButton submitSuspendCommunity = popupView.findViewById(R.id.btnSubmitChangeTitle);
                submitSuspendCommunity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        User user = JWTUtils.getCurrentUser();
                        String suspendReason = suspendReasonTxt.getText().toString();

                        Banned banned = new Banned();
                        banned.setByUser(user.getUserId());
                        banned.setCommunity(community.getCommunityId());
                        banned.setBannedReason(suspendReason);

                        Call<Banned> bannedCommunity = bannedService.createBan(banned);
                        bannedCommunity.enqueue(new Callback<Banned>() {
                            @Override
                            public void onResponse(Call<Banned> call, Response<Banned> response) {
                                Toast.makeText(view.getContext(), "Successful banned community!", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();

                                Call<ResponseBody> deleteCommunity = communityService.deleteCommunity(community.getCommunityId());
                                deleteCommunity.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Toast.makeText(view.getContext(), "Successful delete community!", Toast.LENGTH_SHORT).show();
                                        popupWindow.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                                    }
                                });

                            }

                            @Override
                            public void onFailure(Call<Banned> call, Throwable t) {
                                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });




                    }
                });

                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView communityName;
        AppCompatButton blockButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            communityName = itemView.findViewById(R.id.itemNameForBlock);
            blockButton = itemView.findViewById(R.id.buttonBlock);
        }
    }

}
