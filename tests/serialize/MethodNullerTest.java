package serialize;

public class MethodNullerTest
{
	public static void main(String[] args)
	{
		assert org.apache.commons.collections4.functors.InvokerTransformer.invokerTransformer("toString").transform("notsu") == null;
		assert org.apache.commons.collections.functors.InvokerTransformer.getInstance("toString").transform("notsu") == null;
	}
}
