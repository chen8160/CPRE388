package example.com.lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity implements DownloadWebpageTask.ResultHandler{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Button search = (Button)findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			/* (non-Javadoc)
			 * @see android.view.View.OnClickListener#onClick(android.view.View)
			 */
			public void onClick(View v) {

				//TODO get the username to search for from the activity_main.xml view
                EditText editText = (EditText) findViewById(R.id.username);
				String artist = editText.getText().toString();
                artist = artist.replaceAll("\\s+", "+");
                Log.e("Artist Searched", artist);
                //TODO execute a new DownloadWebpageTask
                DownloadWebpageTask downloadWebpageTask = new DownloadWebpageTask(MainActivity.this);
                downloadWebpageTask.execute("https://itunes.apple.com/search?term=" + artist + "&entity=song&limit=20");

			}
		});
	}


	@Override
	public void handleResult(String result) {
		//TODO Handle the Result of a Network Call

        JSONObject jObj = null;
        try{
            jObj = new JSONObject(result);
            int resultCount = jObj.getInt("resultCount");
            JSONArray results = jObj.getJSONArray("results");
            //Log.e("obj: ", results.get(0).toString());
            //Log.e("type: ", jObj.get("results").getClass().toString());
            ArrayList<ItunesRecord> list = new ArrayList<>();

            for(int i = 0; i < resultCount; i++){
                JSONObject rec = (JSONObject) results.get(i);
                list.add(new ItunesRecord(rec.getString("collectionName"), rec.getString("trackName")));
            }

            ItunesAdapter adapter = new ItunesAdapter(this, R.layout.rwo, list);
            setListAdapter(adapter);

        } catch (JSONException e){
            e.printStackTrace();
        }




	}
}
