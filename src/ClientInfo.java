import java.net.Socket;


public class ClientInfo {
	
	
	private Integer id;
	private String name;
	private Socket socket;
	
	public ClientInfo(int id , String name, Socket socket )
	{
		this.id = id;
		this.name = name;
		this.socket = socket;
	}
	
	
	protected Integer getId()
	{
		return id;
	}
	
	protected void setId(Integer id)
	{
		this.id = id;
	}
	
	protected String getName()
	{
		return name;
	}

	protected void setName(String name)
	{
		this.name = name;
	}
	
	protected Socket getSocket()
	{
		return socket;
	}
}
