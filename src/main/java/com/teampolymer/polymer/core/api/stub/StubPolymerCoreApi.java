package com.teampolymer.polymer.core.api.stub;


import com.teampolymer.polymer.core.api.PolymerCoreApi;
import com.teampolymer.polymer.core.api.capability.IChunkMultiblockStorage;
import com.teampolymer.polymer.core.api.manager.IArchetypeMultiblockManager;
import com.teampolymer.polymer.core.api.multiblock.IAssembledMultiblock;
import com.teampolymer.polymer.core.api.multiblock.assembled.IWorldMultiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class StubPolymerCoreApi implements PolymerCoreApi {
    @Override
    public IArchetypeMultiblockManager getArchetypeManager() {
        throw new NotImplementedException("IMultiblockDefinitionManager");
    }

    @Override
    public Capability<IChunkMultiblockStorage> getChunkMultiblockCapability() {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, UUID id) {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos, boolean coreBlock) {
        return null;
    }

    @Override
    public IAssembledMultiblock findMultiblock(World world, BlockPos pos) {
        return null;
    }

    @Override
    public Collection<IWorldMultiblock> findWorldMultiblocks(World world) {
        return Collections.emptyList();
    }
}
