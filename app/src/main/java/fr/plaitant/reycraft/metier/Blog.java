package fr.plaitant.reycraft.metier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Blog extends AsyncTask<String, Void, Boolean>{

	@Override
	protected Boolean doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		final String strURL = "http://alexandreplaitant.ddns.net/android/blog.php";
		InputStream is = null;
		String result = "";
		
		
		// Envoie de la commande http
    try{
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(strURL);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();
        Log.d("moi", "Fichier Telecharger");
    }
    catch(Exception e){
    	Log.e("log_tag", "Error in http connection " + e.toString());
	}
    
    // Convertion de la requ�te en string
   try{
       BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
       StringBuilder sb = new StringBuilder();
       String line = null;
       while ((line = reader.readLine()) != null) {
    	      sb.append(line + ",");  }
       is.close();
       result=sb.toString();
   	System.out.println(result);
   	Log.d("moi", "Resultat recuperer");
   }
   catch(Exception e){
	   Log.e("log_tag", "Error converting result " + e.toString());
   }
   
   // parse les donnes JSON
       try{
    	   JSONArray jArray = new JSONArray(result);
    	   for(int i=0;i<jArray.length();i++){

               JSONObject json_data = jArray.getJSONObject(i);
               //Log.d("moi", json_data.getString("titreBlog")+" : "+
            	//	   json_data.getString("descriptionBlog"));
               Post ajout = new Post();
               Post post = new Post(json_data.getInt("id"), json_data.getString("titreBlog"),
            		   json_data.getString("descriptionBlog"));
               Log.d("moi", post.getTitre());
               ajout.addPost(post);
               Log.d("moi", ajout.getPost().get(0).getTitre());
	   }
	   //System.out.println(jArray.getJSONObject(0));
   }
   catch(JSONException e){
	   Log.e("log_tag", "Error parsing data " + e.toString());
     	}
		return null;
	}
}
