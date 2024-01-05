package com.github.glodblock.epp;

import com.github.glodblock.epp.client.ClientRegistryHandler;
import com.github.glodblock.epp.client.hotkey.PatternHotKey;
import com.github.glodblock.epp.common.EAERegistryHandler;
import com.github.glodblock.epp.common.EPPItemAndBlock;
import com.github.glodblock.epp.config.EPPConfig;
import com.github.glodblock.epp.network.EPPNetworkHandler;
import com.github.glodblock.epp.xmod.LoadList;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

@Mod(EPP.MODID)
public class EPP {

    public static final String MODID = "expatternprovider";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static EPP INSTANCE;

    public EPP() {
        assert INSTANCE == null;
        INSTANCE = this;
        LoadList.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EPPConfig.SPEC);
        EPPItemAndBlock.init(EAERegistryHandler.INSTANCE);
        bus.register(EAERegistryHandler.INSTANCE);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> bus.register(ClientRegistryHandler.INSTANCE));
        bus.addListener(this::commonSetup);
        bus.addListener(this::clientSetup);
        bus.addListener((RegisterEvent e) -> {
            if (e.getRegistryKey() == Registries.CREATIVE_MODE_TAB) {
                EAERegistryHandler.INSTANCE.registerTab(e.getVanillaRegistry());
            }
        });
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        EAERegistryHandler.INSTANCE.onInit();
        EPPNetworkHandler.INSTANCE.init();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistryHandler.INSTANCE.init();
        PatternHotKey.init();
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(MODID, id);
    }

    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

}
