package com.occulus.rest;

public final class Configuracion {
	private String urlLogin = "http://occulus-stg.herokuapp.com/api/v1/sessions";
	private String activity1Obtener;
	private String urlShare = "http://www.biddus.com";
	private String activity1Insertar;
	private String mainListar = "http://occulus-stg.herokuapp.com/api/v1/campaigns";
	private String mainListarCampa;
	private String logOut = "http://occulus-stg.herokuapp.com/api/v1/sessions";
	private String registro = "http://occulus-stg.herokuapp.com/api/v1/registrations";

	public String getUrlShare() {
		return urlShare;
	}

	public String getUrlLogin() {
		return urlLogin;
	}

	public String getActivity1Obtener(int id) {
		activity1Obtener = "http://occulus-stg.herokuapp.com/api/v1/campaigns/"
				+ id;
		return activity1Obtener;
	}

	public String getActivity1Insertar(int id) {

		activity1Insertar = "http://occulus-stg.herokuapp.com/api/v1/campaigns/"
				+ id + "/orders";
		return activity1Insertar;
	}

	public String getLogOut() {
		return logOut;
	}

	public String getMainListar() {
		return mainListar;
	}

	public String getMainListarCampa(String user_id) {
		mainListarCampa = "http://occulus-stg.herokuapp.com/api/v1/users/"
				+ user_id + "/orders";
		return mainListarCampa;
	}

	public String getRegistro() {
		return registro;
	}


}