package org.ketsu.serialize.transform;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class MethodExceptionTransformer implements ClassFileTransformer
{
	protected final Map<String, Collection<String>> toRemove = new HashMap<>();

	public MethodExceptionTransformer(String args)
	{
		if(args != null)
		{
			for(String a : args.split(","))
			{
				if(!a.isEmpty())
				{
					String[] b = a.split("=", 2);

					if(b.length == 2)
					{
						toRemove.put(b[0].replace(".", "/"), Arrays.asList(b[1].split("\\+")));
					}
					else
					{
						throw new IllegalArgumentException();
					}
				}
			}
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFile) throws IllegalClassFormatException
	{
		Collection<String> methods = toRemove.get(className);

		if(methods == null)
		{
			return classFile;
		}

		try
		{
			ClassPool cp = ClassPool.getDefault();

			CtClass ctCls = cp.makeClass(new ByteArrayInputStream(classFile));

			for(String method : methods)
			{
				for(CtMethod ctMethod : ctCls.getDeclaredMethods(method))
				{
					ctMethod.setBody("{ throw new SecurityException(\"Not allowed to access method "+method+"\"); }");
				}
			}

			return ctCls.toBytecode();
		}
		catch(Throwable e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void premain(String args, Instrumentation instrumentation)
	{
		instrumentation.addTransformer(new MethodExceptionTransformer(args));
	}
}
