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
package org.blockartistry.lib.compat;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class EntityLivingBaseUtil {

	private static Field hideParticles = ReflectionHelper.findField(EntityLivingBase.class, "HIDE_PARTICLES",
			"field_184634_g");
	private static Field isJumping = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");

	private EntityLivingBaseUtil() {

	}

	@SuppressWarnings("unchecked")
	public static DataParameter<Boolean> getHideParticles() {
		try {
			return (DataParameter<Boolean>) hideParticles.get(null);
		} catch (@Nonnull final Throwable t) {
			;
		}
		return null;
	}

	public static boolean isJumping(@Nonnull final EntityLivingBase entity) {
		try {
			return isJumping.getBoolean(entity);
		} catch (@Nonnull final Throwable t) {
			;
		}
		return false;
	}

}
