/*
 * COMP3100, S1, 2023 - Stage 1 of 2
 * Author@ Ravi Inder Singh
 * SID: 46131434
 */

import java.io.*; // for using the Input output funtion
import java.net.*; // for using the socket function

public class MyJavaClient {

    public static void main(String args[]){
        Socket socket = null;
        try {       // using set of try catch for exception handling such as error encountered during connection set-up
            int PORTNUM = 50000; // avoiding magic numbers
            socket = new Socket("localhost",PORTNUM);

            DataOutputStream dataOut=new DataOutputStream(socket.getOutputStream()); 
            BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String serverReply, clientMsg;

            /* Initiating 3 way handshake */
            //1
            clientMsg = "HELO\n";
            dataOut.write(clientMsg.getBytes());
            dataOut.flush();
            serverReply = dataIn.readLine();
            System.out.println("Server says "+serverReply);
            
            //2
            String getUsername =  System.getProperty("user.name");
            clientMsg = "AUTH "+getUsername+"\n";
            dataOut.write(clientMsg.getBytes());
            dataOut.flush();
            serverReply = dataIn.readLine();
            System.out.println("Server says "+serverReply);

            //3
            clientMsg = "REDY\n";
            dataOut.write(clientMsg.getBytes());
            dataOut.flush();
            serverReply = dataIn.readLine();
            System.out.println("Server says "+serverReply);

            if(!serverReply.equals("OK")){ //  checking if the 3-way handshake was successful
                throw UnknownHostException("Unsuccessful 3 way handshake");
            }

            boolean executed = false; // for checking largest server just once.
            int nRecs = 0; //number of servers
            
            while(!serverReply.equals("NONE")){ // if server got one or more than one job. 
            
            //  sending REDY to receive jobs
            clientMsg = "REDY\n";
            dataOut.write(clientMsg.getBytes());
            dataOut.flush();
            serverReply = dataIn.readLine();
            System.out.println("Server says "+serverReply);

            // separating job description into different variables.
            String newJob = "";
            
            
            // finding largest server and its type
            

            clientMsg = "GETS All\n";
            dataOut.write(clientMsg.getBytes());
            dataOut.flush();
            serverReply = dataIn.readLine();
            System.out.println("Server says "+serverReply);

            //-----------Getting total number of server--------------
            

            }

        } catch (UnknownHostException e){
            System.out.println("Socket:"+e.getMessage());
            }
        catch (EOFException e){
            System.out.println("EOF:"+e.getMessage());
            }
        catch (IOException e){
            System.out.println("IO:"+e.getMessage());
        }
        finally {
            if(socket!=null)
             try {
                socket.close();
            }catch (IOException e){
                System.out.println("close:"+e.getMessage());
            }
        }
            
    }   
}
        
