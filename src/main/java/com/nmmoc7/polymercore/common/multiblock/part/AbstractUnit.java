package com.nmmoc7.polymercore.common.multiblock.part;

import com.nmmoc7.polymercore.api.multiblock.MultiblockDirection;
import com.nmmoc7.polymercore.api.multiblock.part.IMultiblockUnit;
import net.minecraft.block.BlockState;

import java.util.Collections;
import java.util.List;

public abstract class AbstractUnit implements IMultiblockUnit {
    protected List<BlockState> samples;

    public AbstractUnit(List<BlockState> samples) {
        this.samples = Collections.unmodifiableList(samples);
    }


    @Override
    public List<BlockState> getSampleBlocks() {
        return samples;
    }

    @Override
    public IMultiblockUnit withDirection(MultiblockDirection direction) {
        return this;
    }
}