package Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.CommunityPageActivity;
import com.example.redditcloneandroid.FragmentUserPosts;
import com.example.redditcloneandroid.HomeActivity;
import com.example.redditcloneandroid.LogInActivity;
import com.example.redditcloneandroid.OnePostActivity;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.SignUpActivity;
import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.PostList;
import com.example.redditcloneandroid.model.ReactionPost;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.model.enums.ReactionType;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.ReactionPostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>{

    private List<Post> postList;
    private Context context;

    private String username;
    private String communityName;

    public PostAdapter(List<Post> postList, Context context) {

        this.postList = postList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post1 = postList.get(position);

        holder.commImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);

                Intent intent= new Intent(context, OnePostActivity.class);
                intent.putExtra("post", selectedPost.getPostId());
                context.startActivity(intent);
            }
        });

        holder.commTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post selectedPost = postList.get(position);

                Intent intent= new Intent(context, OnePostActivity.class);
                intent.putExtra("post", selectedPost.getPostId());
                context.startActivity(intent);
            }
        });


        //SET USERNAME WHERE USER ID
        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        Call<User> call = userService.getUserById(post1.getUser());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (!response.body().getDisplayName().equals("")){
                    holder.username.setText("@" + response.body().getDisplayName());
                }else {
                    holder.username.setText("@" + response.body().getUsername());
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //SET COMMUNITY NAME WHERE COMMUNITY ID
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);
        Call<Community> call1 = communityService.getCommunityById(post1.getCommunity());
        call1.enqueue(new Callback<Community>() {
            @Override
            public void onResponse(Call<Community> call, Response<Community> response) {
                holder.community.setText("@" + response.body().getName());
            }

            @Override
            public void onFailure(Call<Community> call, Throwable t) {
                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //CLICK ON COMMUNITY NAME
        holder.community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityName1 = holder.community.getText().toString();
                System.out.println("Community " + communityName1.substring(1));
                Intent intent= new Intent(context, CommunityPageActivity.class);
                intent.putExtra("communityName", communityName1.substring(1));
                context.startActivity(intent);
            }
        });


        //SET POST KARMA
        ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);
        Call<Karma> call2 = reactionPostService.getPostKarma(post1.getPostId());
        call2.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {
                holder.karma.setText(String.valueOf(response.body().getKarma()));
            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //CREATE LIKE REACTION
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);
                User logUser = JWTUtils.getCurrentUser();

                if (logUser != null){
                    ReactionPost reactionPost = new ReactionPost();
                    reactionPost.setReactionType(ReactionType.UPVOTE);
                    reactionPost.setUserId(logUser.getUserId());
                    reactionPost.setPostId(selectedPost.getPostId());

                    ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);
                    Call<ReactionPost> likeReaction = reactionPostService.createReaction(reactionPost);
                    likeReaction.enqueue(new Callback<ReactionPost>() {
                        @Override
                        public void onResponse(Call<ReactionPost> call, Response<ReactionPost> response) {
                            Toast.makeText(view.getContext(), "Successful like post!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ReactionPost> call, Throwable t) {
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else {
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }



            }
        });



        //CREATE DISLIKE REACTION
        holder.dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);
                User logUser = JWTUtils.getCurrentUser();

                if (logUser != null){
                    ReactionPost reactionPost = new ReactionPost();
                    reactionPost.setReactionType(ReactionType.DOWNVOTE);
                    reactionPost.setUserId(logUser.getUserId());
                    reactionPost.setPostId(selectedPost.getPostId());

                    ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);
                    Call<ReactionPost> likeReaction = reactionPostService.createReaction(reactionPost);
                    likeReaction.enqueue(new Callback<ReactionPost>() {
                        @Override
                        public void onResponse(Call<ReactionPost> call, Response<ReactionPost> response) {
                            Toast.makeText(view.getContext(), "Successful dislike post!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<ReactionPost> call, Throwable t) {
                            Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }



            }
        });


        holder.postText.setText(post1.getText());
        holder.postTitle.setText(post1.getTitle());



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, community, postTitle, postText, karma, commTxtBtn;
        ImageView likeBtn, dislikeBtn, commImgBtn;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            community = itemView.findViewById(R.id.community);
            postTitle = itemView.findViewById(R.id.postTitle);
            postText = itemView.findViewById(R.id.postText);
            karma = itemView.findViewById(R.id.karma);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            dislikeBtn = itemView.findViewById(R.id.dislikeBtn);
            commImgBtn = itemView.findViewById(R.id.commentsImgButton);
            commTxtBtn = itemView.findViewById(R.id.commentsTextButton);
        }
    }



}
