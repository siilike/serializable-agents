package serialize;

import java.io.ObjectInputStream;

public class Deserializer
{
	public static void main(String[] args) throws Exception
	{
		ObjectInputStream ois = new ObjectInputStream(System.in);

		boolean success = false;

		try
		{
			ois.readObject();
		}
		catch(Throwable e)
		{
			Throwable ex = e;

			do
			{
				if(ex instanceof SecurityException)
				{
					success = true;
					break;
				}
			}
			while((ex = ex.getCause()) != null);

			if(!success)
			{
				throw e;
			}
		}

		assert success;
	}
}
