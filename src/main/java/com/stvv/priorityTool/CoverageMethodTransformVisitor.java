package com.stvv.agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CoverageMethodTransformVisitor extends MethodVisitor implements Opcodes {
	String mName;
	int line;
	String className;

	public CoverageMethodTransformVisitor(final MethodVisitor mv, String name, String className) {
		super(ASM5, mv);
		this.mName = name;
		this.className = className;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		this.line = line;
		mv.visitLdcInsn(className + ":" + line + "\n");
		mv.visitMethodInsn(INVOKESTATIC, "com/stvv/agent/HashsetUtil", "addLine", "(Ljava/lang/String;)V", false);
		super.visitLineNumber(line, start);

	}

	@Override
	public void visitLabel(Label label) {
		mv.visitLdcInsn(className + ":" + line + "\n");
		mv.visitMethodInsn(INVOKESTATIC, "com/stvv/agent/HashsetUtil", "addLine", "(Ljava/lang/String;)V", false);
		super.visitLabel(label);
	}
}
