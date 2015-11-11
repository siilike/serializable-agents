package org.ketsu.serialize;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Config
{
	public static Collection<String> getUnsafeClasses()
	{
		Set<String> ret = new HashSet<>();

		ret.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");

		ret.add("org.apache.commons.collections4.functors.InvokerTransformer");
		ret.add("org.apache.commons.collections.functors.InvokerTransformer");

		ret.add("org.apache.commons.collections4.functors.InstantiateFactory");
		ret.add("org.apache.commons.collections.functors.InstantiateFactory");

		ret.add("org.apache.commons.collections4.functors.InstantiateTransformer");
		ret.add("org.apache.commons.collections.functors.InstantiateTransformer");

		ret.add("org.springframework.core.SerializableTypeWrapper$MethodInvokeTypeProvider");

		return ret;
	}
}
