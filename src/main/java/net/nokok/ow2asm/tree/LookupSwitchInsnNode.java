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
 * A node that represents a LOOKUPSWITCH instruction.
 *
 * @author Eric Bruneton
 */
public class LookupSwitchInsnNode extends AbstractInsnNode {

    private LabelNode dflt;

    private List<Integer> keys;

    private List<LabelNode> labels;

    /**
     * Constructs a new {@link LookupSwitchInsnNode}.
     *
     * @param dflt   beginning of the default handler block.
     * @param keys   the values of the keys.
     * @param labels beginnings of the handler blocks. <tt>labels[i]</tt> is the beginning of the
     *               handler block for the <tt>keys[i]</tt> key.
     */
    public LookupSwitchInsnNode(final LabelNode dflt, final int[] keys, final LabelNode[] labels) {
        super(Opcodes.LOOKUPSWITCH);
        this.setDflt(dflt);
        this.setKeys(Util.asArrayList(keys));
        this.setLabels(Util.asArrayList(labels));
    }

    @Override
    public int getType() {
        return LOOKUPSWITCH_INSN;
    }

    @Override
    public void accept(final MethodVisitor methodVisitor) {
        int[] keysArray = new int[this.getKeys().size()];
        for (int i = 0, n = keysArray.length; i < n; ++i) {
            keysArray[i] = this.getKeys().get(i).intValue();
        }
        Label[] labelsArray = new Label[this.getLabels().size()];
        for (int i = 0, n = labelsArray.length; i < n; ++i) {
            labelsArray[i] = this.getLabels().get(i).getLabel();
        }
        methodVisitor.visitLookupSwitchInsn(getDflt().getLabel(), keysArray, labelsArray);
        acceptAnnotations(methodVisitor);
    }

    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> clonedLabels) {
        LookupSwitchInsnNode clone =
                new LookupSwitchInsnNode(clone(getDflt(), clonedLabels), null, clone(getLabels(), clonedLabels));
        clone.getKeys().addAll(getKeys());
        return clone.cloneAnnotations(this);
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
     * The values of the keys.
     */
    public List<Integer> getKeys() {
        return keys;
    }

    public void setKeys(List<Integer> keys) {
        this.keys = keys;
    }

    /**
     * Beginnings of the handler blocks.
     */
    public List<LabelNode> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelNode> labels) {
        this.labels = labels;
    }
}
