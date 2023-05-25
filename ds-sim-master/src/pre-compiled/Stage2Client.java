/*
 * COMP3100, S1, 2023 - Stage 2 of 2
 * Author@ Ravi Inder Singh
 * SID: 46131434
 */

 import java.io.*; // for using the Input output funtion
 import java.net.*; // for using the socket function
 import java.util.ArrayList;
 
 public class Stage2Client {
 
     public static void main(String args[]){
         Socket socket = null;
         try {       // using set of try catch for exception handling such as error encountered during connection set-up
             int PORTNUM = 50000; // avoiding magic numbers
             socket = new Socket("localhost",PORTNUM);
 
             DataOutputStream dataOut=new DataOutputStream(socket.getOutputStream()); 
             BufferedReader dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
             String serverReply = "";
             String clientMsg = "";
 
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
 // ----------------------------------End of three way handshake----------------------------------------------
 
 //================================Global variables===========================================================
 
             int nRecs = 0; //number of servers
             int jobID = 0; // will be used for SCHD function
             String recordsArr[]; // recording server's reply into string array 
 
 /*=--------------------=-=-===----------------Looping through-==========================================------------- */
 
         while(!serverReply.equals("NONE")){ // if server got one or more than one job. 
             
 
 //------------------------------- finding largest server and its type-----------------------------------
             if(serverReply.contains("JOBN")) {
 
             String temp = serverReply;

             //-------------------------
             try {
                 String arrOfStr[] = temp.split(" ",-1);
                 clientMsg = "GETS Avail "+ arrOfStr[4] + " " + arrOfStr[5] + " " + arrOfStr[6] +"\n";
                 dataOut.write(clientMsg.getBytes());
                 dataOut.flush();
                 serverReply = dataIn.readLine();
                 System.out.println("Server says "+serverReply);

                 temp = serverReply;
                 recordsArr = temp.split(" ", -1);

                 if(Integer.parseInt(recordsArr[1])!=0) { // no server readily available
                    System.out.println("Inside the avail function");
                    nRecs = Integer.parseInt(recordsArr[1]);
                    recordsArr = new String[nRecs];

                    clientMsg = "OK\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();

                    for(int i = 0; i < nRecs; i++){
                        recordsArr[i] = dataIn.readLine();
                        System.out.println("Server says " +recordsArr[i]);
                    }

                    clientMsg = "OK\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();

                    String firstServer[] = recordsArr[0].split(" ", -1);
                    
                    jobID = Integer.parseInt(arrOfStr[2]);
                    clientMsg = "SCHD" + " " + jobID +" " + firstServer[0] + " " + firstServer[1] + "\n";
                    System.out.println("Job is schd as = "+clientMsg);
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();
                    System.out.println("Server says "+serverReply);

                 } else{
                    System.out.println("Inside the capable function");
                    clientMsg = "OK\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();
                    System.out.println("Server says "+serverReply);

                    clientMsg = "GETS Capable "+ arrOfStr[4] + " " + arrOfStr[5] + " " + arrOfStr[6] +"\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();
                    System.out.println("Server says "+serverReply);

                    temp = serverReply;
                    recordsArr = temp.split(" ", -1);

                    nRecs = Integer.parseInt(recordsArr[1]);
                    recordsArr = new String[nRecs];

                    clientMsg = "OK\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();

                    for(int i = 0; i < nRecs; i++){
                        recordsArr[i] = dataIn.readLine();
                        System.out.println("Server says " +recordsArr[i]);
                    }

                    clientMsg = "OK\n";
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();

                    String firstServer[] = recordsArr[0].split(" ", -1);
                    
                    jobID = Integer.parseInt(arrOfStr[2]);
                    clientMsg = "SCHD" + " " + jobID +" " + firstServer[0] + " " + firstServer[1] + "\n";
                    System.out.println("Job is schd as = "+clientMsg);
                    dataOut.write(clientMsg.getBytes());
                    dataOut.flush();
                    serverReply = dataIn.readLine();
                    System.out.println("Server says "+serverReply);

                 }   
             } catch (Exception e) {
                 System.out.println("Invalid array:"+e.getMessage());
             }   
         }

         clientMsg = "REDY\n";
         dataOut.write(clientMsg.getBytes());
         dataOut.flush();
         serverReply = dataIn.readLine();
         System.out.println("Server says "+serverReply);
     }
 /*-------------------------------Terminating the connection-------------------------------------------------*/
             clientMsg = "QUIT\n";
             dataOut.write(clientMsg.getBytes());
             dataOut.flush();
             serverReply = dataIn.readLine();
         //    System.out.println("Server says "+serverReply);
 
 /*--------------------------------------------------------------------------------------------------------- */
             dataOut.close();
 
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
         
 