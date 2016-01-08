package basic_udp_chat_system;
/**
 *
 * @author Maruf
 */
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
 
public class server {	
 
    private static HashSet<Integer> portSet = new HashSet<Integer>();
    
    private static ArrayList<HashMap<String, Integer>> clientList = 
            new ArrayList<HashMap<String, Integer>>();

    public static void main(String args[]) throws Exception {

        int serverport = 7777;        

        System.out.println("Usage: UDPServer " + "Now using Port# = " + serverport);

        DatagramSocket udpServerSocket = new DatagramSocket(serverport);        

        System.out.println("Server started...\n");

        while(true) {
            byte[] receiveData = new byte[1024];          

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            udpServerSocket.receive(receivePacket);  
            
            InetAddress clientIP = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            
            System.out.println("Client Connected - Socket Address: " + receivePacket.getSocketAddress());
            System.out.println("Client IP Address & Hostname: " + clientIP + ", " + clientIP.getHostName() + "\n");
            
            String clientMessage = (new String(receivePacket.getData())).trim();
            System.out.println("Client message (Client Post:" + clientPort + "): " + clientMessage);          
            
            portSet.add(clientPort);
		
            String returnMessage = clientPort + ":" + clientMessage;
            
            byte[] sendData  = new byte[1024];
            sendData = returnMessage.getBytes();

            for(Integer port : portSet) {
                if(port != clientPort) {
                    System.out.println("Sending to " + port.toString());
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, port);         
                    udpServerSocket.send(sendPacket);    
                }
            }
        }
    }
}