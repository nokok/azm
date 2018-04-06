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

import net.nokok.azm.MethodVisitor;
import net.nokok.azm.Opcodes;

import java.util.Objects;

/**
 * A node that represents a parameter of a method.
 *
 * @author Remi Forax
 */
public class ParameterNode {

    private String name;

    private int access;

    /**
     * Constructs a new {@link ParameterNode}.
     *
     * @param access The parameter's access flags. Valid values are <tt>ACC_FINAL</tt>,
     *               <tt>ACC_SYNTHETIC</tt> or/and <tt>ACC_MANDATED</tt> (see {@link
     *               Opcodes}).
     * @param name   the parameter's name.
     */
    public ParameterNode(final String name, final int access) {
        this.setName(name);
        this.setAccess(access);
    }

    /**
     * Makes the given visitor visit this parameter declaration.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitParameter(getName(), getAccess());
    }

    /**
     * The parameter's name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The parameter's access flags (see {@link Opcodes}). Valid values are
     * <tt>ACC_FINAL</tt>, <tt>ACC_SYNTHETIC</tt> and <tt>ACC_MANDATED</tt>.
     */
    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterNode that = (ParameterNode) o;
        return getAccess() == that.getAccess() &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getAccess());
    }

    @Override
    public String toString() {
        return "ParameterNode{" +
                "name='" + name + '\'' +
                ", access=" + access +
                '}';
    }
}
