package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Comment;
import com.example.redditcloneandroid.model.Community;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.ReactionPost;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.model.enums.ReactionType;
import com.example.redditcloneandroid.retrofit.CommentService;
import com.example.redditcloneandroid.retrofit.CommunityService;
import com.example.redditcloneandroid.retrofit.PostService;
import com.example.redditcloneandroid.retrofit.ReactionPostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.CommentAdapter;
import Adapters.PostAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnePostActivity extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private SwipeRefreshLayout refreshLayout;
    private AppCompatButton brnAddComment;
    ImageView likeBtn, dislikeBtn;
    private TextView username, community, postTitle, postText, karma;

    private int selectedPostId;
    private Post post;
    private User logUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_post);

        getSupportActionBar().hide();

        if (JWTUtils.getCurrentUser() != null){
            logUser = JWTUtils.getCurrentUser();
        }

        username = findViewById(R.id.usernameOnePost);
        community = findViewById(R.id.communityOnePost);
        postTitle = findViewById(R.id.postTitleOnePost);
        postText = findViewById(R.id.postTextOnePost);
        karma = findViewById(R.id.karmaOnePost);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedPostId = extras.getInt("post");
        }

        ImageView btn = findViewById(R.id.backButtonOnePost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnePostActivity.super.onBackPressed();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getPost();
                getComments();
                addComment();
                postReaction();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerViewComments = (RecyclerView) findViewById(R.id.commentsRecycle);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        getPost();
        getComments();
        addComment();
        postReaction();


    }

    public void addComment(){

        brnAddComment =  findViewById(R.id.btnAddComment);
        brnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.add_comment_popup, null);

                int width = 700;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewComment = popupView.findViewById(R.id.textForComment);

                AppCompatButton submitAddComment = popupView.findViewById(R.id.btnSubmitAddComment);
                submitAddComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newCommentText = inputNewComment.getText().toString();

                        if (inputNewComment.length() == 0){
                            inputNewComment.setError("This field is required!");
                        }else {

                            User user = JWTUtils.getCurrentUser();

                            Comment comment = new Comment();
                            comment.setText(newCommentText);
                            comment.setPost(selectedPostId);
                            comment.setUser(user.getUserId());

                            RetrofitService retrofitService = new RetrofitService();
                            CommentService commentService = retrofitService.getRetrofit().create(CommentService.class);
                            Call<Comment> create = commentService.createComment(comment);

                            create.enqueue(new Callback<Comment>() {
                                @Override
                                public void onResponse(Call<Comment> call, Response<Comment> response) {
                                    Toast.makeText(OnePostActivity.this, "Successful add comment!", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Comment> call, Throwable t) {
                                    Toast.makeText(OnePostActivity.this, "Failed to add comment!", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                                }
                            });


                        }

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


    public void getPost(){

        RetrofitService retrofitService = new RetrofitService();
        PostService postService = retrofitService.getRetrofit().create(PostService.class);
        Call<Post> call = postService.getPostByID(selectedPostId);

        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        CommunityService communityService = retrofitService.getRetrofit().create(CommunityService.class);
        ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                post = response.body();

                Call<User> getUserUsername = userService.getUserById(post.getUser());
                getUserUsername.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        username.setText("@" + response.body().getUsername());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });

                Call<Community> getCommunityName = communityService.getCommunityById(post.getCommunity());
                getCommunityName.enqueue(new Callback<Community>() {
                    @Override
                    public void onResponse(Call<Community> call, Response<Community> response) {
                        community.setText("@" + response.body().getName());
                    }

                    @Override
                    public void onFailure(Call<Community> call, Throwable t) {

                    }
                });

                Call<Karma> getPostKarma = reactionPostService.getPostKarma(post.getPostId());
                getPostKarma.enqueue(new Callback<Karma>() {
                    @Override
                    public void onResponse(Call<Karma> call, Response<Karma> response) {
                        karma.setText(String.valueOf(response.body().getKarma()));
                    }

                    @Override
                    public void onFailure(Call<Karma> call, Throwable t) {
                        Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });

                postTitle.setText(post.getTitle());
                postText.setText(post.getText());

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

    public void getComments(){

        RetrofitService retrofitService = new RetrofitService();
        CommentService commentService = retrofitService.getRetrofit().create(CommentService.class);
        Call<List<Comment>> getPostComments = commentService.getCommentsByPost(selectedPostId);

        getPostComments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                recyclerViewComments.setAdapter(new CommentAdapter(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(OnePostActivity.this, "Failed to load comments!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void postReaction(){

        RetrofitService retrofitService = new RetrofitService();
        likeBtn = findViewById(R.id.likeImgBtn);
        dislikeBtn = findViewById(R.id.dislikeImgBtn);

        //CREATE LIKE REACTION
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logUser != null){
                    ReactionPost reactionPost = new ReactionPost();
                    reactionPost.setReactionType(ReactionType.UPVOTE);
                    reactionPost.setUserId(logUser.getUserId());
                    reactionPost.setPostId(selectedPostId);

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
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logUser != null){
                    ReactionPost reactionPost = new ReactionPost();
                    reactionPost.setReactionType(ReactionType.DOWNVOTE);
                    reactionPost.setUserId(logUser.getUserId());
                    reactionPost.setPostId(selectedPostId);

                    ReactionPostService reactionPostService = retrofitService.getRetrofit().create(ReactionPostService.class);
                    Call<ReactionPost> dislikeReaction = reactionPostService.createReaction(reactionPost);
                    dislikeReaction.enqueue(new Callback<ReactionPost>() {
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

    }

}