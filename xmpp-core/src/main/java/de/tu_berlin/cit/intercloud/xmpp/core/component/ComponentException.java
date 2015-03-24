/**
 * Copyright (C) 2004-2009 Jive Software. All rights reserved.
 * Copyright (C) 2014-2015 TU Berlin. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tu_berlin.cit.intercloud.xmpp.core.component;

import de.tu_berlin.cit.intercloud.xmpp.core.packet.StreamError;


/**
 * Thrown when an exception occors with a Component.
 *
 * @author Matt Tucker
 */
public class ComponentException extends Exception {

	private static final long serialVersionUID = -4133202596762415887L;
	
	private StreamError streamError;

    public ComponentException() {
        super();
    }

    public ComponentException(String message) {
        super(message);
    }

    public ComponentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComponentException(Throwable cause) {
        super(cause);
    }

    public ComponentException(String message, StreamError streamError) {
        super(message);
        this.streamError = streamError;
    }

    public ComponentException(StreamError streamError) {
        super(streamError.getCondition().toXMPP());
        this.streamError = streamError;
    }

    public StreamError getStreamError() {
        return streamError;
    }
}
