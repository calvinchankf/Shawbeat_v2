package com.network;

import static com.network.AccessUrl.myCookie;
import static com.network.AccessUrl.editionURL;

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
import com.shawbeat.Edition;

public class DownloadEdition{
	private DownloadEditionCompleteListener listener;
	private Context context;
	
	public DownloadEdition(DownloadEditionCompleteListener listener, Context context){
		this.listener = listener;
		this.context = context;
	}
	
	public void execute(){
		Log.i("tag", "execute() DownloadEdition");
		RequestQueue queue = Volley.newRequestQueue(context);
		String url = editionURL;
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	//listener.downloadEditionResultCallback(response);
		    	listener.downloadEditionResultCallback(editionStringToJson(response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		        //Log.e("Error: ", error.getMessage());
		    	listener.downloadEditionResultCallback(null);
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
	
	private List<Edition> editionStringToJson(String response){
		List<Edition> result = new ArrayList<Edition>();
		
		//parse json data
	    try{
            JSONArray jArray = new JSONArray(response);
            for(int i=0;i<jArray.length();i++){
            		Edition temp = new Edition();
                    JSONObject json_data = jArray.getJSONObject(i);
                    temp.setEdition_ID(json_data.getInt("edition_ID"));
                    temp.setEdition_name(json_data.getString("edition_name"));
                    temp.setDate(json_data.getString("date"));
                    result.add(temp);
            }
	    }catch(JSONException e){
            Log.i("log_tag", "Error parsing data "+e.toString());
            result = null;
	    }
		
		return result;
	}
}
