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
package net.nokok.ow2asm.tree;

import net.nokok.ow2asm.Handle;
import net.nokok.ow2asm.MethodVisitor;
import net.nokok.ow2asm.Opcodes;
import net.nokok.ow2asm.Type;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * A node that represents an invokedynamic instruction.
 *
 * @author Remi Forax
 */
public class InvokeDynamicInsnNode extends AbstractInsnNode {

    private String name;

    private String desc;

    private Handle bsm;

    private Object[] bsmArgs;

    /**
     * Constructs a new {@link InvokeDynamicInsnNode}.
     *
     * @param name                     the method's name.
     * @param descriptor               the method's descriptor (see {@link Type}).
     * @param bootstrapMethodHandle    the bootstrap method.
     * @param bootstrapMethodArguments the bootstrap method constant arguments. Each argument must be
     *                                 an {@link Integer}, {@link Float}, {@link Long}, {@link Double}, {@link String}, {@link
     *                                 Type} or {@link Handle} value. This method is allowed to modify the
     *                                 content of the array so a caller should expect that this array may change.
     */
    public InvokeDynamicInsnNode(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
        super(Opcodes.INVOKEDYNAMIC);
        this.setName(name);
        this.setDesc(descriptor);
        this.setBsm(bootstrapMethodHandle);
        this.setBsmArgs(bootstrapMethodArguments);
    }

    @Override
    public int getType() {
        return INVOKE_DYNAMIC_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitInvokeDynamicInsn(getName(), getDesc(), getBsm(), getBsmArgs());
        acceptAnnotations(methodVisitor);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new InvokeDynamicInsnNode(getName(), getDesc(), getBsm(), getBsmArgs()).cloneAnnotations(this);
    }

    /**
     * The method's name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The method's descriptor (see {@link Type}).
     */
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * The bootstrap method.
     */
    public Handle getBsm() {
        return bsm;
    }

    public void setBsm(Handle bsm) {
        this.bsm = bsm;
    }

    /**
     * The bootstrap method constant arguments.
     */
    public Object[] getBsmArgs() {
        return bsmArgs;
    }

    public void setBsmArgs(Object[] bsmArgs) {
        this.bsmArgs = bsmArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        InvokeDynamicInsnNode that = (InvokeDynamicInsnNode) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDesc(), that.getDesc()) &&
                Objects.equals(getBsm(), that.getBsm()) &&
                Arrays.equals(getBsmArgs(), that.getBsmArgs());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), getName(), getDesc(), getBsm());
        result = 31 * result + Arrays.hashCode(getBsmArgs());
        return result;
    }
}
