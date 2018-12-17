package com.landa.alertmodule.clases;

public class Constante {

    public final static String CONEXION="https://fvlalert.herokuapp.com/";

  /**Contsantes que indican el tipo de usuario que se registrará*/
    public  final static int ROL_AMBULATORIO=4;
    public  final static int ROL_HOSPITALARIO=5;
    public  final static int ROL_ACOMPAÑANTE=6;
    public final static int ROL_ENCARGADO=7;

     static String idApp;
     static String idDispositivo;

     static String macBluetooth;
     static String nomreBluetooth;


     static String EstadoAlrma="desactivada";

     static String usrename;
     static String hotmail;

     static boolean AlarmaActivada=false;



    static String idUsuarioPediatrico;
     static String idUsuarioEncargado;
     public final static String ID_APP_MOVIL=idApp;
     public final static String ID_MAC_DISPODITIVO=idDispositivo;

    /**TipoDocumento del usuario*/
    public  final static int TIPO_DOC_CEDULA=1;
    public final static int TIPO_DOC_IDENTIDAD=2;


    public static String getMacBluetooth() {
        return macBluetooth;
    }

    public static void setMacBluetooth(String macBluetooth) {
        Constante.macBluetooth = macBluetooth;
    }

    public static String getNomreBluetooth() {
        return nomreBluetooth;
    }

    public static void setNomreBluetooth(String nomreBluetooth) {
        Constante.nomreBluetooth = nomreBluetooth;
    }



    public static String getIdUsuarioPediatrico() {
        return idUsuarioPediatrico;
    }

    public static void setIdUsuarioPediatrico(String idUsuarioPediatrico) {
        Constante.idUsuarioPediatrico = idUsuarioPediatrico;
    }

    public static String getIdUsuarioEncargado() {
        return idUsuarioEncargado;
    }

    public static void setIdUsuarioEncargado(String idUsuarioEncargado) {
        Constante.idUsuarioEncargado = idUsuarioEncargado;
    }

    public static String getIdApp() {
    return idApp;
  }

  public static void setIdApp(String idApp) {
    Constante.idApp = idApp;
  }

  public static String getIdDispositivo() {
    return idDispositivo;
  }

  public static void setIdDispositivo(String idDispositivo) {
    Constante.idDispositivo = idDispositivo;
  }


    public static boolean isAlarmaActivada() {
        return AlarmaActivada;
    }

    public static void setAlarmaActivada(boolean alarmaActivada) {
        AlarmaActivada = alarmaActivada;
    }

    public static String getUsrename() {
        return usrename;
    }

    public static void setUsrename(String usrename) {
        Constante.usrename = usrename;
    }

    public static String getHotmail() {
        return hotmail;
    }

    public static void setHotmail(String hotmail) {
        Constante.hotmail = hotmail;
    }

    public static String getEstadoAlrma() {
        return EstadoAlrma;
    }

    public static void setEstadoAlrma(String estadoAlrma) {
        EstadoAlrma = estadoAlrma;
    }
}
