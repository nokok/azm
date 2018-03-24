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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A node that represents a stack map frame. These nodes are pseudo instruction nodes in order to be
 * inserted in an instruction list. In fact these nodes must(*) be inserted <i>just before</i> any
 * instruction node <b>i</b> that follows an unconditionnal branch instruction such as GOTO or
 * THROW, that is the target of a jump instruction, or that starts an exception handler block. The
 * stack map frame types must describe the values of the local variables and of the operand stack
 * elements <i>just before</i> <b>i</b> is executed. <br>
 * <br>
 * (*) this is mandatory only for classes whose version is greater than or equal to {@link
 * Opcodes#V1_6}.
 *
 * @author Eric Bruneton
 */
public class FrameNode extends AbstractInsnNode {

    private int type;

    private List<Object> local;

    private List<Object> stack;

    private FrameNode() {
        super(-1);
    }

    /**
     * Constructs a new {@link FrameNode}.
     *
     * @param type   the type of this frame. Must be {@link Opcodes#F_NEW} for expanded frames, or
     *               {@link Opcodes#F_FULL}, {@link Opcodes#F_APPEND}, {@link Opcodes#F_CHOP}, {@link
     *               Opcodes#F_SAME} or {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for compressed frames.
     * @param nLocal number of local variables of this stack map frame.
     * @param local  the types of the local variables of this stack map frame. Elements of this list
     *               can be Integer, String or LabelNode objects (for primitive, reference and uninitialized
     *               types respectively - see {@link MethodVisitor}).
     * @param nStack number of operand stack elements of this stack map frame.
     * @param stack  the types of the operand stack elements of this stack map frame. Elements of this
     *               list can be Integer, String or LabelNode objects (for primitive, reference and
     *               uninitialized types respectively - see {@link MethodVisitor}).
     */
    public FrameNode(
            final int type,
            final int nLocal,
            final Object[] local,
            final int nStack,
            final Object[] stack) {
        super(-1);
        this.setType(type);
        switch (type) {
        case Opcodes.F_NEW:
        case Opcodes.F_FULL:
            this.setLocal(Util.asArrayList(nLocal, local));
            this.setStack(Util.asArrayList(nStack, stack));
            break;
        case Opcodes.F_APPEND:
            this.setLocal(Util.asArrayList(nLocal, local));
            break;
        case Opcodes.F_CHOP:
            this.setLocal(Util.asArrayList(nLocal));
            break;
        case Opcodes.F_SAME:
            break;
        case Opcodes.F_SAME1:
            this.setStack(Util.asArrayList(1, stack));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * The type of this frame. Must be {@link Opcodes#F_NEW} for expanded frames, or {@link
     * Opcodes#F_FULL}, {@link Opcodes#F_APPEND}, {@link Opcodes#F_CHOP}, {@link Opcodes#F_SAME} or
     * {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for compressed frames.
     */
    @Override
    public int getType() {
        return FRAME;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        switch (getType()) {
        case Opcodes.F_NEW:
        case Opcodes.F_FULL:
            methodVisitor.visitFrame(getType(), getLocal().size(), asArray(getLocal()), getStack().size(), asArray(getStack()));
            break;
        case Opcodes.F_APPEND:
            methodVisitor.visitFrame(getType(), getLocal().size(), asArray(getLocal()), 0, null);
            break;
        case Opcodes.F_CHOP:
            methodVisitor.visitFrame(getType(), getLocal().size(), null, 0, null);
            break;
        case Opcodes.F_SAME:
            methodVisitor.visitFrame(getType(), 0, null, 0, null);
            break;
        case Opcodes.F_SAME1:
            methodVisitor.visitFrame(getType(), 0, null, 1, asArray(getStack()));
            break;
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        FrameNode clone = new FrameNode();
        clone.setType(getType());
        if (getLocal() != null) {
            clone.setLocal(new ArrayList<Object>());
            for (int i = 0, n = getLocal().size(); i < n; ++i) {
                Object localElement = getLocal().get(i);
                if (localElement instanceof LabelNode) {
                    localElement = clonedLabels.get(localElement);
                }
                clone.getLocal().add(localElement);
            }
        }
        if (getStack() != null) {
            clone.setStack(new ArrayList<Object>());
            for (int i = 0, n = getStack().size(); i < n; ++i) {
                Object stackElement = getStack().get(i);
                if (stackElement instanceof LabelNode) {
                    stackElement = clonedLabels.get(stackElement);
                }
                clone.getStack().add(stackElement);
            }
        }
        return clone;
    }

    private static Object[] asArray(final List<Object> list) {
        Object[] array = new Object[list.size()];
        for (int i = 0, n = array.length; i < n; ++i) {
            Object o = list.get(i);
            if (o instanceof LabelNode) {
                o = ((LabelNode) o).getLabel();
            }
            array[i] = o;
        }
        return array;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * The types of the local variables of this stack map frame. Elements of this list can be Integer,
     * String or LabelNode objects (for primitive, reference and uninitialized types respectively -
     * see {@link MethodVisitor}).
     */
    public List<Object> getLocal() {
        return local;
    }

    public void setLocal(List<Object> local) {
        this.local = local;
    }

    /**
     * The types of the operand stack elements of this stack map frame. Elements of this list can be
     * Integer, String or LabelNode objects (for primitive, reference and uninitialized types
     * respectively - see {@link MethodVisitor}).
     */
    public List<Object> getStack() {
        return stack;
    }

    public void setStack(List<Object> stack) {
        this.stack = stack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FrameNode frameNode = (FrameNode) o;
        return getType() == frameNode.getType() &&
                Objects.equals(getLocal(), frameNode.getLocal()) &&
                Objects.equals(getStack(), frameNode.getStack());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getType(), getLocal(), getStack());
    }
}
