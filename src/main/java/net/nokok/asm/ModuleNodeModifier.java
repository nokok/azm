package net.nokok.asm;

import net.nokok.ow2asm.Opcodes;

public enum ModuleNodeModifier {
    OPEN(Opcodes.ACC_OPEN),
    NONE(0);
    private final int access;

    private ModuleNodeModifier(int access) {
        this.access = access;
    }

    public int toAccess() {
        return this.access;
    }
}
