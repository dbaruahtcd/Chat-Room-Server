import java.util.HashMap;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;


public class ChatRoom {
	
	private String name;
	private int roomRef;
	private HashMap<Integer,ClientInfo> clients = new HashMap<Integer, ClientInfo>();

	
	//constructor
	protected ChatRoom(String name, int roomRef)
	{
		this.name = name;
		this.roomRef = roomRef;
	}
	
	protected String getName()
	{
		return name;
	}
	
	protected void setName(String name)
	{
		this.name = name;
	}
	
	protected HashMap<Integer, ClientInfo> getClients()
	{
		return clients;
	}
	
	protected void addClient(ClientInfo client)
	{
		clients.put(client.getId(),client);
		String joined = "JOINED_CHATROOM: " + name + "\n SERVER_IP: "+ client.getSocket().getLocalAddress().toString().substring(1) + "\nPORT: "+ client.getSocket().getLocalPort() + "\nROOM_REF: "+ roomRef + "\nJOIN_ID: "+ client.getId()+ "\n";
		sendMessage(joined,client);
		chat(client.getId(), client.getName() + "has joined the chatroom.");
	}
	
	public void sendMessage(String message, ClientInfo client)
	{
		try
		{
			PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream(), true);
			System.out.println(message);
			writer.println(message);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void chat(Integer clientId, String message)
	{
		Iterator<Entry<Integer, ClientInfo>> iterator = clients.entrySet().iterator();
		ClientInfo sender = clients.get(clientId);
		if(sender != null)
		{
			String sendString = "CHAT: " + roomRef + "\nCLIENT_NAME"+ sender.getName() + "\nMESSAGE: "+ message + "\n";
			while(iterator.hasNext())
			{
				ClientInfo receipient = iterator.next().getValue();
				sendMessage(sendString, receipient);
			}
		}
		else
		{
			
		}
	}
}
