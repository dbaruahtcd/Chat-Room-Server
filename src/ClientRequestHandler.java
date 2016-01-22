
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClientRequestHandler implements Runnable{
	
	private Socket socket ;
	private InputStreamReader streamIn ;
	private ChatServer server;
	private int joinID;
	private boolean killServer;
	
	
	public ClientRequestHandler(Socket socket, int joinID, ChatServer server)
	{
		this.socket = socket;
		this.joinID = joinID;
		this.server = server;
		this.killServer = false;
	}
	
	/* 
	 * This method is used to save to client joining information
	 */
	
	private void joinChatRoom(String chatroomName, String clientName)
	{
		ClientInfo client = new ClientInfo(joinID , clientName, socket);
		server.joinChatRoom(chatroomName, client);
	}
	
	//leave chatroom
	protected void leaveChatRoom(String chatRoomId, String clientId)
	{
		int chatRoom = Integer.parseInt(chatRoomId);
		int client = Integer.parseInt(clientId);
		server.leaveChatRoom(chatRoom, client);
	}
	
	//chat with the group
	protected void chat(String chatRoomId, String clientId, String message)
	{
		int chatRoom = Integer.parseInt(chatRoomId);
		int client = Integer. parseInt(clientId);
		if(!server.chat(chatRoom, client, message)){
			error(1);
		}
	}
	
	protected String helloResponse(String command){
		return command + "\nIP: "+ socket.getLocalAddress().toString().substring(1)+ "\nPort:";
	}
	
	//send an error message back to client
	protected void error(int error)
	{
		try
		{
			PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);
			String errorMessage = "ERROR_CODE" + error + "\nERROR_DESCRIPTION :";
			String description = "";
			if(error == 0)
			{
				description = "Incorrect Format, Not enough lines \n";
			}
			else if( error == 1)
			{
				description = "ChatRoom does not exists\n";
			}
			writer.println(errorMessage + description);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			ArrayList<String> lines;
			
			while(!killServer)
			{
				lines = new ArrayList<String>();
				while(reader.ready())
				{
					lines.add(reader.readLine());
					System.out.println("Received : "+ lines.get(0));
				}
				
				if(lines.isEmpty())
				{
					
				}
				
				else if(lines.get(0).startsWith("JOIN_CHATROOM"))
				{
					String[] line0 = lines.get(0).split(":");
					String chatroomName = line0[1];
					String[] line3 = lines.get(3).split(":");
					String clientName = line3[1];
					joinChatRoom(chatroomName,clientName);
				}
				
				else if(lines.get(0).startsWith("LEAVE_CHATROOM"))
				{
					String[] line0 = lines.get(0).split(":");
					String chatroomID = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinID = line1[1].substring(1);
					leaveChatRoom(chatroomID, joinID);
				}
				
				else if(lines.get(0).startsWith("CHAT"))
				{
					String[] line0 = lines.get(0).split(":");
					String chatroomID = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinID = line1[1].substring(1);
					String[] line3 = lines.get(3).split(":");
					String message = line3[1].substring(1);
					chat(chatroomID, joinID, message);
				}
				
				else if(lines.get(0).startsWith("Hello"))
				{
					writer.println(helloResponse(lines.get(0)));
				}
					
				else if(lines.get(0).startsWith("KILL_SERVICE"))
				{
					killServer = true;
					socket.close();
					server.killService();
					
				}
					
				else
				{
					// Do nothing
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
