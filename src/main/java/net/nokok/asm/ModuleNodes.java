package net.nokok.asm;

import net.nokok.ow2asm.ClassWriter;
import net.nokok.ow2asm.Opcodes;
import net.nokok.ow2asm.tree.ModuleExportNode;
import net.nokok.ow2asm.tree.ModuleNode;
import net.nokok.ow2asm.tree.ModuleOpenNode;
import net.nokok.ow2asm.tree.ModuleProvideNode;
import net.nokok.ow2asm.tree.ModuleRequireNode;

import java.util.ArrayList;
import java.util.List;

public class ModuleNodes {
    private final String moduleName;
    private final int access; // ACC_MODULEはModuleNodeに対しては使わない。OPENかそうでないか
    private final String version = "10";
    private final List<String> packages = new ArrayList<>();
    private String mainClass = null;
    private final List<ModuleRequireNode> requires = new ArrayList<>();
    private final List<ModuleExportNode> exports = new ArrayList<>();
    private final List<ModuleOpenNode> opens = new ArrayList<>();
    private final List<String> uses = new ArrayList<>();
    private final List<ModuleProvideNode> provides = new ArrayList<>();

    private ModuleNodes(String moduleName) {
        this(moduleName, ModuleNodeModifier.NONE /*0がデフォルト値*/);
    }

    private ModuleNodes(String moduleName, ModuleNodeModifier modifier) {
        this.moduleName = moduleName;
        this.access = modifier.toAccess();
        this.addRequire("java.base", ModuleRequireModifier.MANDATED);
    }

    public byte[] toByteCode() {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ModuleNode moduleNode = new ModuleNode(
                Opcodes.ASM6,
                this.moduleName,
                this.access,
                this.version,
                this.mainClass,
                this.packages,
                this.requires,
                this.exports,
                this.opens,
                this.uses,
                this.provides);

        // 何故かPackagesとmainClassがコンストラクタに無いので追加する。
        // ASM6側のバグに思える(TODOコメントがある)
        moduleNode.setPackages(this.packages);
        moduleNode.setMainClass(this.mainClass);
        writer.visit(Opcodes.ASM6, Opcodes.ACC_MODULE, "module-info", null, null, null);
        moduleNode.accept(writer);
        writer.visitEnd();
        return writer.toByteArray();
    }

    public void addPackage(String internalPackageName) {
        this.packages.add(internalPackageName);
    }

    public void addRequire(String moduleName) {
        this.requires.add(new ModuleRequireNode(moduleName, 0, "10"));
    }

    public void addRequire(String moduleName, ModuleRequireModifier modifier) {
        this.requires.add(new ModuleRequireNode(moduleName, modifier.toAccess(), "10"));
    }

    public static ModuleNodes newModule(String moduleName) {
        return new ModuleNodes(moduleName);
    }

    public static ModuleNodes newOpenModule(String moduleName) {
        return new ModuleNodes(moduleName);
    }

}
