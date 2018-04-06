// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package net.nokok.azm.tree;

import net.nokok.azm.ModuleVisitor;
import net.nokok.azm.Opcodes;

import java.util.Objects;

/**
 * A node that represents a required module with its name and access of a module descriptor.
 *
 * @author Remi Forax
 */
public class ModuleRequireNode {

    private String module;

    private int access;

    private String version;

    /**
     * Constructs a new {@link ModuleRequireNode}.
     *
     * @param module  the fully qualified name (using dots) of the dependence.
     * @param access  the access flag of the dependence among {@code ACC_TRANSITIVE}, {@code
     *                ACC_STATIC_PHASE}, {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     * @param version the module version at compile time, or <tt>null</tt>.
     */
    public ModuleRequireNode(final String module, final int access, final String version) {
        this.setModule(module);
        this.setAccess(checkAccess(access));
        this.setVersion(version);
    }

    private static int checkAccess(int access) {
        switch (access) {
        case Opcodes.ACC_TRANSITIVE:
        case Opcodes.ACC_STATIC_PHASE:
        case Opcodes.ACC_SYNTHETIC:
        case Opcodes.ACC_MANDATED:
        case 0:
            return access;
        default:
            throw new IllegalArgumentException(String.valueOf(access));
        }

    }

    /**
     * Makes the given module visitor visit this require directive.
     *
     * @param moduleVisitor a module visitor.
     */
    public void accept(final ModuleVisitor moduleVisitor) {
        moduleVisitor.visitRequire(getModule(), getAccess(), getVersion());
    }

    /**
     * The fully qualified name (using dots) of the dependence.
     */
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    /**
     * The access flag of the dependence among {@code ACC_TRANSITIVE}, {@code ACC_STATIC_PHASE},
     * {@code ACC_SYNTHETIC} and {@code ACC_MANDATED}.
     */
    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    /**
     * The module version at compile time, or <tt>null</tt>.
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModuleRequireNode that = (ModuleRequireNode) o;
        return getAccess() == that.getAccess() &&
                Objects.equals(getModule(), that.getModule()) &&
                Objects.equals(getVersion(), that.getVersion());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getModule(), getAccess(), getVersion());
    }

    @Override
    public String toString() {
        return "ModuleRequireNode{" +
                "module='" + module + '\'' +
                ", access=" + access +
                ", version='" + version + '\'' +
                '}';
    }
}
