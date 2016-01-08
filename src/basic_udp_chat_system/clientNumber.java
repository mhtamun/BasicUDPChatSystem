package basic_udp_chat_system;
/**
 *
 * @author Maruf
 */
public final class clientNumber{
    private static int x = 0;

    public static int getClientNumber(){
        return x;
    }

    public static void setClientNumber(int cn){
        clientNumber.x = cn; 
    }
}
