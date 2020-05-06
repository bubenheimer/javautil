/*
 * Copyright (c) 2015-2020 Uli Bubenheimer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.bubenheimer.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public final class SerialExecutor implements Executor {
    private final @NotNull Executor executor;

    private final Queue<Runnable> queue = new ArrayDeque<>();

    private boolean isRunning;

    private void work(
    ) {
        while (true) {
            final Runnable runnable;

            synchronized (queue) {
                runnable = queue.poll();
                if (runnable == null) {
                    isRunning = false;
                    return;
                }
            }

            runnable.run();
        }
    }

    public SerialExecutor(
            @SuppressWarnings("SameParameterValue") final @NotNull Executor executor
    ) {
        this.executor = executor;
    }

    @Override
    public void execute(
            final @NotNull Runnable r
    ) {
        synchronized (queue) {
            queue.add(r);

            if (isRunning) {
                return;
            }

            isRunning = true;
        }

        executor.execute(this::work);
    }
}
