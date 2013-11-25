/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serverconsoleapp;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author TempL
 */
public class SeriHaberlesme implements SerialPortEventListener{
private static SeriHaberlesme seriHaberlesme;
    SerialPort serialPort;
private BufferedReader input;
private OutputStream output;
private static final int TIME_OUT=1000;
private static final int DATA_RATE=9600;


    private SeriHaberlesme() {
    initialize();
    }

    public static SeriHaberlesme getSeriHaberlesme() {
        if(seriHaberlesme==null){
            return new SeriHaberlesme();
        }
        return seriHaberlesme;
    }
    
public void initialize(){
    CommPortIdentifier portId=null;
    Enumeration portEnum=CommPortIdentifier.getPortIdentifiers();
    while(portEnum.hasMoreElements()){
        CommPortIdentifier currPortId=(CommPortIdentifier)portEnum.nextElement();
        if(currPortId.getName().equals("COM4")){
            portId=currPortId;
            break;
        }
    }
    if(portId==null){
        System.out.println("Port bulunamadı");
    return;
    }
       try {
           
                serialPort=(SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
                	serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
                input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                output = serialPort.getOutputStream();
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
                
       } catch (TooManyListenersException ex) {
            Logger.getLogger(SeriHaberlesme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SeriHaberlesme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(SeriHaberlesme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortInUseException ex) {
                Logger.getLogger(SeriHaberlesme.class.getName()).log(Level.SEVERE, null, ex);
            }
     
    }
    
public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
    @Override
    public synchronized  void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
                               
                               
                              //  androidOutput.writeUTF(inputLine);
				//System.out.println(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
    }
        public void sendData(String data){
        try {
            output.write(data.getBytes());
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
