package com.occulus.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import twitter4j.AccountSettings;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusListener;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trends;
import twitter4j.TweetEntity;
import twitter4j.Twitter;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.FavoritesResources;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.api.HelpResources;
import twitter4j.api.ListsResources;
import twitter4j.api.PlacesGeoResources;
import twitter4j.api.SavedSearchesResources;
import twitter4j.api.SearchResource;
import twitter4j.api.SpamReportingResource;
import twitter4j.api.SuggestedUsersResources;
import twitter4j.api.TimelinesResources;
import twitter4j.api.TrendsResources;
import twitter4j.api.TweetsResources;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.addthis.core.AddThis;
import com.addthis.core.Config;
import com.addthis.error.ATDatabaseException;
import com.addthis.error.ATSharerException;
import com.example.rest.R;
//import com.addthis.core.AddThis;
//import com.addthis.core.Config;
//import com.addthis.error.ATDatabaseException;
//import com.addthis.error.ATSharerException;
//import com.addthis.models.ATShareItem;
//import com.addthis.ui.views.ATButton;

public class Activity1 extends Activity {
	private int id;
	private String login;
	private String user_id;
	// private TextView txt;
	private Button boton, botonShare;
	private String respStr;
	private TextView lblResultado;
	private TextView titulo;
	private TextView lblFechaFin;
	private TextView fecha;
	private ImageButton imB1, imB2;
	private Bitmap imagen;
	private ImageView imagenDetalle;
	private Configuracion c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);

		c = new Configuracion();

		if (!MainActivity.verificaConexion(Activity1.this)) {
			Toast.makeText(getBaseContext(),
					"Comprueba tu conexión a Internet.", Toast.LENGTH_SHORT)
					.show();
		}

		Bundle b = getIntent().getExtras();
		id = b.getInt("id");
		user_id = b.getString("user_id");
		login = b.getString("login");

		// txt = (TextView) findViewById(R.id.texto);
		boton = (Button) findViewById(R.id.botonApuntar);
		// botonShare = (Button) findViewById(R.id.botonShare);
		//
		//
		// botonShare.setOnClickListener(new OnClickListener() {
		//
		//
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent(Intent.ACTION_SEND);
		//
		// intent.setType("text/plain");
		// intent.putExtra(android.content.Intent.EXTRA_TEXT,
		// "Mira que campaña colectiva acabo de encontrar.");
		// startActivity(Intent.createChooser(intent, "Share"));
		//
		// }
		// });

		imB1 = (ImageButton) findViewById(R.id.botonImagen1);
		imB2 = (ImageButton) findViewById(R.id.botonImagen2);
		imB1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					
					AddThis.shareItem(Activity1.this, "facebook",
							c.getUrlShare(), "Biddus Campañas Colectivas",
							"Mira que campaña colectiva acabo de encontrar");
				} catch (ATDatabaseException e) {
					e.printStackTrace();
				} catch (ATSharerException e) {
					e.printStackTrace();
				}
			}
		});

		imB2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Config.configObject().setTwitterViaText("biddus");

					AddThis.shareItem(Activity1.this, "twitter",
							c.getUrlShare(), "Biddus Campañas Colectivas",
							"Mira que campaña colectiva acabo de encontrar");
					
				} catch (ATDatabaseException e) {
					e.printStackTrace();
				} catch (ATSharerException e) {
					e.printStackTrace();
				}
			}
		});

		// switch (id) {
		//
		// case (1):
		// txt.setText("ID 1");
		// break;
		// case 2:
		// txt.setText("ID 2");
		//
		// break;
		// }

		lblResultado = (TextView) findViewById(R.id.detalles);
		titulo = (TextView) findViewById(R.id.tituloDetalles);
		lblFechaFin = (TextView) findViewById(R.id.lblfechaFin);
		fecha = (TextView) findViewById(R.id.fechaFin);

		imagenDetalle = (ImageView) findViewById(R.id.imagenDetalle);
		// lstClientes = (ListView) findViewById(R.id.lista);

		// Button obtener = (Button) findViewById(R.id.button1);

		// obtener.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {

		TareaWSObtener tarea = new TareaWSObtener();
		tarea.execute();

		// }
		// });

		boton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TareaWSInsertar insert = new TareaWSInsertar();
				// Id de la campaña e Id de usuario
				insert.execute("" + id, user_id);

				Intent intent = new Intent(Activity1.this, MainActivity.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("login", login);
				startActivity(intent);

				onRestartApuntado();

				// Toast t = Toast.makeText(Activity1.this,
				// "Apuntado correctamente a la campaña ",
				// Toast.LENGTH_SHORT);
				// t.show();
			}

		});

	
	}

	private class TareaWSObtener extends AsyncTask<String, Integer, Boolean> {

		private String brand;
		private String modelo;
		private String desc;
		private String fechaFin;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(Activity1.this);
			pDialog.setMessage("Cargando");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			String url = "http://biddusavatar.s3.amazonaws.com/5bdf1ca694cc9932c273f63a0876ba39a9a32299.png?1402655684";
			imagen = descargarImagen(url);
			imagen = Bitmap.createScaledBitmap(imagen, 150, 150, true);

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet(c.getActivity1Obtener(id));

			del.setHeader("content-type", "application/json");

			del.addHeader("Authorization", "Token token=\"" + login + "\"");

			try {
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());

				JSONObject object = new JSONObject(respStr);
				// Log.v("Object", object.toString());

				JSONObject auxObject = object.getJSONObject("data");
				// Log.v("JSON2", auxObject.toString());

				JSONObject respJSON = auxObject.getJSONObject("campaign");

				brand = respJSON.getString("brand");
				modelo = respJSON.getString("model");

				// Aquí iría la fecha de fin de la campaña que hasta ahora
				// no la tenemos en el JSON
				fechaFin = respJSON.getString("created_at");
				fechaFin = (String) fechaFin.subSequence(0, 10);
				desc = respJSON.getString("description");

			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				ex.printStackTrace();
				resul = false;

			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {

			if (result) {
				fecha.setVisibility(1);
				boton.setVisibility(1);
				imB1.setVisibility(1);
				imB2.setVisibility(1);

				titulo.setText(brand + " " + modelo + "\n");
				imagenDetalle.setImageBitmap(imagen);
				lblFechaFin.setText("\t" + fechaFin);
				lblResultado.setText("\n\n " + "Descripción: \n" + desc);
				pDialog.dismiss();

			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class TareaWSInsertar extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost(c.getActivity1Insertar(id));

			post.setHeader("content-type", "application/json");
			post.addHeader("Authorization", "Token token=\"" + login + "\"");

			try {
				// Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();

				dato.put("campaign_id", params[1]);
				dato.put("user_id", params[1]);

				StringEntity entity = new StringEntity(dato.toString());
				post.setEntity(entity);

				HttpResponse resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("COSA", respStr);

				if (!respStr.equals("true"))
					resul = false;
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {

			if (respStr.contains("false")) {
				Toast t = Toast.makeText(Activity1.this,
						"El usuario ya está apuntado en esta campaña",
						Toast.LENGTH_SHORT);
				t.show();
			} else {
				Toast t = Toast.makeText(Activity1.this,
						"Apuntado correctamente a la campaña ",
						Toast.LENGTH_SHORT);
				t.show();
			}
		}

	}

	private void onRestartApuntado() {
		finish();
	}

	public Bitmap descargarImagen(String imageHttpAddress) {
		URL imageUrl = null;
		Bitmap imagen = null;
		try {
			imageUrl = new URL(imageHttpAddress);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.connect();
			imagen = BitmapFactory.decodeStream(conn.getInputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return imagen;
	}

}
