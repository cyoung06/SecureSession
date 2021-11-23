package kr.syeyoung.securesession;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Arrays;

public class SessionTransformerContainer extends DummyModContainer {
    public SessionTransformerContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "$SecuredSession";
        meta.name = "SecuredSession";
        meta.version = "1.0";
        meta.credits = "syeyoung";
        meta.authorList = Arrays.asList("syeyoung#3876 (cyoung06@naver.com)");
        meta.description = "Prevents unauthorized access to session token";
        meta.url = "NOT YET";
        meta.screenshots = new String[0];
        meta.logoFile = "";

    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void modConstruction(FMLConstructionEvent evt){
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {
    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {

    }


    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {

    }
}
