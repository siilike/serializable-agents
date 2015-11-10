package serialize;

public class MethodExceptionTest
{
	public static void main(String[] args) throws Exception
	{
		boolean success = false;

		try
		{
			Runtime.getRuntime().exec("hostname");
		}
		catch(SecurityException e)
		{
			success = true;
		}

		assert success;
	}
}
