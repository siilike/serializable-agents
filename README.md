
Simple Java agents to mitigate the Java deserialization exploits without updating any libraries or messing with the SecurityManager.

Working exploits and details are available here:

  https://github.com/frohoff/ysoserial

Java agents provide means to instrument JVM programs by modifying byte code.

The current library includes 4 different standalone agents:

## SerializableTransformer

Removes the Serializable interface from selected classes.

Example usage:

  java -javaagent:/path/to/SerializableTransformer.jar com.example.ExampleApp

or for setting custom classes:

  java -javaagent:/path/to/SerializableTransformer.jar=sun.reflect.annotation.AnnotationInvocationHandler,com.example.SerializableClass com.example.ExampleApp

Note that this overrides the defaults.

## ObjectInputStreamTransformer

Adds relevant class name checks to ObjectInputStream.

Example usage:

  java -javaagent:/path/to/ObjectInputStreamTransformer.jar com.example.ExampleApp

or for setting custom classes:

  java -javaagent:/path/to/ObjectInputStreamTransformer.jar=java.lang.reflect.Proxy,com.example.SerializableClass com.example.ExampleApp

Note that this overrides the defaults.

## MethodNullerTransformer

Rewrites a method body to return null.

Example usage:

  java -javaagent:/path/to/MethodNullerTransformer.jar=org.apache.commons.collections4.functors.InvokerTransformer=transform com.example.ExampleApp

## MethodExceptionTransformer

Rewrites a method body to throw a SecurityException.

For example, to disable executing subprocesses via the Runtime class entirely:

  java -javaagent:/path/to/MethodExceptionTransformer.jar=java.lang.ProcessBuilder=start com.example.ExampleApp

You can add several agents, so combine them as needed according to the situation, they are all standalone and contain everything required.

Groovy is not covered by default, although one could make org.codehaus.groovy.runtime.ConvertedClosure non-serializable. Note that the problem is (a lot) wider than just getting rid of those specific classes/attack routes.
