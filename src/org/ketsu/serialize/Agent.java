package org.ketsu.serialize;

import java.lang.instrument.Instrumentation;

import org.ketsu.serialize.transform.ObjectInputStreamTransformer;
import org.ketsu.serialize.transform.SerializableTransformer;

public class Agent
{
	public static void premain(String args, Instrumentation instrumentation)
	{
		instrumentation.addTransformer(new SerializableTransformer(null));
		instrumentation.addTransformer(new ObjectInputStreamTransformer(null));
	}
}
