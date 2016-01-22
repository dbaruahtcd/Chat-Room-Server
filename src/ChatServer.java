import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Executors;
import java.util.Iterator;
import java.util.Map.Entry;


public class ChatServer {
	
	private ServerSocket serverSocket;
	private int clientCount ;
	private int chatRoomCount;
	private HashMap<Integer, ChatRoom> chatrooms = new HashMap<Integer, ChatRoom>();
	private static final ThreadPoolExecutor  EXECUTORSERVICE = (ThreadPoolExecutor) Executors.newFixedThreadPool(6);
	
	
	protected ChatServer(ServerSocket  serverSocket)
	{
		this.serverSocket = serverSocket;
		this.clientCount = 0;
		this.chatRoomCount = 0;
	}
	
	//Start the executor
	public void start(){
		try{
			while(true)
			{
				if(EXECUTORSERVICE.getActiveCount() < EXECUTORSERVICE.getMaximumPoolSize() )
				{
					Socket socket = serverSocket.accept();
					EXECUTORSERVICE.execute(new ClientRequestHandler(socket,clientCount, this));
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}	
	
	//Add client to a chatRoom
	protected void joinChatRoom(String chatRoomName, ClientInfo client)
	{
		ChatRoom chatRoom = findChatRoomByName(chatRoomName);
		if(chatRoom != null)
		{
			chatRoom.addClient(client);
		}
		else
		{
			chatRoom = new ChatRoom(chatRoomName, chatRoomCount);
			chatRoom.addClient(client);
			
			
		}
	}
	
	// find chatroom by name
	public ChatRoom findChatRoomByName(String name)
	{
		Iterator<Entry<Integer, ChatRoom>> iterator = chatrooms.entrySet().iterator();
		while(iterator.hasNext())
		{
			ChatRoom chatroom = iterator.next().getValue();
			if(chatroom.getName().equals(name))
			{
				return chatroom;
			}
		}
		return null;
	}
	
	
	//Remove a client from a chatroom
	protected void leaveChatRoom(int chatroomId, int clientId)
	{
		ChatRoom chatroom = chatrooms.get(chatroomId);
		chatroom.removeClient(clientId);
	}

	//Send message to a chatRoom
	protected boolean chat(Integer chatRoomId, int clientId, String message)
	{
		ChatRoom chatRoom = chatrooms.get(chatRoomId);
		if(chatRoom != null)
		{
			chatRoom.chat(clientId, message);
			return true;
		}
		return false;
	}
	
	//Increment the client count
	protected void addClient(){
		clientCount++;
	}
	
	
	//Stop the server and kill all the threads
	protected void killService()
	{
		try{
			EXECUTORSERVICE.shutdownNow();
			serverSocket.close();
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
	}
	}
	
	protected ChatRoom getChatRoom(int chatRoomId){
		chatrooms.get(chatRoomId);
		return null;
	}
	
	
	// main method
	public static void main(String[] args) throws IOException
	{
		int portNumber = Integer.parseInt(args[0]);
		ChatServer server = new ChatServer(new ServerSocket(portNumber));
		server.start();
	}
}

