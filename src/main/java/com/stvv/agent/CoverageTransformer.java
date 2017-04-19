package com.stvv.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class CoverageTransformer implements ClassFileTransformer {

	private String classBase;
	static final String[] DEFAULT_EXCLUDES = new String[] { "com/sun/", "sun/", "java/", "javax/", "org/slf4j" };
	
	CoverageTransformer(String args) {
		super();
		classBase = args;
	}

	public byte[] transform(ClassLoader loader, String className, Class classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		byte[] byteCode = classfileBuffer;
		boolean excluded = false;
		for (String exclude : DEFAULT_EXCLUDES) {
			excluded = className.startsWith(exclude) || excluded;
		}
		if (className.startsWith(classBase)) {
			ClassReader cr = new ClassReader(classfileBuffer);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			CoverageClassTransformVisitor ca = new CoverageClassTransformVisitor(cw, className);
			cr.accept(ca, 0);
			byteCode = cw.toByteArray();
		}
		return byteCode;
	}
}
