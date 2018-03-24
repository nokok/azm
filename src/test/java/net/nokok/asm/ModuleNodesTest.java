package net.nokok.asm;

import org.junit.jupiter.api.Test;
import net.nokok.ow2asm.ClassReader;
import net.nokok.ow2asm.tree.ClassNode;
import net.nokok.ow2asm.tree.ModuleNode;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleNodesTest {

    @Test
    void testModuleNodes1() {
        ModuleNodes moduleNodes = ModuleNodes.newModule("foo.bar");
        String javaSource = "module foo.bar {}";
        ClassReader expectedReader = new ClassReader(compile("module-info", javaSource));
        ClassReader actualReader = new ClassReader(moduleNodes.toByteCode());
        ClassNode actualClassNode = new ClassNode();
        actualReader.accept(actualClassNode, ClassReader.SKIP_CODE);
        assertEquals(expectedReader.getClassName(), actualReader.getClassName());
        assertEquals(expectedReader.getInterfaces().length, actualReader.getInterfaces().length);
        assertArrayEquals(expectedReader.getInterfaces(), actualReader.getInterfaces());
        assertEquals(expectedReader.getSuperName(), actualReader.getSuperName());
        assertEquals(expectedReader.getAccess(), actualReader.getAccess());

        ClassNode expectedClassNode = new ClassNode();
        expectedReader.accept(expectedClassNode, ClassReader.SKIP_CODE);

        ModuleNode actualModule = actualClassNode.module;
        ModuleNode expectedModule = expectedClassNode.module;

        assertEquals(expectedModule.access, actualModule.access);
        assertEquals(expectedModule.mainClass, actualModule.mainClass);
        assertEquals(expectedModule.packages, actualModule.packages);
        assertIterableEquals(expectedModule.exports, actualModule.exports);
        assertIterableEquals(expectedModule.opens, actualModule.opens);
        assertIterableEquals(expectedModule.provides, actualModule.provides);
        assertIterableEquals(expectedModule.requires, actualModule.requires);
        assertIterableEquals(expectedModule.uses, actualModule.uses);


    }

    @Test
    public void testModuleNodes2() {
    }

    private byte[] compile(String sourceFileName, String sourceCode) {
        FileObject fileObject = new FileObject(sourceFileName, sourceCode);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                null,
                collector,
                Arrays.asList("-d", "out"),
                null,
                Collections.singleton(fileObject));
        assertTrue(task.call(), () -> collector.getDiagnostics().stream().map(Diagnostic::toString).reduce((l, r) -> l + r).get());
        try {
            return Files.readAllBytes(Paths.get("out", sourceFileName + ".class"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    class FileObject extends SimpleJavaFileObject {

        private final String source;

        protected FileObject(String name, String source) {
            super(URI.create("string:///" + name.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.source = source;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return this.source;
        }
    }
}
