/* This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.lib.scanner;

import java.util.Random;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.blockartistry.lib.chunk.IBlockAccessEx;
import org.blockartistry.lib.logging.ModLog;
import org.blockartistry.lib.random.XorShiftRandom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class Scanner implements ITickable, Callable<Void> {

	private final static int MAX_BLOCKS_TICK = 3000;

	protected final String name;

	protected final int xRange;
	protected final int yRange;
	protected final int zRange;

	protected final int xSize;
	protected final int ySize;
	protected final int zSize;
	protected final int blocksPerTick;
	protected final int volume;

	protected final ScanLocus locus;

	protected final Random random = new XorShiftRandom();
	protected final BlockPos.MutableBlockPos workingPos = new BlockPos.MutableBlockPos();

	protected ModLog log;

	public Scanner(@Nonnull final ScanLocus locus, @Nonnull final String name, final int range) {
		this(locus, name, range, 0);
	}

	public Scanner(@Nonnull final ScanLocus locus, @Nonnull final String name, final int range,
			final int blocksPerTick) {
		this(locus, name, range, range, range, blocksPerTick);
	}

	public Scanner(@Nonnull final ScanLocus locus, @Nonnull final String name, final int xRange, final int yRange,
			final int zRange) {
		this(locus, name, xRange, yRange, zRange, 0);
	}

	public Scanner(@Nonnull final ScanLocus locus, @Nonnull final String name, final int xRange, final int yRange,
			final int zRange, final int blocksPerTick) {
		this.log = ModLog.NULL_LOGGER;
		this.name = name;
		this.xRange = xRange;
		this.yRange = yRange;
		this.zRange = zRange;

		this.xSize = xRange * 2;
		this.ySize = yRange * 2;
		this.zSize = zRange * 2;
		this.volume = this.xSize * this.ySize * this.zSize;
		if (blocksPerTick == 0)
			this.blocksPerTick = Math.min(this.volume / 20, MAX_BLOCKS_TICK);
		else
			this.blocksPerTick = Math.min(blocksPerTick, MAX_BLOCKS_TICK);

		this.locus = locus;
	}

	public void setLogger(@Nonnull final ModLog log) {
		this.log = log;
	}

	/**
	 * The volume of the scan area
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * Invoked when a block of interest is discovered. The BlockPos provided is not
	 * safe to hold on to beyond the call so if it needs to be kept it needs to be
	 * copied.
	 */
	public abstract void blockScan(@Nonnull final IBlockState state, @Nonnull final BlockPos pos,
			@Nonnull final Random rand);

	/**
	 * Determines if the block is of interest to the effects. Override to provide
	 * logic beyond the basics.
	 */
	protected boolean interestingBlock(final IBlockState state) {
		return state != Blocks.AIR.getDefaultState();
	}

	@Override
	public Void call() {
		update();
		return null;
	}

	public void preScan() {

	}

	public void postScan() {

	}

	@Override
	public void update() {

		preScan();

		final IBlockAccessEx provider = this.locus.getWorld();
		for (int count = 0; count < this.blocksPerTick; count++) {
			final BlockPos pos = nextPos(this.workingPos, this.random);
			if (pos == null)
				break;
			final IBlockState state = provider.getBlockState(pos);
			if (interestingBlock(state)) {
				blockScan(state, pos, this.random);
			}
		}

		postScan();

	}

	/**
	 * Provide the next block position to be processed. For memory efficiency the
	 * provided mutable should be used to store the coordinate information and
	 * returned from the function call.
	 */
	@Nullable
	protected abstract BlockPos nextPos(@Nonnull final BlockPos.MutableBlockPos pos, @Nonnull final Random rand);

}
