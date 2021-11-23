package kr.syeyoung.securesession;

import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import sun.reflect.Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
@IFMLLoadingPlugin.TransformerExclusions({"kr.syeyoung.securesession"})
public class SecureSession implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                SessionTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return SessionTransformerContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        System.out.println(data);

        Field security = null;
        try {
            Method getDeclaredFields0M = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
            getDeclaredFields0M.setAccessible(true);
            Field[] fields = (Field[]) getDeclaredFields0M.invoke(System.class, false);
            for (Field field : fields)
                if (field.getName().equals("security")) {
                    security = field;
                }
        } catch (Throwable ex) {
            throw new UnsupportedOperationException(ex);
        }
        ModSecurityManager modSecurityManager = new ModSecurityManager(System.getSecurityManager());
        if (security != null) {
            try {
                security.setAccessible(true);
                security.set(null, modSecurityManager);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Can not install security manager", e);
            }
        } else {
            throw new RuntimeException("Can not install security manager");
        }


        try {
            Reflection.registerFieldsToFilter(Session.class,Session.class.getDeclaredField(SessionTransformer.name).getName());
            // Remove the way I used to replace security manager
            Reflection.registerMethodsToFilter(Class.class, Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class).getName());
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            Field f = Class.class.getDeclaredField("reflectionData");
            f.setAccessible(true);
            f.set(Session.class, null);
            f.set(Class.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
