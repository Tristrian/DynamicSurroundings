/*
 * This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
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

package org.blockartistry.lib.expression;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ITickable;

public class DynamicVariantList implements ITickable {

	protected final List<IDynamicVariant<?>> variants = new ArrayList<IDynamicVariant<?>>();

	public DynamicVariantList() {

	}

	/*
	 * Called to update the state of the dynamic variants contain
	 * within the list.
	 */
	@Override
	public void update() {
		for (final IDynamicVariant<?> dv : this.variants)
			dv.update();
	}

	/*
	 * Adds a dynamic variant to be managed to the list.
	 */
	public void add(final IDynamicVariant<?> dv) {
		this.variants.add(dv);
	}

	/*
	 * Attaches the dynamic variants in the list to the
	 * specified expression.
	 */
	public void attach(final Expression exp) {
		for (final IDynamicVariant<?> dv : this.variants)
			exp.addVariable((Variant) dv);
	}

	/*
	 * Gets the list of dynamic variants.
	 */
	public List<IDynamicVariant<?>> getList() {
		return this.variants;
	}

}