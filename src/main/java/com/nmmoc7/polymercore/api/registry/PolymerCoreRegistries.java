package com.nmmoc7.polymercore.api.registry;

import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IDefinedMultiblock;
import com.nmmoc7.polymercore.api.multiblock.IMultiblockType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class PolymerCoreRegistries {
    public static IForgeRegistry<IDefinedMultiblock> DEFINED_MULTIBLOCKS =
        new RegistryBuilder<IDefinedMultiblock>()
            .setName(new ResourceLocation(PolymerCore.MOD_ID, "defined_multiblock"))
            .setType(IDefinedMultiblock.class)
            .disableSaving()
            .create();

    public static IForgeRegistry<IMultiblockType> MULTIBLOCK_TYPES =
        new RegistryBuilder<IMultiblockType>()
            .setName(new ResourceLocation(PolymerCore.MOD_ID, "multiblock_type"))
            .setType(IMultiblockType.class)
            .disableSaving()
            .create();
}