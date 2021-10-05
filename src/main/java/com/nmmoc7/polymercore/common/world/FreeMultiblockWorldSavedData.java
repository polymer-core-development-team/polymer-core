package com.nmmoc7.polymercore.common.world;

import com.google.common.collect.HashBiMap;
import com.nmmoc7.polymercore.PolymerCore;
import com.nmmoc7.polymercore.api.multiblock.IAssembledMultiblock;
import com.nmmoc7.polymercore.api.multiblock.assembled.IFreeMultiblock;
import com.nmmoc7.polymercore.api.util.MultiblockUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FreeMultiblockWorldSavedData extends WorldSavedData {
    private static final String NAME = "polymer_core_multiblock";
    private World world;

    public FreeMultiblockWorldSavedData(World world) {
        super(NAME);
        this.world = world;
        this.assembledMultiblockMap = new ConcurrentHashMap<>();
    }

    private final ConcurrentHashMap<UUID, IFreeMultiblock> assembledMultiblockMap;
    private final HashBiMap<UUID, BlockPos> positions = HashBiMap.create();

    public IFreeMultiblock getAssembledMultiblock(UUID multiblockId) {
        return assembledMultiblockMap.get(multiblockId);
    }

    public IFreeMultiblock getAssembledMultiblock(BlockPos corePos) {
        return assembledMultiblockMap.get(positions.inverse().get(corePos));
    }

    public Collection<IFreeMultiblock> getAssembledMultiblocks() {
        return Collections.unmodifiableCollection(assembledMultiblockMap.values());
    }

    public void validateMultiblocksInChunk(ChunkPos pos) {
        for (BlockPos value : positions.values()) {
            if ((value.getX() >> 4) == pos.x && (value.getZ() >> 4) == pos.z) {
                UUID uuid = positions.inverse().get(value);
                PolymerCore.LOG.error("Found invalidate multiblock {} in {}", uuid, value);
                IFreeMultiblock assembledMultiblock = getAssembledMultiblock(uuid);
                if (assembledMultiblock != null) {
                    assembledMultiblock.disassemble();
                }
                removeAssembledMultiblock(uuid);
            }
        }

    }

    public void addAssembledMultiblock(IFreeMultiblock multiblock) {
        IFreeMultiblock result = assembledMultiblockMap.put(multiblock.getMultiblockId(), multiblock);
        if (result != null) {
            PolymerCore.LOG.error("Attempting to add an multiblock with existing id: {}", multiblock.getMultiblockId());
        }
        if (positions.containsValue(multiblock.getOffset())) {
            UUID uuid = positions.inverse().get(multiblock.getOffset());
            PolymerCore.LOG.error("Attempting to add an multiblock {} to position {} where there are another multiblock {}",
                multiblock.getMultiblockId(), multiblock.getOffset(), uuid);
            IFreeMultiblock assembledMultiblock = getAssembledMultiblock(uuid);
            if (assembledMultiblock != null) {
                assembledMultiblock.disassemble();
            }
            removeAssembledMultiblock(uuid);
        }
        positions.put(multiblock.getMultiblockId(), multiblock.getOffset());
        markDirty();
    }

    public void removeAssembledMultiblock(UUID multiblockId) {
        assembledMultiblockMap.remove(multiblockId);
        positions.remove(multiblockId);
        markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        assembledMultiblockMap.clear();
        ListNBT multiblocks = nbt.getList("assembled_multiblocks", 10);
        for (INBT multiblock : multiblocks) {
            IFreeMultiblock assembledMultiblock = (IFreeMultiblock) MultiblockUtils.deserializeNBT(world, multiblock);
            if (assembledMultiblock == null) {
                continue;
            }
            assembledMultiblockMap.put(assembledMultiblock.getMultiblockId(), assembledMultiblock);
            positions.put(assembledMultiblock.getMultiblockId(), assembledMultiblock.getOffset());
        }
        if (PolymerCore.LOG.isDebugEnabled() && assembledMultiblockMap.size() > 0) {
            PolymerCore.LOG.debug("Loaded {} machines in world {}", assembledMultiblockMap.size(), world.getDimensionKey());
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        for (IFreeMultiblock value : assembledMultiblockMap.values()) {
            listNBT.add(value.serializeNBT());
        }
        compound.put("assembled_multiblocks", listNBT);
        if (PolymerCore.LOG.isDebugEnabled() && listNBT.size() > 0) {
            PolymerCore.LOG.debug("Saving {} machines in world {}", listNBT.size(), world.getDimensionKey());
        }
        return compound;
    }

    public static FreeMultiblockWorldSavedData get(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(() -> new FreeMultiblockWorldSavedData(world), NAME);
    }
}
