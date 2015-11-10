package serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectInputStreamTest
{
	public static class Inaccessible implements Serializable {}
	public static class Accessible implements Serializable { Inaccessible inaccessible = new Inaccessible(); }
	public static class Accessible2 implements Serializable {}

	public static void main(String[] args) throws Exception
	{
		ByteArrayOutputStream bos;
		ObjectOutputStream oos;
		ObjectInputStream ois;

		bos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(bos);
		oos.writeObject(new Accessible());

		ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

		boolean success = true;

		try
		{
			ois.readObject();
		}
		catch(SecurityException e)
		{
			success = false;
		}

		assert !success;

		bos = new ByteArrayOutputStream();
		oos = new ObjectOutputStream(bos);
		oos.writeObject(new Accessible2());

		ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

		try
		{
			ois.readObject();
			success = true;
		}
		catch(SecurityException e)
		{
			e.printStackTrace();
		}

		assert success;
	}
}
