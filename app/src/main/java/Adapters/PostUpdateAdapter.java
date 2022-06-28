package Adapters;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.CommunityPageActivity;
import com.example.redditcloneandroid.FragmentSuspendCommunity;
import com.example.redditcloneandroid.FragmentUserUpdatePosts;
import com.example.redditcloneandroid.OnePostActivity;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.PostList;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.ReactionPostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class PostUpdateAdapter extends RecyclerView.Adapter<PostUpdateAdapter.MyViewHolder>{

    private List<Post> postList;
    private Context context;

    private String username;
    private String communityName;

    public PostUpdateAdapter(List<Post> postList, Context context) {

        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostUpdateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_update_card, parent, false);


        return new PostUpdateAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostUpdateAdapter.MyViewHolder holder, int position) {

        Post post1 = postList.get(position);

        //SET USERNAME WHERE USER ID
        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        Call<User> call = userService.getUserById(post1.getUser());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                holder.username.setText("@" + response.body().getUsername());
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


        holder.postText.setText(post1.getText());
        holder.postTitle.setText(post1.getTitle());

        //POPUP CHANGE TITLE
        holder.btnChangeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.change_post_title_popup, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewTitle = popupView.findViewById(R.id.newTitleForPost);

                AppCompatButton changeTitle = popupView.findViewById(R.id.btnSubmitChangeTitle);
                changeTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newTitle = inputNewTitle.getText().toString();

                        Post changedPost = postList.get(position);
                        changedPost.setTitle(newTitle);

                        PostService postService = retrofitService.getRetrofit().create(PostService.class);
                        Call<Post> changePost = postService.changePost(changedPost, changedPost.getPostId());
                        changePost.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                Toast.makeText(view.getContext(), "Successful change post title!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                        popupWindow.dismiss();

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

        //POPUP CHANGE TEXT
        holder.btnCahangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.change_post_text_popup, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewText = popupView.findViewById(R.id.newTextForPost);

                AppCompatButton changeText = popupView.findViewById(R.id.btnSubmitChangeText);
                changeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newText = inputNewText.getText().toString();

                        Post changedPost = postList.get(position);
                        changedPost.setText(newText);

                        PostService postService = retrofitService.getRetrofit().create(PostService.class);
                        Call<Post> changePost = postService.changePost(changedPost, changedPost.getPostId());
                        changePost.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                Toast.makeText(view.getContext(), "Successful change post text!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                        popupWindow.dismiss();

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

        holder.btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);

                PostService postService = retrofitService.getRetrofit().create(PostService.class);
                Call<ResponseBody> deletePost = postService.deleteItem(selectedPost.getPostId());

                deletePost.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Toast.makeText(view.getContext(), "Successful delete post!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });

            }
        });



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, community, postTitle, postText;
        AppCompatButton btnChangeTitle, btnCahangeText, btnDeletePost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            community = itemView.findViewById(R.id.community);
            postTitle = itemView.findViewById(R.id.postTitle);
            postText = itemView.findViewById(R.id.postText);
            btnChangeTitle = itemView.findViewById(R.id.buttonChangeTitle);
            btnCahangeText = itemView.findViewById(R.id.buttonChangeTExt);
            btnDeletePost = itemView.findViewById(R.id.buttonDeletePost);

        }
    }

}
