package kr.syeyoung.securesession;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class SessionTransformer implements IClassTransformer {
    public static String name;
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("net.minecraft.util.Session")) {
            return injectSecurityCheck(name, basicClass, false);
        }
        return basicClass;
    }

    private byte[] injectSecurityCheck(String name, byte[] bytes, boolean obf) {
        Set<String> targetMethodNames = new HashSet<>();
        String tokenName = "";
        if(obf) {
        } else {
            targetMethodNames.add("getSessionID()Ljava/lang/String;");
            targetMethodNames.add("getToken()Ljava/lang/String;");
            tokenName = "token";
        }



        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (targetMethodNames.contains(method.name + method.desc)) {
                InsnList toInject = new InsnList();

                toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
                toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "kr/syeyoung/securesession/PermissionChecker", "checkPermission", "()V"));

                method.instructions.insert(method.instructions.getFirst(), toInject);
            }
        }
        StringBuilder transformInto = new StringBuilder();
        String charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            transformInto.append(charset.charAt(r.nextInt(charset.length())));
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(new ClassAdapter(classWriter, tokenName, transformInto.toString()));

        SessionTransformer.name = transformInto.toString();
        System.out.println(SessionTransformer.name);

        return classWriter.toByteArray();
    }

    public static class ClassAdapter extends ClassVisitor {
        private String origName = "";
        private String transformInto = "";


        public ClassAdapter(ClassVisitor cv, String origName, String transformInto) {
            super(Opcodes.ASM5, cv);
            this.origName = origName;
            this.transformInto = transformInto;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            if (name.equals(origName) && desc.equals("Ljava/lang/String;")) name = transformInto;
            return super.visitField(access, name, desc, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
            return new MethodAdapter(mv);
        }

        public class MethodAdapter extends MethodVisitor {
            public MethodAdapter(MethodVisitor mv) {
                super(Opcodes.ASM5, mv);
            }
            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                if (name.equals(origName) && desc.equals("Ljava/lang/String;")) name = transformInto;

                super.visitFieldInsn(opcode, owner, name, desc);
            }
        }
    }
}
