package com.occulus.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.occulus.rest.R;

import android.R.color;
import android.R.drawable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Perfil extends Activity {
	private ListView lstClientes;
	private int i, j;
	private String[] campañas, titulos, lista;
	private String login;
	private String user_id;
	private String respStr;
	private String campaña;
	private int lim;
	private String id;
	private ArrayList<Campanna> arrayAux;
	private Campanna campanna;
	private HttpURLConnection conn;
	private Bitmap imagen;
	private Configuracion c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil);

		c = new Configuracion();

		if (!MainActivity.verificaConexion(Perfil.this)) {
			Toast.makeText(getBaseContext(),
					"Comprueba tu conexión a Internet.", Toast.LENGTH_SHORT)
					.show();
		}

		Bundle b = getIntent().getExtras();
		login = b.getString("login");
		user_id = b.getString("user_id");

		arrayAux = new ArrayList<Campanna>();

		lstClientes = (ListView) findViewById(R.id.listaPerfil);
		TareaWSPerfil perfil = new TareaWSPerfil();

		perfil.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil, menu);
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

	private class TareaWSPerfil extends AsyncTask<String, Integer, Boolean> {

		// private String brand;

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(Perfil.this);
			pDialog.setMessage("Cargando");
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.show();

		}

		@Override
		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			// Cuando tengamos la url de la imagen en el JSON
			// simplemente esto habría que moverlo al bucle de lectura
			// y cargar la URL con un getString de la URL en el JSON
			// Log.i("doInBackground", "Entra en doInBackground");
			String url = "http://biddusavatar.s3.amazonaws.com/8223f77f2c09287ff1bfc3efc7888c06a627f47c.png?1403848467";
			imagen = descargarImagen(url);
			imagen = Bitmap.createScaledBitmap(imagen, 150, 150, true);

			/**
			 * 
			 * Sirve para obtener un cliente concreto
			 * 
			 * HttpClient httpClient = new DefaultHttpClient();
			 * 
			 * String id = params[0];
			 * 
			 * HttpGet del = new HttpGet(
			 * "http://occulus-stg.herokuapp.com/campaigns/" + id);
			 * 
			 * del.setHeader("content-type", "application/json");
			 * 
			 * try { HttpResponse resp = httpClient.execute(del); String respStr
			 * = EntityUtils.toString(resp.getEntity());
			 * 
			 * JSONObject respJSON = new JSONObject(respStr);
			 * 
			 * brand = respJSON.getString("brand");
			 * 
			 * } catch (Exception ex) { Log.e("ServicioRest", "Error!", ex);
			 * ex.printStackTrace(); resul = false;
			 * 
			 * }
			 */

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet(c.getMainListar());

			HttpGet campa = new HttpGet(c.getMainListarCampa(user_id));

			del.setHeader("content-type", "application/json");

			del.addHeader("Authorization", "Token token=\"" + login + "\"");

			campa.setHeader("content-type", "application/json");

			campa.addHeader("Authorization", "Token token=\"" + login + "\"");
			try {
				HttpResponse resp2 = httpClient.execute(campa);

				String respStr2 = EntityUtils.toString(resp2.getEntity());

				HttpResponse resp = httpClient.execute(del);

				String respStr = EntityUtils.toString(resp.getEntity());

				Log.v("respStr", respStr);
				Log.v("respStr2", respStr2);

				JSONObject objectCamp = new JSONObject(respStr2);
				JSONObject auxObject2 = objectCamp.getJSONObject("data");
				JSONArray respJSON2 = auxObject2.getJSONArray("orders");

				Log.v("JSON2", respJSON2.toString());

				JSONObject object = new JSONObject(respStr);
				// Log.v("Object", object.toString());

				JSONObject auxObject = object.getJSONObject("data");
				// Log.v("JSON2", auxObject.toString());

				JSONArray respJSON = auxObject.getJSONArray("campaigns");

				campañas = new String[respJSON.length()];
				titulos = new String[respJSON.length()];
				lista = new String[respJSON2.length()];

				for (i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);

					for (int p = 0; p < respJSON2.length(); p++) {
						JSONObject obj2 = respJSON2.getJSONObject(p);
						String id2 = obj2.getString("campaign_id");
						lista[p] = id2;
						Log.v("Lista", lista[p]);
					}

					// String respStr2 = obj.getString("data");

					// String title = obj.getString("title");
					// Log.v("OBJ", respStr);

					// lista[j] = campaña;
					//
					// Log.v("Lista", lista[j]);

					// for (int w = 0; w < j; w++) {
					//
					// if (lista[w].equalsIgnoreCase(id)) {
					// titulos[i] = " " + title + " Apuntado";
					//
					// } else {
					// titulos[i] = " " + title;
					// }
					// }

					// active = obj.getString("active");
					id = obj.getString("id");
					Log.v("ID", id);

					String marca = obj.getString("brand");
					String modelo = obj.getString("model");
					String title = obj.getString("title");

					// Log.v("lista" + j, lista[j].toString());
					// Log.v("ID" + j, id);

					// Log.v("length lista", "" + lista.length);

					// directivo = new Directivo(getResources().getDrawable(
					// R.drawable.facebook), title);
					// arraydir.add(directivo);

					for (int w = 0; w < lista.length; w++) {
						if (lista[w].equalsIgnoreCase(id)) {

							campanna = new Campanna(imagen, title,
									getResources().getDrawable(R.drawable.tick));

							arrayAux.add(campanna);

						}

						// arrayCamp.add(campanna);

						campañas[i] = " " + marca + " " + modelo + " ";

					}

					// directivo = new
					// Directivo(getResources().getDrawable(R.drawable.facebook),title);
					// arraydir.add(directivo);

					// titulos[i] = " " + title;

					// lstClientes.setOnItemClickListener(new
					// OnItemClickListener() {
					//
					// @Override
					// public void onItemClick(AdapterView<?> parent, View view,
					// int position, long id) {
					//
					// Intent intent = new Intent(MainActivity.this,
					// Activity1.class);
					// intent.putExtra("id", position + 1);
					// intent.putExtra("user_id", user_id);
					// intent.putExtra("login", login);
					//
					// startActivity(intent);
					//
				}
				// });

			} catch (SocketException ex) {
				Log.e("ServicioRest", "Error!", ex);

				resul = false;
				finish();
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);

				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {
			try {
				if (result) {
					// Rellenamos la lista con los resultados
					// ArrayAdapter<String> adaptador = new
					// ArrayAdapter<String>(
					// MainActivity.this,
					// android.R.layout.simple_list_item_1, titulos);
					AdapterCampannas adapter = new AdapterCampannas(
							Perfil.this, arrayAux);

					pDialog.dismiss();
					lstClientes.setAdapter(adapter);

				}

				if (arrayAux.isEmpty()) {
					Toast t = Toast.makeText(Perfil.this,
							"No estás apuntado a ninguna campaña",
							Toast.LENGTH_SHORT);
					t.show();
				}

			} catch (Exception e) {
				Log.e("ServicioRest", "Error del PostExecute", e);

			}
		}
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
