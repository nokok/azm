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

import net.nokok.ow2asm.Label;
import net.nokok.ow2asm.MethodVisitor;
import net.nokok.ow2asm.Opcodes;

import java.util.List;
import java.util.Map;

/**
 * A node that represents a TABLESWITCH instruction.
 *
 * @author Eric Bruneton
 */
public class TableSwitchInsnNode extends AbstractInsnNode {

    private int min;

    private int max;

    private LabelNode dflt;

    private List<LabelNode> labels;

    /**
     * Constructs a new {@link TableSwitchInsnNode}.
     *
     * @param min    the minimum key value.
     * @param max    the maximum key value.
     * @param dflt   beginning of the default handler block.
     * @param labels beginnings of the handler blocks. <tt>labels[i]</tt> is the beginning of the
     *               handler block for the <tt>min + i</tt> key.
     */
    public TableSwitchInsnNode(
            final int min, final int max, final LabelNode dflt, final LabelNode... labels) {
        super(Opcodes.TABLESWITCH);
        this.setMin(min);
        this.setMax(max);
        this.setDflt(dflt);
        this.setLabels(Util.asArrayList(labels));
    }

    @Override
    public int getType() {
        return TABLESWITCH_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        Label[] labelsArray = new Label[this.getLabels().size()];
        for (int i = 0, n = labelsArray.length; i < n; ++i) {
            labelsArray[i] = this.getLabels().get(i).getLabel();
        }
        methodVisitor.visitTableSwitchInsn(getMin(), getMax(), getDflt().getLabel(), labelsArray);
        acceptAnnotations(methodVisitor);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        return new TableSwitchInsnNode(getMin(), getMax(), clone(getDflt(), clonedLabels), clone(getLabels(), clonedLabels))
                .cloneAnnotations(this);
    }

    /**
     * The minimum key value.
     */
    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    /**
     * The maximum key value.
     */
    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Beginning of the default handler block.
     */
    public LabelNode getDflt() {
        return dflt;
    }

    public void setDflt(LabelNode dflt) {
        this.dflt = dflt;
    }

    /**
     * Beginnings of the handler blocks. This list is a list of {@link LabelNode} objects.
     */
    public List<LabelNode> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelNode> labels) {
        this.labels = labels;
    }
}
