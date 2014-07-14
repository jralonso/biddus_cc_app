package com.occulus.rest;

import java.util.ArrayList;

import com.example.rest.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterCampannas extends BaseAdapter{
	
	protected Activity activity;
	protected ArrayList<Campanna> items;

	public AdapterCampannas(Activity activity, ArrayList<Campanna> items) {
	    this.activity = activity;
	    this.items = items;
	  }

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Generamos una convertView por motivos de eficiencia
		View v = convertView;
		
		//Asociamos el layout de la lista que hemos creado
		if(convertView == null){
			LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.itemlista, null);
		}
		
		// Creamos un objeto directivo
		Campanna dir = items.get(position);

		
		//Rellenamos la fotografía
		ImageView foto = (ImageView) v.findViewById(R.id.foto);
		foto.setImageBitmap(dir.getFoto2());
//		foto.setImageDrawable(dir.getFoto());
		
		
		ImageView img = (ImageView) v.findViewById(R.id.icon);
		img.setImageDrawable(dir.getImg());
		
		
		//Rellenamos el nombre
		TextView titulo = (TextView) v.findViewById(R.id.titulo);
		titulo.setText(dir.getTitulo2());
		
	
		
		
		//Rellenamos el cargo
//		TextView modelo = (TextView) v.findViewById(R.id.modelo);
//		modelo.setText(dir.getModelo());
		
		// Retornamos la vista
		return v;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
