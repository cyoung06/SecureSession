package kr.syeyoung.securesession;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.PrintStream;
import java.security.AccessControlException;
import java.security.Permission;

public class ModSecurityManager extends SecurityManager {
    private SecurityManager parent;
    public ModSecurityManager(SecurityManager parent) {
        this.parent = parent;
    }

    @Override
    public void checkRead(String file) {
        super.checkRead(file);
//        System.out.println("reading "+file);
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        super.checkRead(fd);
//        System.out.println("reading "+fd);
    }

    @Override
    public void checkRead(String file, Object context) {
        super.checkRead(file, context);
//        System.out.println("reading "+file+" with "+context);
    }

    @Override
    public void checkPermission(Permission perm) {
        if (perm.getName().equals("accessSessionToken")) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            try { throw new RuntimeException("");} catch(Exception e) {
                e.printStackTrace(printStream);
            }
            printStream.flush();

            String stacktrace = byteArrayOutputStream.toString();
            int res = JOptionPane.showConfirmDialog(null, stacktrace+"\n\nContinue?", "A mod is trying to access session token", JOptionPane.YES_NO_OPTION);
            if (res != 0) throw new AccessControlException("Denied access");
        }

        if (parent != null)
            parent.checkPermission(perm);
        else
            super.checkPermission(perm);
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        if (parent != null)
            parent.checkPermission(perm, context);
        else
            super.checkPermission(perm, context);
    }
}
