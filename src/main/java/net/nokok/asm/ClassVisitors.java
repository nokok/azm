package net.nokok.asm;

import net.nokok.azm.ClassVisitor;
import net.nokok.azm.tree.ClassNode;

public class ClassVisitors {
    private final ClassVisitor classVisitor;

    private ClassVisitors() {
        this.classVisitor = new ClassNode();
    }

}
