/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package tech.gpu.lib.utils;

import tech.gpu.lib.ex.GpuRuntimeException;
import tech.gpu.lib.utils.reflect.ClassReflection;
import tech.gpu.lib.utils.reflect.Constructor;
import tech.gpu.lib.utils.reflect.ReflectionException;

/**
 * Pool that creates new instances of a type using reflection. The type must have a zero argument constructor.
 * {@link Constructor#setAccessible(boolean)} will be used if the class and/or constructor is not visible.
 * <p>
 *
 * @author Nathan Sweet
 * @deprecated Please use {@link DefaultPool} instead.
 */
@Deprecated
public class ReflectionPool<T> extends Pool<T> {
    private final tech.gpu.lib.utils.reflect.Constructor constructor;

    public ReflectionPool(Class<T> type) {
        this(type, 16, Integer.MAX_VALUE);
    }

    public ReflectionPool(Class<T> type, int initialCapacity) {
        this(type, initialCapacity, Integer.MAX_VALUE);
    }

    public ReflectionPool(Class<T> type, int initialCapacity, int max) {
        super(initialCapacity, max);
        constructor = findConstructor(type);
        if (constructor == null)
            throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + type.getName());
    }

    private Constructor findConstructor(Class<T> type) {
        try {
            return ClassReflection.getConstructor(type, (Class[]) null);
        } catch (Exception ex1) {
            try {
                Constructor constructor = ClassReflection.getDeclaredConstructor(type, (Class[]) null);
                constructor.setAccessible(true);
                return constructor;
            } catch (ReflectionException ex2) {
                return null;
            }
        }
    }

    protected T newObject() {
        try {
            return (T) constructor.newInstance((Object[]) null);
        } catch (Exception ex) {
            throw new GpuRuntimeException("Unable to create new instance: " + constructor.getDeclaringClass().getName(), ex);
        }
    }
}
