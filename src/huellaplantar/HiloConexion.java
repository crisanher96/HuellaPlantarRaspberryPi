package huellaplantar;


import static huellaplantar.HiloCapturaImagen.server;
import static huellaplantar.HiloCapturaImagen.servidorSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HiloConexion extends Thread {
    static ServerSocket server;
    static Socket servidorConexion;
    static int puerto = 8000;
    static BufferedReader entrada;
    static String mensajeRecibido; 
    
    @Override
    public void run(){
        try {
            server = new ServerSocket(puerto);
        } catch (IOException ex) {
            Logger.getLogger(HiloConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        servidorConexion = new Socket();
        while(true){
            try {
                servidorConexion = server.accept();
                System.out.println("Hilo de Conexion Conectado");
                entrada = new BufferedReader(new InputStreamReader(servidorConexion.getInputStream()));
                mensajeRecibido = entrada.readLine();
                System.out.println(mensajeRecibido); 
                
                entrada.close();
                servidorConexion.close();                     
            } catch (IOException ex) {
                Logger.getLogger(HiloConexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
