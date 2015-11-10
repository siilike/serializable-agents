package org.ketsu.serialize.transform;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class ObjectInputStreamTransformer implements ClassFileTransformer
{
	protected final Set<String> nonSerializables = new HashSet<>();

	public ObjectInputStreamTransformer(String args)
	{
		if(args == null || args.isEmpty())
		{
			nonSerializables.add("org.apache.commons.collections4.functors.InvokerTransformer");
			nonSerializables.add("org.apache.commons.collections.functors.InvokerTransformer");
			nonSerializables.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
		}
		else
		{
			for(String a : args.split(","))
			{
				if(!a.isEmpty())
				{
					nonSerializables.add(a.replace('/', '.'));
				}
			}
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFile) throws IllegalClassFormatException
	{
		if(!className.equals("java/io/ObjectInputStream") || nonSerializables.isEmpty())
		{
			return classFile;
		}

		try
		{
			ClassPool cp = ClassPool.getDefault();

			CtClass ctCls = cp.makeClass(new ByteArrayInputStream(classFile));

			CtMethod ctMethod = ctCls.getDeclaredMethod("resolveClass");

			StringBuilder b = new StringBuilder();
			b.append("{");
			b.append("	String n = $1.getName();");
			b.append("	if(");

			Iterator<String> iter = nonSerializables.iterator();
			while(iter.hasNext())
			{
				String s = iter.next();

				b.append("n.equals(\"").append(s).append("\")");

				if(iter.hasNext())
				{
					b.append(" || ");
				}
			}

			b.append(") { throw new SecurityException(\"Deserializing class \"+n+\" is not allowed\"); }");
			b.append("}");

			ctMethod.insertBefore(b.toString());

			return ctCls.toBytecode();
		}
		catch(Throwable e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void premain(String args, Instrumentation instrumentation)
	{
		instrumentation.addTransformer(new ObjectInputStreamTransformer(args));
	}
}
