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

import java.util.Objects;

/**
 * A node that represents a local variable declaration.
 *
 * @author Eric Bruneton
 */
public class LocalVariableNode {

    private String name;

    private String desc;

    private String signature;

    private LabelNode start;

    private LabelNode end;

    private int index;

    /**
     * Constructs a new {@link LocalVariableNode}.
     *
     * @param name       the name of a local variable.
     * @param descriptor the type descriptor of this local variable.
     * @param signature  the signature of this local variable. May be <tt>null</tt>.
     * @param start      the first instruction corresponding to the scope of this local variable
     *                   (inclusive).
     * @param end        the last instruction corresponding to the scope of this local variable (exclusive).
     * @param index      the local variable's index.
     */
    public LocalVariableNode(
            final String name,
            final String descriptor,
            final String signature,
            final LabelNode start,
            final LabelNode end,
            final int index) {
        this.setName(name);
        this.setDesc(descriptor);
        this.setSignature(signature);
        this.setStart(start);
        this.setEnd(end);
        this.setIndex(index);
    }

    /**
     * Makes the given visitor visit this local variable declaration.
     *
     * @param methodVisitor a method visitor.
     */
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(
                getName(), getDesc(), getSignature(), getStart().getLabel(), getEnd().getLabel(), getIndex());
    }

    /**
     * The name of a local variable.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The type descriptor of this local variable.
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * The signature of this local variable. May be <tt>null</tt>.
     */
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * The first instruction corresponding to the scope of this local variable (inclusive).
     */
    public LabelNode getStart() {
        return start;
    }

    public void setStart(LabelNode start) {
        this.start = start;
    }

    /**
     * The last instruction corresponding to the scope of this local variable (exclusive).
     */
    public LabelNode getEnd() {
        return end;
    }

    public void setEnd(LabelNode end) {
        this.end = end;
    }

    /**
     * The local variable's index.
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalVariableNode that = (LocalVariableNode) o;
        return getIndex() == that.getIndex() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDesc(), that.getDesc()) &&
                Objects.equals(getSignature(), that.getSignature()) &&
                Objects.equals(getStart(), that.getStart()) &&
                Objects.equals(getEnd(), that.getEnd());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getDesc(), getSignature(), getStart(), getEnd(), getIndex());
    }
}
