package net.nokok.asm;

import net.nokok.azm.Opcodes;

public enum ModuleRequireModifier {
    STATIC(Opcodes.ACC_STATIC),
    TRANSITIVE(Opcodes.ACC_TRANSITIVE),
    MANDATED(Opcodes.ACC_MANDATED);

    private final int access;

    private ModuleRequireModifier(int access) {
        this.access = access;
    }

    public int toAccess() {
        return this.access;
    }

}
