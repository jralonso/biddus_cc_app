package com.occulus.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.occulus.rest.R;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends Activity {

	private EditText ETemail;
	private String email;
	private EditText ETnombre;
	private String nombre;
	private EditText ETpass;
	private String pass;
	private EditText ETrePass;
	private String rePass;
	private Button enviar;
	private TareaWSRegistro reg;
	private String login;
	private String user_id;
	private String respStr;
	private Configuracion c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registro);

		c = new Configuracion();

		ETemail = (EditText) findViewById(R.id.email);
		ETnombre = (EditText) findViewById(R.id.nombre);
		ETpass = (EditText) findViewById(R.id.pass);
		ETrePass = (EditText) findViewById(R.id.confirmPass);
		enviar = (Button) findViewById(R.id.boton);

		reg = new TareaWSRegistro();

		enviar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				email = ETemail.getText().toString();
				nombre = ETnombre.getText().toString();
				pass = ETpass.getText().toString();
				rePass = ETrePass.getText().toString();

				if (email.length() == 0 || !email.contains("@")
						|| !email.contains(".")) {
					Toast t = Toast
							.makeText(
									Registro.this,
									"El email no puede estar vacío y debe ser un email valido",
									Toast.LENGTH_SHORT);
					t.show();
				} else if (nombre.length() == 0) {
					Toast t = Toast.makeText(Registro.this,
							"El nombre no puede estar vacío",
							Toast.LENGTH_SHORT);
					t.show();
				} else if (!pass.equalsIgnoreCase(rePass) || pass.length() < 8) {
					Toast t = Toast
							.makeText(
									Registro.this,
									"Las contraseñas no coinciden o son demasiado cortas (8 caracteres mínimo)",
									Toast.LENGTH_SHORT);
					t.show();
				} else {

					reg.execute();
				}

			}
		});

	}

	private class TareaWSRegistro extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {

			boolean resul = true;

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost post = new HttpPost(c.getRegistro());

			post.setHeader("content-type", "application/json");
			post.setHeader("accept", "application/json");

			try {
				// Construimos el objeto cliente en formato JSON
				JSONObject parentData = new JSONObject();
				JSONObject dato = new JSONObject();

				dato.put("email", email);
				dato.put("biddus_name", nombre);
				dato.put("password", pass);
				dato.put("password_confirmation", rePass);
				parentData.put("user", dato);

				StringEntity entity = new StringEntity(parentData.toString());
				post.setEntity(entity);

				HttpResponse resp = httpClient.execute(post);
				respStr = EntityUtils.toString(resp.getEntity());

				Log.v("BIEN", respStr);

				JSONObject object = new JSONObject(respStr);
				JSONObject object2 = object.getJSONObject("data");
				JSONObject object3 = object2.getJSONObject("user");

				// Log.v("Bien un token", object2.toString());

				login = object2.getString("auth_token");
				user_id = object3.getString("id");
				Log.v("LOGIN", login);
				Log.v("ID", user_id);

				if (!respStr.equals("true"))
					resul = false;
			} catch (Exception ex) {
				Log.e("ServicioRest", "Error!", ex);
				resul = false;
			}

			return resul;
		}

		protected void onPostExecute(Boolean result) {

			if (respStr.contains("Registered")) {

				Toast t = Toast.makeText(Registro.this,
						"Registrado correctamente", Toast.LENGTH_SHORT);

				t.show();

				Intent intent = new Intent(Registro.this, MainActivity.class);
				// Log.v("Login final", login);
				
//				intent.putExtra("login", login);
//				intent.putExtra("user_id", user_id);

				c.setToken(login);
				c.setUsuario(user_id);
				startActivity(intent);

			} else {
				Toast t = Toast.makeText(Registro.this, "Error en el registro",
						Toast.LENGTH_SHORT);

				t.show();

				finish();
			}

		}
	}

	@Override
	protected void onRestart() {
		finish();
		super.onRestart();
	}
}
