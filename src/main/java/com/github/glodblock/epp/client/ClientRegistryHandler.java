package com.github.glodblock.epp.client;

import appeng.api.util.AEColor;
import appeng.client.render.StaticItemColor;
import appeng.init.client.InitScreens;
import com.github.glodblock.epp.client.gui.GuiExDrive;
import com.github.glodblock.epp.client.gui.GuiExIOBus;
import com.github.glodblock.epp.client.gui.GuiExInscriber;
import com.github.glodblock.epp.client.gui.GuiExInterface;
import com.github.glodblock.epp.client.gui.GuiExMolecularAssembler;
import com.github.glodblock.epp.client.gui.GuiExPatternProvider;
import com.github.glodblock.epp.client.gui.GuiExPatternTerminal;
import com.github.glodblock.epp.client.gui.GuiIngredientBuffer;
import com.github.glodblock.epp.client.gui.GuiPatternModifier;
import com.github.glodblock.epp.client.gui.GuiTagExportBus;
import com.github.glodblock.epp.client.gui.GuiTagStorageBus;
import com.github.glodblock.epp.client.gui.GuiWirelessConnector;
import com.github.glodblock.epp.client.gui.pattern.GuiCraftingPattern;
import com.github.glodblock.epp.client.gui.pattern.GuiProcessingPattern;
import com.github.glodblock.epp.client.gui.pattern.GuiSmithingTablePattern;
import com.github.glodblock.epp.client.gui.pattern.GuiStonecuttingPattern;
import com.github.glodblock.epp.client.hotkey.PatternHotKey;
import com.github.glodblock.epp.client.model.ExDriveModel;
import com.github.glodblock.epp.client.render.tesr.ExChargerTESR;
import com.github.glodblock.epp.client.render.tesr.ExDriveTESR;
import com.github.glodblock.epp.client.render.tesr.ExInscriberTESR;
import com.github.glodblock.epp.client.render.tesr.ExMolecularAssemblerTESR;
import com.github.glodblock.epp.client.render.tesr.IngredientBufferTESR;
import com.github.glodblock.epp.common.EPPItemAndBlock;
import com.github.glodblock.epp.common.tileentities.TileExCharger;
import com.github.glodblock.epp.common.tileentities.TileExDrive;
import com.github.glodblock.epp.common.tileentities.TileExInscriber;
import com.github.glodblock.epp.common.tileentities.TileExMolecularAssembler;
import com.github.glodblock.epp.common.tileentities.TileIngredientBuffer;
import com.github.glodblock.epp.container.ContainerExDrive;
import com.github.glodblock.epp.container.ContainerExIOBus;
import com.github.glodblock.epp.container.ContainerExInscriber;
import com.github.glodblock.epp.container.ContainerExInterface;
import com.github.glodblock.epp.container.ContainerExMolecularAssembler;
import com.github.glodblock.epp.container.ContainerExPatternProvider;
import com.github.glodblock.epp.container.ContainerExPatternTerminal;
import com.github.glodblock.epp.container.ContainerIngredientBuffer;
import com.github.glodblock.epp.container.ContainerPatternModifier;
import com.github.glodblock.epp.container.ContainerTagExportBus;
import com.github.glodblock.epp.container.ContainerTagStorageBus;
import com.github.glodblock.epp.container.ContainerWirelessConnector;
import com.github.glodblock.epp.container.pattern.ContainerCraftingPattern;
import com.github.glodblock.epp.container.pattern.ContainerProcessingPattern;
import com.github.glodblock.epp.container.pattern.ContainerSmithingTablePattern;
import com.github.glodblock.epp.container.pattern.ContainerStonecuttingPattern;
import com.glodblock.github.glodium.util.GlodUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientRegistryHandler {

    public static final ClientRegistryHandler INSTANCE = new ClientRegistryHandler();

    public void init() {
        this.registerGui();
    }

    public void registerGui() {
        InitScreens.register(ContainerExPatternProvider.TYPE, GuiExPatternProvider::new, "/screens/ex_pattern_provider.json");
        InitScreens.register(ContainerExInterface.TYPE, GuiExInterface::new, "/screens/ex_interface.json");
        InitScreens.register(ContainerExIOBus.EXPORT_TYPE, GuiExIOBus::new, "/screens/ex_export_bus.json");
        InitScreens.register(ContainerExIOBus.IMPORT_TYPE, GuiExIOBus::new, "/screens/ex_import_bus.json");
        InitScreens.register(ContainerExPatternTerminal.TYPE, GuiExPatternTerminal::new, "/screens/ex_pattern_access_terminal.json");
        InitScreens.register(ContainerWirelessConnector.TYPE, GuiWirelessConnector::new, "/screens/wireless_connector.json");
        InitScreens.register(ContainerIngredientBuffer.TYPE, GuiIngredientBuffer::new, "/screens/ingredient_buffer.json");
        InitScreens.register(ContainerExDrive.TYPE, GuiExDrive::new, "/screens/ex_drive.json");
        InitScreens.register(ContainerPatternModifier.TYPE, GuiPatternModifier::new, "/screens/pattern_modifier.json");
        InitScreens.register(ContainerExMolecularAssembler.TYPE, GuiExMolecularAssembler::new, "/screens/ex_molecular_assembler.json");
        InitScreens.register(ContainerExInscriber.TYPE, GuiExInscriber::new, "/screens/ex_inscriber.json");
        InitScreens.register(ContainerTagStorageBus.TYPE, GuiTagStorageBus::new, "/screens/tag_storage_bus.json");
        InitScreens.register(ContainerTagExportBus.TYPE, GuiTagExportBus::new, "/screens/tag_export_bus.json");
        MenuScreens.register(ContainerProcessingPattern.TYPE, GuiProcessingPattern::new);
        MenuScreens.register(ContainerCraftingPattern.TYPE, GuiCraftingPattern::new);
        MenuScreens.register(ContainerStonecuttingPattern.TYPE, GuiStonecuttingPattern::new);
        MenuScreens.register(ContainerSmithingTablePattern.TYPE, GuiSmithingTablePattern::new);
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public void registerColorHandler(RegisterColorHandlersEvent.Item event) {
        var color = event.getItemColors();
        color.register(new StaticItemColor(AEColor.TRANSPARENT), EPPItemAndBlock.EX_PATTERN_TERMINAL);
    }

    @SubscribeEvent
    public void registerModels(ModelEvent.RegisterGeometryLoaders event) {
        BlockEntityRenderers.register(GlodUtil.getTileType(TileIngredientBuffer.class), IngredientBufferTESR::new);
        BlockEntityRenderers.register(GlodUtil.getTileType(TileExDrive.class), ExDriveTESR::new);
        BlockEntityRenderers.register(GlodUtil.getTileType(TileExMolecularAssembler.class), ExMolecularAssemblerTESR::new);
        BlockEntityRenderers.register(GlodUtil.getTileType(TileExInscriber.class), ExInscriberTESR::new);
        BlockEntityRenderers.register(GlodUtil.getTileType(TileExCharger.class), ExChargerTESR::new);
        event.register("ex_drive", new ExDriveModel.Loader());
    }

    @SubscribeEvent
    public void registerHotKey(RegisterKeyMappingsEvent e) {
        e.register(PatternHotKey.getHotKey());
    }

}
