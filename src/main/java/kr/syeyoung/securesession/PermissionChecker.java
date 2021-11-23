package kr.syeyoung.securesession;

public class PermissionChecker {
    public static void checkPermission() {
        System.getSecurityManager().checkPermission(new RuntimePermission("accessSessionToken"));
    }
}
