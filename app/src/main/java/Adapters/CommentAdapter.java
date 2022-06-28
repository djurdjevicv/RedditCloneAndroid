package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Comment;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.ReactionComment;
import com.example.redditcloneandroid.model.ReactionPost;
import com.example.redditcloneandroid.model.User;
import com.example.redditcloneandroid.model.enums.ReactionType;
import com.example.redditcloneandroid.retrofit.ReactionCommentService;
import com.example.redditcloneandroid.retrofit.ReactionPostService;
import com.example.redditcloneandroid.retrofit.RetrofitService;
import com.example.redditcloneandroid.retrofit.UserService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<Comment> commentList;
    Context context;

    public CommentAdapter(List<Comment> commentList, Context context){
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_card, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        //SET USERNAME WHERE USER ID
        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        Call<User> call = userService.getUserById(comment.getUser());
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

        //SET COMMENT KARMA
        ReactionCommentService reactionCommentService = retrofitService.getRetrofit().create(ReactionCommentService.class);
        Call<Karma> call2 = reactionCommentService.getCommentKarma(comment.getCommentId());
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

        holder.likeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User logUser = JWTUtils.getCurrentUser();
                Comment selectedComment = commentList.get(position);

                if (logUser != null){
                    ReactionComment reactionComment = new ReactionComment();
                    reactionComment.setReactionType(ReactionType.UPVOTE);
                    reactionComment.setUserId(logUser.getUserId());
                    reactionComment.setCommentId(selectedComment.getCommentId());

                    Call<ReactionComment> likeReaction = reactionCommentService.createReaction(reactionComment);
                    likeReaction.enqueue(new Callback<ReactionComment>() {
                        @Override
                        public void onResponse(Call<ReactionComment> call, Response<ReactionComment> response) {
                            Toast.makeText(view.getContext(), "Successful like comment!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ReactionComment> call, Throwable t) {
                            Logger.getLogger(CommentAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.dislikeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User logUser = JWTUtils.getCurrentUser();
                Comment selectedComment = commentList.get(position);

                if (logUser != null){
                    ReactionComment reactionComment = new ReactionComment();
                    reactionComment.setReactionType(ReactionType.DOWNVOTE);
                    reactionComment.setUserId(logUser.getUserId());
                    reactionComment.setCommentId(selectedComment.getCommentId());

                    Call<ReactionComment> dislikeReaction = reactionCommentService.createReaction(reactionComment);
                    dislikeReaction.enqueue(new Callback<ReactionComment>() {
                        @Override
                        public void onResponse(Call<ReactionComment> call, Response<ReactionComment> response) {
                            Toast.makeText(view.getContext(), "Successful dislike comment!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ReactionComment> call, Throwable t) {
                            Logger.getLogger(CommentAdapter.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        holder.commentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, commentText, karma;
        ImageView likeComment, dislikeComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernameComm);
            commentText = itemView.findViewById(R.id.textComment);
            karma = itemView.findViewById(R.id.commentKarma);
            likeComment = itemView.findViewById(R.id.likeCommentBtn);
            dislikeComment = itemView.findViewById(R.id.dislikeCommentBtn);
        }
    }
}
