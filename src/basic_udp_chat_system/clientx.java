package basic_udp_chat_system;
/**
 *
 * @author Maruf
 */
import java.io.*;
import java.net.*;
 
public class clientx {
 
    public static void main(String args[]) throws Exception {  
 
        int clientport = 7777;
        String host = "localhost";
        
        System.out.println("UDPClient: Now using host = " + host + ", Port# = " + clientport);
 
        InetAddress ia = InetAddress.getByName(host);
 
        SenderThread sender = new SenderThread(ia, clientport);
        sender.start();
        
        ReceiverThread receiver = new ReceiverThread(sender.getSocket());
        receiver.start();
    }
}      
 
class SenderThread extends Thread {
 
    private final InetAddress serverIPAddress;
    private final int serverport;
    private final DatagramSocket udpClientSocket;
    
    private boolean stopped = false;
    
 
    public SenderThread(InetAddress address, int serverport) throws SocketException {
        this.serverIPAddress = address;
        this.serverport = serverport;
        this.udpClientSocket = new DatagramSocket();
        this.udpClientSocket.connect(serverIPAddress, serverport);
    }
    public void halt() {
        this.stopped = true;
    }
    public DatagramSocket getSocket() {
        return this.udpClientSocket;
    }
 
    public void run() {     
        try {    
            byte[] data = new byte[1024];
            data = "Client Online".getBytes();
            
            DatagramPacket firstPacket = new DatagramPacket(data,data.length , serverIPAddress, serverport);
            udpClientSocket.send(firstPacket);
           
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            
            while (true) {
                if (stopped)
                    return;
 
                String clientMessage = inFromUser.readLine();
 
                if (clientMessage.equals("."))
                    break;
 
                byte[] sendData = new byte[1024];
 
                sendData = clientMessage.getBytes();
 
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverport);
                udpClientSocket.send(sendPacket);
 
                Thread.yield();
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }
}   
 
class ReceiverThread extends Thread {
 
    private DatagramSocket udpClientSocket;
    private boolean stopped = false;
 
    public ReceiverThread(DatagramSocket ds) throws SocketException {
        this.udpClientSocket = ds;
    }
 
    public void halt() {
        this.stopped = true;
    }
 
    public void run() {
 
        byte[] receiveData = new byte[1024];
 
        while (true) {            
            if (stopped)
            return;
 
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                udpClientSocket.receive(receivePacket);  
                
                String serverReply =  new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(serverReply);
 
                Thread.yield();
            } 
            catch (IOException ex) {
            System.err.println(ex);
            }
        }
    }
}