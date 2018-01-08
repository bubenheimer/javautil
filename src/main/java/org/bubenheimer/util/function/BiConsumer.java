/*
 * Copyright (c) 2015-2017 Uli Bubenheimer. All rights reserved.
 */

package org.bubenheimer.util.function;

public interface BiConsumer<T, U> {
    void accept(T t, U u);
}
