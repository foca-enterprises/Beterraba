package com.focasoft.beterraba.client;

import com.focasoft.beterraba.net.Packet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import org.json.JSONObject;

public class NetworkManager implements Runnable
{
  private final LinkedList<String> OUT_MESSAGES = new LinkedList<>();
  private final LinkedList<Packet> IN_MESSAGES = new LinkedList<>();
  private final String HOST;
  private final int PORT;
  
  private BufferedReader input;
  private BufferedWriter output;
  private Socket socket;
  private Thread thread;
  
  private boolean running;
  private long mod;
  
  public NetworkManager(String hostname, int port)
  {
    this.HOST = hostname;
    this.PORT = port;
  }
  
  public void connect() throws IOException
  {
    socket = new Socket(HOST, PORT);
    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    thread = new Thread(this, "Network Manager");
    running = true;
    thread.start();
  }
  
  public void disconnect() throws IOException
  {
    running = false;
    
    try{
      thread.join();
    } catch(InterruptedException e)
    {
      e.printStackTrace();
    }
    
    socket.close();
    socket = null;
    input = null;
    output = null;
  }
  
  private void processInput(String line)
  {
  
  }
  
  public void sendMessage(String msg)
  {
    synchronized(OUT_MESSAGES)
    {
      OUT_MESSAGES.add(msg);
    }
  }
  
  public void sendMessage(JSONObject json)
  {
    sendMessage(json.toString());
  }
  
  public void sendPacket(Packet packet)
  {
    sendMessage(packet.serialize());
  }
  
  private LinkedList<String> getOut()
  {
    synchronized(OUT_MESSAGES)
    {
      return new LinkedList<>(OUT_MESSAGES);
    }
  }
  
  @Override
  public void run()
  {
    long cMod;
    
    while(running)
    {
      LinkedList<String> out = getOut();
      cMod = mod;
      
      out.forEach(e -> {
        
        try {
          output.write(e);
          output.flush();
        } catch(IOException ioException) {
          ioException.printStackTrace();
        }
        
        OUT_MESSAGES.remove(e);
        ++mod;
      });
      
      String line = null;
      
      try {
        line = input.readLine();
      } catch(IOException e) {
        e.printStackTrace();
      }
  
      if(line != null)
      {
        processInput(line);
        ++mod;
      }
      
      if(mod == cMod)
      {
        try {
          Thread.sleep(100);
        } catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}