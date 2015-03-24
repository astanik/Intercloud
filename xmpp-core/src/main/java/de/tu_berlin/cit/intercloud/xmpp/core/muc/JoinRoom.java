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

package de.tu_berlin.cit.intercloud.xmpp.core.muc;

import de.tu_berlin.cit.intercloud.xmpp.core.packet.Presence;


/**
 * Initial presence sent when joining an existing room or creating a new room. The JoinRoom presence
 * indicates the posibility of the sender to speak MUC.<p>
 *
 * Code example:
 * <pre>
 * // Join an existing room or create a new one.
 * JoinRoom joinRoom = new JoinRoom("john@jabber.org/notebook", "room@conference.jabber.org/nick");
 *
 * component.sendPacket(joinRoom);
 * </pre>
 *
 * @author Gaston Dombiak
 */
public class JoinRoom extends Presence {

    /**
     * Creates a new Presence packet that could be sent to a MUC service in order to join
     * an existing MUC room or create a new one.
     *
     * @param from the real full JID of the user that will join or create a MUC room.
     * @param to a full JID where the bare JID is the MUC room address and the resource is the
     *        nickname of the user joining the room.
     */
    public JoinRoom(String from, String to) {
        super();
        setFrom(from);
        setTo(to);
        addChildElement("x", "http://jabber.org/protocol/muc");
    }
}
