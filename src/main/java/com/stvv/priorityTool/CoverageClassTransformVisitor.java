package com.stvv.priorityTool;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class CoverageClassTransformVisitor extends ClassVisitor implements Opcodes {
	private String className;

	public CoverageClassTransformVisitor(final ClassVisitor cv, String className) {
		super(ASM5, cv);
		this.className = className;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new CoverageMethodTransformVisitor(mv, name, className);
	}
}
