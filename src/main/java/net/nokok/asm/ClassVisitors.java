package net.nokok.asm;

import net.nokok.ow2asm.ClassVisitor;
import net.nokok.ow2asm.tree.ClassNode;

public class ClassVisitors {
    private final ClassVisitor classVisitor;

    private ClassVisitors() {
        this.classVisitor = new ClassNode();
    }

}
