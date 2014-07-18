package com.occulus.rest;

public final class Configuracion {
	private String urlLogin = "http://demo.biddus.com/api/v1/sessions";
	private String activity1Obtener;
	private String urlShare = "http://www.biddus.com";
	private String activity1Insertar;
	private String mainListar = "http://demo.biddus.com/api/v1/campaigns";
	private String mainListarCampa;
	private String logOut = "http://demo.biddus.com/api/v1/sessions";
	private String registro = "http://demo.biddus.com/api/v1/registrations";
	private static String token;
	private static String usuario;
	
	
	
	public String getUrlShare() {
		return urlShare;
	}

	public String getUrlLogin() {
		return urlLogin;
	}

	public String getActivity1Obtener(int id) {
		activity1Obtener = "http://demo.biddus.com/api/v1/campaigns/"
				+ id;
		return activity1Obtener;
	}

	public String getActivity1Insertar(int id) {

		activity1Insertar = "http://demo.biddus.com/api/v1/campaigns/"
				+ id + "/cc_orders";
		return activity1Insertar;
	}

	public String getLogOut() {
		return logOut;
	}

	public String getMainListar() {
		return mainListar;
	}

	public String getMainListarCampa(String user_id) {
		mainListarCampa = "http://demo.biddus.com/api/v1/users/"
				+ user_id + "/cc_orders";
		return mainListarCampa;
	}

	public String getRegistro() {
		return registro;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		Configuracion.token = token;
	}

	public static String getUsuario() {
		return usuario;
	}

	public static void setUsuario(String usuario) {
		Configuracion.usuario = usuario;
	}


}