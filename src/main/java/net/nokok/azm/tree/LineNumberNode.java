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

import java.util.Map;
import java.util.Objects;

/**
 * A node that represents a line number declaration. These nodes are pseudo instruction nodes in
 * order to be inserted in an instruction list.
 *
 * @author Eric Bruneton
 */
public class LineNumberNode extends AbstractInsnNode {

    private int line;

    private LabelNode start;

    /**
     * Constructs a new {@link LineNumberNode}.
     *
     * @param line  a line number. This number refers to the source file from which the class was
     *              compiled.
     * @param start the first instruction corresponding to this line number.
     */
    public LineNumberNode(final int line, final LabelNode start) {
        super(-1);
        this.setLine(line);
        this.setStart(start);
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLineNumber(getLine(), getStart().getLabel());
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new LineNumberNode(getLine(), clone(getStart(), clonedLabels));
    }

    /**
     * A line number. This number refers to the source file from which the class was compiled.
     */
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    /**
     * The first instruction corresponding to this line number.
     */
    public LabelNode getStart() {
        return start;
    }

    public void setStart(LabelNode start) {
        this.start = start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LineNumberNode that = (LineNumberNode) o;
        return getLine() == that.getLine() &&
                Objects.equals(getStart(), that.getStart());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getLine(), getStart());
    }
}
