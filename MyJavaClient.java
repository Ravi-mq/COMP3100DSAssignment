/*
 * COMP3100, S1, 2023 - Stage 1 of 2
 * Author@ Ravi Inder Singh
 * SID: 46131434
 */

 import java.io.*; // for using the Input output funtion
 import java.net.*; // for using the socket function
 import java.util.ArrayList;
 
 public class MyJavaClient {
 
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
 
             boolean executed = false; // for checking largest server just once.
             int nRecs = 0; //number of servers
             String LServerName =""; // server with highest number of cores.
             int maxCores = 0; // maximum number of cores.
             int numLServer = 0; // number of servers of highest core type
             int jobID = 0; // will be used for SCHD function
             int count = 0; // keeps track of various counts, states at different times.
             int LserverID = 0; // largest first server ID
             String recordsArr[]; // recording server's reply into string array 
             ArrayList<Integer> serverIDs = new ArrayList<>();
 
 /*=--------------------=-=-===----------------Looping through-==========================================------------- */
 
         while(!serverReply.equals("NONE")){ // if server got one or more than one job. 
             
             
 
 //------------------------------- finding largest server and its type-----------------------------------
             if(!executed) {
 
             String temp = "";
             clientMsg = "GETS All\n";
             dataOut.write(clientMsg.getBytes());
             dataOut.flush();
             temp = dataIn.readLine();
             System.out.println("Server says "+temp);
 
             //-----------Getting total number of server--------------
             try {
                 String arrOfStr[] = temp.split(" ",-1);
 
                     nRecs = Integer.parseInt(arrOfStr[1]); // Getting nRecs from "DATA nRecs recSize"
 
                     clientMsg = "OK\n";
                     dataOut.write(clientMsg.getBytes());
                     dataOut.flush();
 
                      recordsArr = new String[nRecs]; // creating string array to hold description of servers
 
                 for(int i = 0; i < nRecs; i++){
                     recordsArr[i] = dataIn.readLine();
                     System.out.println("Server says " +recordsArr[i]);
                 }
                     arrOfStr = recordsArr[0].split(" ",-1);
                     maxCores = 0;
                      count = 0;
                 for(int i = 0; i < nRecs; i++){
                     arrOfStr = recordsArr[i].split(" ",-1);
                     if(maxCores < Integer.parseInt(arrOfStr[4])){
                         maxCores =  Integer.parseInt(arrOfStr[4]);
                         LServerName = arrOfStr[0]; // largest server name
                     }
                 }
                 clientMsg = "OK\n";
                 dataOut.write(clientMsg.getBytes());
                 dataOut.flush();
                 temp = dataIn.readLine();
                  
                 System.out.println("Server says "+temp);
                 
                 System.out.println("ArrofStrings "+recordsArr[count]);
 
                     
                     count = 0;
               //      LServerName = arrOfStr[0];
                     System.out.println("Lservername "+LServerName);
                     for(int i = 0; i<nRecs; i++){
                           arrOfStr = recordsArr[i].split(" ",-1);
                         if(arrOfStr[0].equals(LServerName)){
                             arrOfStr = recordsArr[i].split(" ",-1);
                             serverIDs.add(Integer.parseInt(arrOfStr[1]));
                         }
                     }
                 numLServer = serverIDs.size();
                 LserverID = serverIDs.get(0);
                 System.out.println("Number of servers in "+LServerName+" is = "+numLServer);
                 System.out.println("Largest server ID is: "+ LserverID);
                 System.out.println("And Server ids are : "+ serverIDs);
             //    count = 0;
                 
 
                 
             } catch (Exception e) {
                 System.out.println("Invalid array:"+e.getMessage());
             }
                 executed = true;
                 
         }
 
             
 //---------------------------------------ending the Gets all function---------------------------------------------------------
 
 
 //-------------------------------------Scheduling of the jobs-----------------------------------------------------------------
             
                 recordsArr = serverReply.split(" ",-1);
                 //if(recordsArr[0].equals("JOBN") || recordsArr[0].equals("JCPL")){
                 if(recordsArr[0].equals("JOBN")){
                     jobID = Integer.parseInt(recordsArr[2]);
                     clientMsg = "SCHD" + " " + jobID +" " + LServerName + " " + LserverID + "\n";
                     System.out.println("Job is schd as = "+clientMsg);
                     dataOut.write(clientMsg.getBytes());
                     dataOut.flush();
                     serverReply = dataIn.readLine();
                     System.out.println("Server says "+serverReply);
                 }
                 
                 // checking for "JCPL endTime jobID serverType serverID"
                 if(recordsArr[0].equals("JCPL")){
                     clientMsg = "REDY\n";
                     dataOut.write(clientMsg.getBytes());
                     dataOut.flush();
                     serverReply = dataIn.readLine();
                     System.out.println("Server says "+serverReply);
                     continue;
                 }
 
                 if(serverIDs.get(serverIDs.size()-1) == LserverID){
                     LserverID = serverIDs.get(0);
                 }else{
                 LserverID++;
                 }         
                 executed = true;
 
             //  sending REDY to receive jobs
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
         
 