/*
 *
 * Copyright 2017-2018 Nitrite author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dizitart.no2.collection;

import lombok.extern.slf4j.Slf4j;
import org.dizitart.no2.Document;
import org.dizitart.no2.common.event.ChangeListener;
import org.dizitart.no2.common.event.ChangedItem;
import org.dizitart.no2.common.event.NitriteEventBus;

/**
 * @author Anindya Chatterjee.
 */
@Slf4j
class ChangeEventBus extends NitriteEventBus<ChangedItem<Document>, ChangeListener> {

    @Override
    public void post(ChangedItem<Document> changedItem) {
        for (final ChangeListener listener : getListeners()) {
            String threadName = Thread.currentThread().getName();
            changedItem.setOriginatingThread(threadName);

            getEventExecutor().submit(() -> listener.onChange(changedItem));
        }
    }
}
