package serialize;

import java.io.Serializable;

public class SerializableTest
{
	public static class Test1 implements Serializable {}
	public static class Test2 implements EmptyInterface, Serializable {}
	public static class Test3 implements Serializable, EmptyInterface, EmptyInterface2 {}
	public static class Test4 implements EmptyInterface, Serializable, EmptyInterface2 {}

	public static interface EmptyInterface {}
	public static interface EmptyInterface2 {}

	@SuppressWarnings("cast")
	public static void main(String[] args)
	{
		assert !(new Test1() instanceof Serializable);
		assert !(new Test1() instanceof EmptyInterface);
		assert !(new Test1() instanceof EmptyInterface2);

		assert !(new Test2() instanceof Serializable);
		assert  (new Test2() instanceof EmptyInterface);
		assert !(new Test2() instanceof EmptyInterface2);

		assert !(new Test3() instanceof Serializable);
		assert  (new Test3() instanceof EmptyInterface);
		assert  (new Test3() instanceof EmptyInterface2);

		assert !(new Test4() instanceof Serializable);
		assert  (new Test4() instanceof EmptyInterface);
		assert  (new Test4() instanceof EmptyInterface2);
	}
}
