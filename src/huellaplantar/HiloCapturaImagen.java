package huellaplantar;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class HiloCapturaImagen extends Thread {
    
    static ServerSocket server;
    static Socket servidorSocket;
    static int puerto = 9000;
    static ObjectOutputStream oos ;
    
    @Override
    public void run(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {
            server = new ServerSocket(puerto);
        } catch (IOException ex) {
            Logger.getLogger(HiloCapturaImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        servidorSocket = new Socket();
        while (true){
           try{
               System.out.println("Proceso Listo");
                servidorSocket = server.accept();                
                System.out.println("Conexion Establecida");
                
                Mat cam1 = new Mat(), cam11 = new Mat();
        Mat cam2 = new Mat(), cam22 = new Mat();
        Mat captura1 = new Mat(),captura11 = new Mat();
        Mat captura2 = new Mat(),captura22 = new Mat();
        HuellaPlantar.TiraLed.low();
        
        VideoCapture camara1 = new VideoCapture();                        
        camara1.open(0);
        camara1.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
        camara1.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);                 
        while(true){
            if(camara1.isOpened()){
                System.out.println("Imagen Capturada");
                camara1.read(cam1);
                break;
            }else{
                System.out.println("Intentando Abrir la Camara");
                camara1.open(1);
                camara1.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
                camara1.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
            }
        } 
        Imgcodecs.imwrite("Cam1Apagada.jpg", cam1);
        camara1.release();
        HuellaPlantar.TiraLed.high();
        System.out.println("Enciende Leds");
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloCapturaImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        camara1.open(0);
        camara1.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
        camara1.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
        while(true){
            if(camara1.isOpened()){
                System.out.println("Imagen Capturada");
                camara1.read(cam2);
                break;
            }else{
                System.out.println("Intentando Abrir la Camara");
                camara1.open(1);
                camara1.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
                camara1.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
            }
        } 
        Imgcodecs.imwrite("Cam1Encendida.jpg", cam2);
        HuellaPlantar.TiraLed.low();
        camara1.release();
                        
        try {
            Thread.sleep(600);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloCapturaImagen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        VideoCapture camara2 = new VideoCapture();
        camara2.open(1);
        camara2.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
        camara2.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
        while(true){
            if(camara2.isOpened()){
                System.out.println("Imagen Capturada");
                camara2.read(cam11);
                break;
            }else{
                System.out.println("Intentando Abrir la Camara");
                camara2.open(1);
                camara2.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
                camara2.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
            }
        }
                
        Imgcodecs.imwrite("Cam2Apagada.jpg", cam11);
        HuellaPlantar.TiraLed.high();
        System.out.println("Enciende Leds");
        camara2.release();
        try {
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloCapturaImagen.class.getName()).log(Level.SEVERE, null, ex);
        }        
        camara2.open(1);
        camara2.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
        camara2.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
        while(true){
            if(camara2.isOpened()){
                System.out.println("Imagen Capturada");
                camara2.read(cam22);
                break;
            }else{
                System.out.println("Intentando Abrir la Camara");
                camara2.open(1);
                camara2.set(Videoio.CV_CAP_PROP_FRAME_WIDTH,2592);
                camara2.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT,1944);
            }
        } 
        Imgcodecs.imwrite("Cam2Encendida.jpg", cam22);
        HuellaPlantar.TiraLed.low();
        camara2.release();        
        Imgproc.resize(cam1, captura1, new Size(cam1.width()/2, cam1.height()/2));
        Imgproc.resize(cam2, captura2, new Size(cam2.width()/2, cam2.height()/2));                                     
        Imgproc.resize(cam11, captura11, new Size(cam11.width()/2, cam11.height()/2));
        Imgproc.resize(cam22, captura22, new Size(cam22.width()/2, cam22.height()/2));                                
        
        
        

        Mat resta1 = new Mat(captura2.rows(),captura2.cols(),captura2.type());
        Core.subtract(cam2, cam1, resta1);     
        Mat resta2 = new Mat(captura22.rows(),captura22.cols(),captura22.type());
        Core.subtract(cam22, cam11, resta2); 
 
        //Se giran las dos imagenes
        Mat girada1 = new Mat(resta1.cols(),resta1.rows(),resta1.type());
        Mat girada2 = new Mat(resta2.cols(),resta2.rows(),resta2.type());        
        Core.rotate(resta1, girada1, Core.ROTATE_90_COUNTERCLOCKWISE);
        Core.rotate(resta2, girada2, Core.ROTATE_90_COUNTERCLOCKWISE);

        
        //Se recortan las imagenes
        Rect rectCrop1 = new Rect(100, 0, girada1.width()-190, girada1.height()-20);
        Mat cortada1 = new Mat(girada1, rectCrop1);
        Rect rectCrop2 = new Rect(100, 0, girada2.width()-180, girada2.height()-20);
        Mat cortada2 = new Mat(girada2, rectCrop2);

        
        //Se unen las imagenes
        Mat union = new Mat(cortada1.rows(),cortada1.cols()+cortada2.cols(),cortada1.type());
        ArrayList<Mat> sources = new ArrayList<>();
        sources.add(cortada2);
        sources.add(cortada1);
        Core.hconcat(sources, union);
        //Se convierte a RGB
        Mat imagenMat = new Mat(captura1.cols(),captura1.rows(),captura1.type());
        Imgproc.cvtColor(union, imagenMat, Imgproc.COLOR_BGR2RGB); 
        Imgcodecs.imwrite("Resta.jpg", imagenMat);  
                
                byte [] data = new byte[(int) (imagenMat.total() * imagenMat.elemSize())];
                imagenMat.get(0, 0, data);
                BufferedImage out = new BufferedImage(imagenMat.cols(), imagenMat.rows(),BufferedImage.TYPE_3BYTE_BGR);
                out.getRaster().setDataElements(0, 0,imagenMat.cols() , imagenMat.rows(), data);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(out,"jpg",baos);
                baos.flush();
                byte[] imageInByte = baos.toByteArray();  
                oos=new ObjectOutputStream(servidorSocket.getOutputStream());             
                oos.writeObject(imageInByte);
                oos.flush();
                oos.close(); 
                baos.flush();
                baos.close();
                servidorSocket.close();                              
            }catch(Exception e){ }           
       }
    }    
}
