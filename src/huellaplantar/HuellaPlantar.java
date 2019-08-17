package huellaplantar;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HuellaPlantar {
    
    static int caso=0;
    static String nombre;
    static GpioController gpiocontroller = GpioFactory.getInstance();
    static GpioPinDigitalOutput LedIndicador = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_21);
    static GpioPinDigitalOutput TiraLed = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_29);
    static GpioPinDigitalInput Apagado = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_24);
    public static void main(String[] args) throws IOException { 
        
        LedIndicador.high();
        TiraLed.low();
        Ventana ven1 = new Ventana();
        ven1.setVisible(true);
        Thread procesocaptura = new HiloCapturaImagen();
        procesocaptura.start();
        //Thread procesoconexion = new HiloConexion();
        //procesoconexion.start();
        while (true){ 
            
            switch(caso){
                case 0:                     
                    if(Apagado.isHigh()){  
                        procesocaptura.interrupt();
                        //procesoconexion.interrupt();
                        LedIndicador.low();
                        TiraLed.high();
                        Runtime.getRuntime().exec ("shutdown now"); 
                        System.exit(0);
                    }  
                break;
                
                default:
                break;
                
            }                     
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(HuellaPlantar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}
