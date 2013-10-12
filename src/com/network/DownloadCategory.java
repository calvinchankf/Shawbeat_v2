package com.network;

import static com.network.AccessUrl.categoryBaseURL;
import static com.network.AccessUrl.myCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.shawbeat.Category;

public class DownloadCategory {
	
	public interface DownloadCategoryCompleteListener {
		void DownloadCategoryResultCallback(List<Category> s);
	}
	
	private DownloadCategoryCompleteListener listener;
	private Context context;
	
	public DownloadCategory(DownloadCategoryCompleteListener listener, Context context){
		this.listener = listener;
		this.context = context;
	}
	
	public void execute(int ed_id){
		Log.i("tag", "execute(ed_id) DownloadCategory");
		RequestQueue queue = Volley.newRequestQueue(context);
		String url = categoryBaseURL+String.valueOf(ed_id);
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	//listener.downloadEditionResultCallback(response);
		    	listener.DownloadCategoryResultCallback(categoryStringToJson(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        //Log.e("Error: ", error.getMessage());
		    	listener.DownloadCategoryResultCallback(null);
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
	
	private List<Category> categoryStringToJson(String response){
		List<Category> result = new ArrayList<Category>();
		
		//parse json dataz
	    try{
            JSONArray jArray = new JSONArray(response);
            for(int i=0;i<jArray.length();i++){
            		Category temp = new Category();
                    JSONObject json_data = jArray.getJSONObject(i);
                    temp.setEdition_ID(json_data.getInt("edition_ID"));
                    temp.setCategory_id(json_data.getInt("category_id"));
                    temp.setCategory_name(json_data.getString("category_name"));
                    result.add(temp);
            }
	    }catch(JSONException e){
            Log.i("log_tag", "Error parsing data "+e.toString());
            result = null;
	    }
		
		return result;
	}
}
