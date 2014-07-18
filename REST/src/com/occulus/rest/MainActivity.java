package com.occulus.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.occulus.rest.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseCloud;
import com.parse.ParseInstallation;
import com.parse.ParseRelation;
import com.parse.ParseTwitterUtils;
import com.parse.PushService;

public class MainActivity extends Activity {
	// private TextView lblResultado;
	private ListView lstClientes;
	private int i, j;
	private String[] campañas, titulos, lista;
	private String login;
	private String user_id;
	private TareaWSLogOut descon;
	private String respStr;
	private String id;
	private boolean repe = false;
	// private TextView texto;
	private ArrayList<Campanna> arrayCamp;
	private Campanna campanna;
	private HttpURLConnection conn;
	private Bitmap imagen;
	private TextView tituloCamp;
	private TareaWSListar tarea;
	private Configuracion c;
	private Button proponCamp;

	// private String active;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Parse.initialize(this, "PAkvxW7z7T7k5TLHbMQAV0uKXBeNK20iVtpLNTYr",
				"DguMmFZPBJOjlAr4SoAG0cYeFgrl6wdYSSNU0lkl");

		PushService.subscribe(this, "Biddus", MainActivity.class);

		// PushService.setDefaultPushCallback(MainActivity.this,
		// MainActivity.class);
		//
		// Crear notificaciones parse con un id distinto para cada dispositivo
		//
		// String androidId =
		// Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
		// Parse.initialize(this, "KEY1", "KEY2");
		// PushService.setDefaultPushCallback(this, ParseActivity.class);
		//
		// ParseInstallation installation =
		// ParseInstallation.getCurrentInstallation();
		// installation.put("UniqueId",androidId);
		// installation.put("channel", "Biddus");
		// installation.setObjectId(null);
		//
		// installation.saveInBackground();

		// ParseInstallation.getCurrentInstallation().saveInBackground();

		c = new Configuracion();

		// Notificaciones

		// Intent resultIntent = new Intent(MainActivity.this,
		// MainActivity.class);
		// resultIntent.putExtra("texto", "texto"); // Aquí pasamos la
		// información
		// PendingIntent resultPendingIntent = PendingIntent.getActivity(
		// MainActivity.this, 0, resultIntent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		//
		// NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		// MainActivity.this).setSmallIcon(R.drawable.ic_launcher)
		// .setContentTitle("titulo").setContentText("texto")
		// .setContentIntent(resultPendingIntent) // <- Atencion aqui!
		// .setAutoCancel(true);
		// NotificationManager mNotificationManager = (NotificationManager)
		// MainActivity.this
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(1, mBuilder.build());

		//
		//
		// Intent resultIntent = new Intent(MainActivity.this,
		// MainActivity.class);
		// PendingIntent resultPendingIntent =
		// PendingIntent.getActivity(
		// MainActivity.this,
		// 0,
		// resultIntent,
		// PendingIntent.FLAG_UPDATE_CURRENT
		// );
		//
		// NotificationCompat.Builder mBuilder =
		// new NotificationCompat.Builder(MainActivity.this)
		// .setSmallIcon(R.drawable.ic_launcher)
		// .setContentTitle("titulo")
		// .setContentText("texto")
		// .setContentIntent(resultPendingIntent) // <- Atencion aqui!
		// .setAutoCancel(true);
		// NotificationManager mNotificationManager =
		// (NotificationManager)
		// MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(1, mBuilder.build());
		//

		arrayCamp = new ArrayList<Campanna>();

		// Bundle b = getIntent().getExtras();
		// login = b.getString("login");
		// user_id = b.getString("user_id");

		login = c.getToken();
		user_id = c.getUsuario();

		descon = new TareaWSLogOut();

		// texto = (TextView) findViewById(R.id.titulo);

		// lblResultado = (TextView) findViewById(R.id.texto);
		lstClientes = (ListView) findViewById(R.id.lista);

		proponCamp = (Button) findViewById(R.id.proponCampanna);

		tituloCamp = (TextView) findViewById(R.id.tituloCampannas);
		// Button obtener = (Button) findViewById(R.id.button1);

		// obtener.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {

		if (!verificaConexion(MainActivity.this)) {
			Toast.makeText(getBaseContext(),
					"Comprueba tu conexión a Internet.", Toast.LENGTH_SHORT)
					.show();
		}
		tarea = new TareaWSListar();
		tarea.execute();

		// }
		// });

		proponCamp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ProponCampanna.class);
				startActivity(intent);
			}
		});

	}

	private class TareaWSListar extends AsyncTask<String, Integer, Boolean> {

		private String brand;

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MainActivity.this);
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
			String url = "http://biddusavatar.s3.amazonaws.com/5bdf1ca694cc9932c273f63a0876ba39a9a32299.png?1402655684";
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

			//
			// String auth = new String(Base64.encode(("mrmiyago" + ":"
			// + "piscolabis").getBytes(), Base64.URL_SAFE
			// | Base64.NO_WRAP));

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet del = new HttpGet(c.getMainListar());

			HttpGet campa = new HttpGet(c.getMainListarCampa(user_id));

			del.setHeader("Content-type", "application/json");

			del.addHeader("Accept", "application/json");
			
			del.addHeader("Authorization", "Token token=\"" + login + "\"");

			campa.setHeader("Content-type", "application/json");

			campa.addHeader("Accept", "application/json");

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

							break;
						} else {

							campanna = new Campanna(imagen, title,

							getResources().getDrawable(R.drawable.cross));
						}

					}

					if (lista.length == 0) {
						campanna = new Campanna(imagen, title, getResources()
								.getDrawable(R.drawable.cross));

					}

					arrayCamp.add(campanna);

					campañas[i] = " " + marca + " " + modelo + " ";

				}

				// directivo = new
				// Directivo(getResources().getDrawable(R.drawable.facebook),title);
				// arraydir.add(directivo);

				// titulos[i] = " " + title;

				lstClientes.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Intent intent = new Intent(MainActivity.this,
								Activity1.class);
						intent.putExtra("id", position + 1);
						intent.putExtra("user_id", user_id);
						intent.putExtra("login", login);

						startActivity(intent);

					}
				});

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
					// PushService.setDefaultPushCallback(MainActivity.this,
					// MainActivity.class);
					// ParseInstallation.getCurrentInstallation()
					// .saveInBackground();
					// ParseInstallation.getCurrentInstallation()
					// .deleteInBackground();

					proponCamp.setVisibility(1);

					// Rellenamos la lista con los resultados
					// ArrayAdapter<String> adaptador = new
					// ArrayAdapter<String>(
					// MainActivity.this,
					// android.R.layout.simple_list_item_1, titulos);

					tituloCamp.setVisibility(1);

					AdapterCampannas adapter = new AdapterCampannas(
							MainActivity.this, arrayCamp);

					lstClientes.setAdapter(adapter);
					pDialog.dismiss();
				}
			} catch (Exception e) {
				Log.e("ServicioRest", "Error del PostExecute", e);

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.logOut) {
			descon.execute();

			return true;
		} else if (id == R.id.tutorial) {
			// desde aqui habría que llamar a la primera imagen tutorial de la
			// app

			Intent intent = new Intent(MainActivity.this, Tutorial.class);
			Tutorial.cont = 2;
			// intent.putExtra("user_id", user_id);
			// intent.putExtra("login", login);
			//
			startActivity(intent);
			finish();
			// Toast t = Toast.makeText(MainActivity.this,
			// "Aqui iria el perfil",
			// Toast.LENGTH_SHORT);
			// t.show();

			return true;
		} else if (id == R.id.misCampannas) {

			Intent intent = new Intent(MainActivity.this, Perfil.class);
			intent.putExtra("user_id", user_id);
			intent.putExtra("login", login);

			startActivity(intent);

			return true;
		} else if (id == R.id.exit) {
			descon.execute();

			return true;
		}

		return super.onMenuItemSelected(featureId, item);

	}

	// private void onRestartFallo() {
	//
	// finish();
	// }

	private class TareaWSLogOut extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();

			HttpDelete delete = new HttpDelete(c.getLogOut());

			delete.setHeader("content-type", "application/json");
			delete.addHeader("accept", "application/json");

			delete.addHeader("Authorization", "Token token=\"" + login + "\"");

			try {
				// Construimos el objeto cliente en formato JSON
				// JSONObject parentData = new JSONObject();
				// JSONObject dato = new JSONObject();

				// StringEntity entity = new StringEntity(dato.toString());
				// entity.setContentType("Token token=\"" + login + "\"");

				// httpClient.execute(delete);

				HttpResponse resp = httpClient.execute(delete);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);
				//
				// String token = "";
				// token = respStr.substring(respStr.lastIndexOf(":"),
				// respStr.length() - 3);
				//
				// Log.v("Token", token);
				//
				// login = token.substring(2);
				// Log.v("LOGIN", login);
				//
				// if (!respStr.equals("true"))
				// resul = false;
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {

			// Intent intent = new Intent(MainActivity.this,
			// MainActivity.class);
			// Log.v("Login final", login);
			// intent.putExtra("login", login);

			c.setToken("");
			c.setUsuario("");

			if (respStr.contains("true")) {

				Toast t = Toast.makeText(MainActivity.this, "Hasta pronto",
						Toast.LENGTH_SHORT);

				t.show();

				finish();
			}

			// pass.setText("");
			// } else {
			//
			// Toast t = Toast.makeText(Login.this, "Bienvenido",
			// Toast.LENGTH_SHORT);
			//
			// t.show();
			// startActivity(intent);

			// user.setText("");
			// pass.setText("");

			// }

		}
	}

	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);

		// moveTaskToBack(true);
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

	public static boolean verificaConexion(Context ctx) {
		boolean bConectado = false;
		ConnectivityManager connec = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// No sólo wifi, también GPRS
		NetworkInfo[] redes = connec.getAllNetworkInfo();
		// este bucle debería no ser tan ñapa
		for (int i = 0; i < 2; i++) {
			// ¿Tenemos conexión? ponemos a true
			if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
				bConectado = true;
			}
		}
		return bConectado;
	}

}
