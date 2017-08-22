package com.kong.app.blog;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.kong.R;
import com.kong.app.blog.model.Feed;
import com.kong.app.blog.tool.OnRCVScollListener;
import com.kong.app.demo.about.TextItemViewBinder;
import com.kong.app.demo.about.TextViewItem;
import com.library.utils.ListUtils;
import com.library.utils.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by CaoPengfei on 17/8/21.
 */

public class BlogContentView extends FrameLayout implements OnRCVScollListener.OnLoadListener {

    public static final String TAG = "BlogContentView";

    private RecyclerView mRecyclerView;
    private Feed.PostsBean mPostsBeans;
    private MultiTypeAdapter mAdapter;
    private List<Object> mObjectList = new ArrayList<Object>();
    public String mTitle;

    public BlogContentView(Context context) {
        this(context, null);
    }

    public BlogContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlogContentView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setPostsBeans(Feed.PostsBean postsBeans) {
        mPostsBeans = postsBeans;
        setAdapter();
    }

    private void init(Context context) {
        LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.fragment_blog_list, this, true);
        mRecyclerView = (RecyclerView) findViewById(R.id.blog_recycle_view_id);
    }

    private void setAdapter() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new OnRCVScollListener(layoutManager,this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Feed.PostsBean.ItemsBean.class, new BlogItemViewBinder());
        mAdapter.register(TextViewItem.class, new TextItemViewBinder());

        mRecyclerView.setAdapter(mAdapter);

        if (mPostsBeans == null) {
//            showError();
            return;
        }
        List<Feed.PostsBean.ItemsBean> list = mPostsBeans.getItems();
        if (!ListUtils.isEmpty(list)) {
            for (Feed.PostsBean.ItemsBean bean : list) {
                mObjectList.add(bean);
            }
            mAdapter.setItems(mObjectList);
            mAdapter.notifyDataSetChanged();
//            showResult();
        } else {
//            showError();
        }
    }

    @Override
    public void load() {
        if (!isEnd()){
            TextViewItem item = new TextViewItem();
            item.text = ResourceUtil.getString(R.string.blog_end);
            mObjectList.add(item);
            mAdapter.notifyDataSetChanged();
            setEnd(true);
        }
    }

    private boolean isEnd;

    private boolean isEnd(){
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}