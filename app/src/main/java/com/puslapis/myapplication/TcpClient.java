package com.puslapis.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

//public static final String SERVER_IP = "192.168.8.107"; //your computer IP address
//public static final int SERVER_PORT = 7050;

public class TcpClient {
    public static final String TAG = "Main -> TCP";

    public static final String SERVER_IP = "192.168.8.107"; //your computer IP address
    public static final int SERVER_PORT = 7050;
    // message to send to the server
    private String mServerMessage;
    // sends message received notifications
    private OnMessageReceived mMessageListener = null;
    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to read messages from the server
    private BufferedReader mBufferIn;
    public boolean isConnected = false;

    private OutputStream mOutputStream;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message to the server after creating a thread for that purpose
     *
     * @param message packet to send
     */
    public void sendMessage_thread(final byte[] message) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sendMessage_noThread(message);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /**
     * Sends the message to the server
     *
     * @param message packet to send
     */
    public void sendMessage_noThread(final byte[] message) {
        if (mOutputStream != null) {
            try {
                mOutputStream.write(message, 0, message.length);
                mOutputStream.flush();
            } catch (IOException e) {
                Log.d(TAG, "run: " + e.getMessage());
            }
//            Log.d(TAG, "sendMessage_thread: sent");
        }
    }


    /**
     * Close the connection and release the members
     */
    public void stopClient() {

        mRun = false;

        if (mOutputStream != null) {
            try {
                mOutputStream.flush();
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        mMessageListener = null;
        mBufferIn = null;
        mOutputStream = null;
        mServerMessage = null;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            Log.d("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server

            try (Socket socket = new Socket(serverAddr, SERVER_PORT)) {
                socket.setTcpNoDelay(true);

                //for outgoing packets
                mOutputStream = socket.getOutputStream();

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                isConnected = true;


                //in this while the client listens for the messages sent by the server
                while (mRun) {

                    mServerMessage = mBufferIn.readLine();

                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }

                }

                Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                isConnected = false;
            }

        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }

    }

    //Declare the interface. The method messageReceived(String message) will must be implemented in the Activity
    //class at on AsyncTask doInBackground
    public interface OnMessageReceived {
        void messageReceived(String message);
    }

}