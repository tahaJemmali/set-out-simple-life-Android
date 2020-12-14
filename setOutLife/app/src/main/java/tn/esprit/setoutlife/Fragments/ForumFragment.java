package tn.esprit.setoutlife.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import tn.esprit.setoutlife.Activities.Forum.AddPostActivity;
import tn.esprit.setoutlife.Activities.Forum.CommentsActivity;
import tn.esprit.setoutlife.Activities.HomeActivity;
import tn.esprit.setoutlife.Adapters.PostListAdapter;
import tn.esprit.setoutlife.Adapters.TaskListAdapter;
import tn.esprit.setoutlife.R;
import tn.esprit.setoutlife.Repository.IRepository;
import tn.esprit.setoutlife.Repository.PostRepository;
import tn.esprit.setoutlife.Utils.CallBackInterface;
import tn.esprit.setoutlife.entities.Comment;
import tn.esprit.setoutlife.entities.Post;
import tn.esprit.setoutlife.entities.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment implements IRepository {

    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    FloatingActionButton fab;
    private static final String FRAGMENT_NAME = "Social";

    ArrayList<Post> posts;


    PostListAdapter postListAdapter;
    RecyclerView rvPosts;

    SwipeRefreshLayout swipeRefreshLayout;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(String param1, String param2) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PostRepository.getInstance().setiRepository(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forum, container, false);

        mContext = getContext();
        rvPosts = view.findViewById(R.id.rvPosts);

        posts = PostRepository.getInstance().getPosts(mContext);
        postListAdapter = new PostListAdapter(mContext,posts);
        rvPosts.setAdapter(postListAdapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        postListAdapterListener();

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                posts = PostRepository.getInstance().getPosts(getContext());
                postListAdapter = new PostListAdapter(mContext,posts);
                postListAdapterListener();
                rvPosts.setAdapter(postListAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        PostRepository.getInstance().setiRepository(this);
        //PostRepository.getInstance().getPosts(getContext());

        /*System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAX");
        System.out.println(PostRepository.getInstance().getPosts(getContext()));*/


        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
            }
        });

        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_NAME);


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed from frag");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }

    private void postListAdapterListener() {
        postListAdapter.setLikeListener(new PostListAdapter.OnLikeClickListener() {
            @Override
            public void onLikeClick(int position) {
                //TODO api like
                Post post = posts.get(position);
                PostRepository.getInstance().likePost(post,HomeActivity.getCurrentLoggedInUser(),mContext);
            }

            @Override
            public void onUnLikeClick(int position) {
                //TODO api unLike
                Post post = posts.get(position);
                PostRepository.getInstance().unLikePost(post,HomeActivity.getCurrentLoggedInUser(),mContext);
            }

            @Override
            public void onCommentClick(int position) {
                Post post = posts.get(position);
                    Intent intent = new Intent(mContext, CommentsActivity.class);

                    intent.putExtra("post_id",post.getId());
                    intent.putExtra("user_email_post",post.getUser().getEmail());

                //List<Comment> commentList = new ArrayList<Comment>();
                //commentList = posts.get(position).getComments();

                //intent.putParcelableArrayListExtra("comments", posts.get(position).getComments());

                /*for (Comment comment: posts.get(position).getComments()){
                    System.out.println("azaz");
                    System.out.println(comment.getText());
                    System.out.println(comment.getUser().getEmail());
                    System.out.println("azaz");}*/

                mContext.startActivity(intent);
            }
        });
    }

    public void setCallBackInterface (CallBackInterface callBackInterface){
        this.callBackInterface = callBackInterface;
    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        postListAdapter.notifyDataSetChanged();
    }
}