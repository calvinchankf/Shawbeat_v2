package com.shawbeat;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.database.ArticleDBHelper;
import com.database.CategoryDBHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.network.DownloadArticle;
import com.network.DownloadArticleCompleteListener;
import com.network.DownloadCategory;
import com.network.DownloadCategoryCompleteListener;

public class ContentFrameFragment extends Fragment implements DownloadCategoryCompleteListener{
	private int ed_id;
	private ViewPager pager;
	private CategoryDBHelper categoryDBHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_content_frame, container, false);
		
		ed_id = getArguments().getInt("ed_id");
		//Toast.makeText(getActivity(), String.valueOf(ed_id), Toast.LENGTH_LONG).show();
		new DownloadCategory(this,getActivity()).execute(ed_id);
		
		pager = (ViewPager) view.findViewById(R.id.pager);
		SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
		
		categoryDBHelper = new CategoryDBHelper(getActivity());
		List<Category> cache = categoryDBHelper.getCategoryByEditionID(ed_id);
		if (cache!=null){
			adapter.mpagerCategory = cache;
		}
		
		pager.setAdapter(adapter);
		
		return view;
	}
	
	// Since this is an object collection, use a FragmentStatePagerAdapter,
	// and NOT a FragmentPagerAdapter.
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
		public List<Category> mpagerCategory = new ArrayList<Category>();
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
	    }
	
	    @Override
	    public Fragment getItem(int i) {
	        Fragment fragment = new MyListFragment();
	        Bundle args = new Bundle();
	        args.putInt("edition_id", ed_id);
	        args.putInt("category_id", mpagerCategory.get(i).getCategory_id());
		    fragment.setArguments(args);
		    return fragment;
		}
		
		@Override
		public int getCount() {
			return mpagerCategory.size();
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return mpagerCategory.get(position).getCategory_name();
	    }
	}
	
	// Instances of this class are fragments representing a single
	// object in our collection.
	public static class MyListFragment extends Fragment implements DownloadArticleCompleteListener{
		private int mEdition_id;
		private int mCategory_id;
	    private PullToRefreshListView lv;
	    private ArrayAdapter<String> mAdapter;
	    private List<Article> article = new ArrayList<Article>();
	    private ArrayList<String> mTitles = new ArrayList<String>();
	    private ArticleDBHelper articleDBHelper;
	    
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		    
			View v = inflater.inflate(R.layout.mylist, container, false);
			
			mEdition_id = getArguments().getInt("edition_id");
			mCategory_id = getArguments().getInt("category_id");
			
			lv = (PullToRefreshListView)v.findViewById(R.id.listView);
		    lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					new DownloadArticle(MyListFragment.this,getActivity()).execute(mEdition_id, mCategory_id);
				}
		    });
		    lv.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					//Toast.makeText(getActivity(), "selected index="+String.valueOf(position-1), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
	        	    intent.setClass(getActivity(),ContentActivity.class);
	        	    Bundle bundle = new Bundle();
	        	    bundle.putString("content", article.get(position-1).getContent());
	        	    bundle.putString("title", article.get(position-1).getTitle());
	        	    intent.putExtras(bundle);
	        	    startActivity(intent);
				}
		    });
		    
		    // get article from db
		    articleDBHelper = new ArticleDBHelper(getActivity());
		    List<Article> cache = articleDBHelper.getArticlesByEditionID(mEdition_id, mCategory_id);
		    if (cache!=null){
		    	article = cache;
		    	for (Article a : cache)
		    		mTitles.add(a.getTitle());
		    }
		    
		    mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mTitles);
		    lv.setAdapter(mAdapter);
		    new DownloadArticle(MyListFragment.this,getActivity()).execute(mEdition_id, mCategory_id);
		    
			return v;
	    }

		@Override
		public void downloadArticleResultCallback(List<Article> s) {
			if (s!=null){
				article = s;
				
				if (mTitles.size()>0) mTitles.clear();
				for (Article a : s)
					mTitles.add(a.getTitle());
				
				mAdapter.notifyDataSetChanged();
				articleDBHelper.insertOrUpdateArticle(s, mEdition_id, mCategory_id);
			}else{
				//Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
			}
			
			// Call onRefreshComplete when the list has been refreshed.
			lv.onRefreshComplete();
		}
		
		
	}

	@Override
	public void DownloadCategoryResultCallback(List<Category> s) {
		if (s!=null){
			View v = getView();
			pager = (ViewPager) v.findViewById(R.id.pager);
			SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
			adapter.mpagerCategory = s;
			pager.setAdapter(adapter);
			
			categoryDBHelper.insertOrUpdateCategory(s, ed_id);
		}else{
			//Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_LONG).show();
		}
	}
}
