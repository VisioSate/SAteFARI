package com.visiosate.test0909;

/**
 * Created by Clément on 15/09/2017.
 */

import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClientThread extends Thread{

    String dstAddress;
    int dstPort;
    private boolean running;
    MainActivity.UdpClientHandler handler;
    String msg;
    DatagramSocket socket;
    InetAddress address;

    public UdpClientThread(String addr, int port, MainActivity.UdpClientHandler handler, String msg) {
        super();
        dstAddress = addr;
        dstPort = port;
        this.handler = handler;
        this.msg = msg;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    private void sendState(String state){
        handler.sendMessage(
                Message.obtain(handler,
                        MainActivity.UdpClientHandler.UPDATE_STATE, state));
    }

    @Override
    public void run() {

        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(dstAddress);

            DatagramSocket s = new DatagramSocket();
            int msg_lenght = msg.length();
            byte []message = msg.getBytes();
            DatagramPacket p = new DatagramPacket(message,msg_lenght,address,12345);
            s.send(p);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(socket != null){
                handler.sendEmptyMessage(MainActivity.UdpClientHandler.UPDATE_END);

            }
        }

    }
}