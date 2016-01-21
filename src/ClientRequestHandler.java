
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
	private boolean killService;
	
	
	public ClientRequestHandler(Socket socket, int joinID, ChatServer server)
	{
		this.socket = socket;
		this.joinID = joinID;
		this.server = server;
	}
	
	
	
	
	@Override
	public void run() {
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			ArrayList<String> lines;
			
			while(!killService)
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
					joinChatroom(chatroomName,clientName);
				}
				
				else if(lines.get(0).startsWith("LEAVE_CHATROOM"))
				{
					String[] line0 = lines.get(0).split(":");
					String chatroomID = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinID = line1[1].substring(1);
					leaveChatroom(chatroomID, joinID);
				}
				
				else if(lines.get(0).startsWith("CHAT"))
				{
					String[] line0 = lines.get(0).split(":");
					String chatroomID = line0[1].substring(1);
					String[] line1 = lines.get(1).split(":");
					String joinID = line1[1].substring(1);
					String[] line3 = lines.get(3).split(":");
					String[] message = lines3[1].substring(1);
					chat(chatroomID, joinID, message);
				}
				
				else if(lines.get(0).startsWith("Hello"))
				{
					writer.println(heloResponse(lines.get(0)));
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
