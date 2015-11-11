package org.ketsu.serialize.transform;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import org.ketsu.serialize.Config;

import javassist.ClassPool;
import javassist.CtClass;

public class SerializableTransformer implements ClassFileTransformer
{
	protected final Set<String> nonSerializables = new HashSet<>();

	public SerializableTransformer(String args)
	{
		if(args == null || args.isEmpty())
		{
			for(String c : Config.getUnsafeClasses())
			{
				nonSerializables.add(c.replace('.', '/'));
			}
		}
		else
		{
			for(String a : args.split(","))
			{
				if(!a.isEmpty())
				{
					nonSerializables.add(a.replace('.', '/'));
				}
			}
		}
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFile) throws IllegalClassFormatException
	{
		if(!nonSerializables.contains(className))
		{
			return classFile;
		}

		try
		{
			ClassPool cp = ClassPool.getDefault();

			CtClass ctCls = cp.makeClass(new ByteArrayInputStream(classFile));
			CtClass[] ifs = ctCls.getInterfaces();

			int i = 0;
			for(CtClass iface : ifs)
			{
				if(iface.getName().equals("java.io.Serializable"))
				{
					if(ifs.length == 1)
					{
						ifs = null;
					}
					else
					{
						CtClass[] newIfs = new CtClass[ifs.length-1];

						System.arraycopy(ifs, 0, newIfs, 0, i);

						if(i != ifs.length - 1)
						{
							System.arraycopy(ifs, i + 1, newIfs, i, ifs.length - i - 1);
						}

						ifs = newIfs;
					}

					ctCls.setInterfaces(ifs);

					return ctCls.toBytecode();
				}

				i++;
			}

			return classFile;
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void premain(String args, Instrumentation instrumentation)
	{
		instrumentation.addTransformer(new SerializableTransformer(args));
	}
}
