import java.util.HashMap;
import java.net.ServerSocket;
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
}
