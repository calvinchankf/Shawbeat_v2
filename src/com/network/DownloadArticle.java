package com.network;

import static com.network.AccessUrl.articleBaseURL;
import static com.network.AccessUrl.myCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.content.Context;
import android.util.Log;
import com.shawbeat.Article;

public class DownloadArticle {
	private DownloadArticleCompleteListener listener;
	private Context context;
	
	public DownloadArticle(DownloadArticleCompleteListener listener, Context context){
		this.listener = listener;
		this.context = context;
	}
	
	public void execute(int ed_id, int cat_id){
		Log.i("tag", "execute() DownloadArticle");
		RequestQueue queue = Volley.newRequestQueue(context);
		String url = articleBaseURL+String.valueOf(ed_id)+"&category_id="+cat_id;
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	//listener.downloadEditionResultCallback(response);
		    	listener.downloadArticleResultCallback(articleStringToJson(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        //Log.e("Error: ", error.getMessage());
		    	listener.downloadArticleResultCallback(null);
		    }
		}){
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put("Cookie", myCookie);
				return headers;
			}
		};
		queue.add(stringRequest);
	}
	
	private List<Article> articleStringToJson(String response){
		List<Article> result = new ArrayList<Article>();
		
		//parse json data
	    try{
            JSONArray jArray = new JSONArray(response);
            for(int i=0;i<jArray.length();i++){
            		Article temp = new Article();
                    JSONObject json_data = jArray.getJSONObject(i);
                    temp.setArticleID(json_data.getInt("article_ID"));
                    temp.setEdition_ID(json_data.getInt("edition_ID"));
                    temp.setPosition(json_data.getInt("position"));
                    temp.setTitle(json_data.getString("title"));
                    temp.setAuthor(json_data.getString("author"));
                    temp.setContent(json_data.getString("content"));
                    temp.setCategory_id(json_data.getInt("category_id"));
                    temp.setCategory_name(json_data.getString("category_name"));
                    result.add(temp);
            }
	    }catch(JSONException e){
            Log.e("log_tag", "Error parsing data "+e.toString());
            return null;
	    }
		
		return result;
	}
}
