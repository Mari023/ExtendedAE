package com.github.glodblock.epp.common.tileentities;

import appeng.api.stacks.AEKeyType;
import appeng.blockentity.AEBaseBlockEntity;
import appeng.capabilities.Capabilities;
import appeng.helpers.externalstorage.GenericStackInv;
import com.github.glodblock.epp.common.EPPItemAndBlock;
import com.github.glodblock.epp.util.FCUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileIngredientBuffer extends AEBaseBlockEntity {

    private final GenericStackInv buffer;

    public TileIngredientBuffer(BlockPos pos, BlockState blockState) {
        super(FCUtil.getTileType(TileIngredientBuffer.class, TileIngredientBuffer::new, EPPItemAndBlock.INGREDIENT_BUFFER), pos, blockState);
        this.buffer = new GenericStackInv(this::setChanged, 36);
        this.buffer.setCapacity(AEKeyType.fluids(), 64000);
    }

    @Override
    public void addAdditionalDrops(Level level, BlockPos pos, List<ItemStack> drops) {
        super.addAdditionalDrops(level, pos, drops);
        for (int index = 0; index < this.buffer.size(); index ++) {
            var stack = this.buffer.getStack(index);
            if (stack != null) {
                stack.what().addDrops(stack.amount(), drops, level, pos);
            }
        }
    }

    @Override
    public void saveAdditional(CompoundTag data) {
        super.saveAdditional(data);
        this.buffer.writeToChildTag(data, "buffer");
    }

    @Override
    public void loadTag(CompoundTag data) {
        super.loadTag(data);
        this.buffer.readFromChildTag(data, "buffer");
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (capability == Capabilities.GENERIC_INTERNAL_INV) {
            return LazyOptional.of(() -> this.buffer).cast();
        }
        return super.getCapability(capability, facing);
    }

    public GenericStackInv getInventory() {
        return this.buffer;
    }

}
