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

import java.util.Map;
import java.util.Objects;

/**
 * A node that represents an IINC instruction.
 *
 * @author Eric Bruneton
 */
public class IincInsnNode extends AbstractInsnNode {

    private int var;

    private int incr;

    /**
     * Constructs a new {@link IincInsnNode}.
     *
     * @param var  index of the local variable to be incremented.
     * @param incr increment amount to increment the local variable by.
     */
    public IincInsnNode(final int var, final int incr) {
        super(Opcodes.IINC);
        this.setVar(var);
        this.setIncr(incr);
    }

    @Override
    public int getType() {
        return IINC_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitIincInsn(getVar(), getIncr());
        acceptAnnotations(methodVisitor);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new IincInsnNode(getVar(), getIncr()).cloneAnnotations(this);
    }

    /**
     * Index of the local variable to be incremented.
     */
    public int getVar() {
        return var;
    }

    public void setVar(int var) {
        this.var = var;
    }

    /**
     * Amount to increment the local variable by.
     */
    public int getIncr() {
        return incr;
    }

    public void setIncr(int incr) {
        this.incr = incr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IincInsnNode that = (IincInsnNode) o;
        return getVar() == that.getVar() &&
                getIncr() == that.getIncr();
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getVar(), getIncr());
    }

    @Override
    public String toString() {
        return "IincInsnNode{" +
                "var=" + var +
                ", incr=" + incr +
                '}';
    }
}
