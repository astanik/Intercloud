package de.tu_berlin.cit.intercloud.xmpp.component;


import de.tu_berlin.cit.intercloud.xmpp.core.component.AbstractComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;

public class ExchangeComponent extends AbstractComponent {

	//private ExternalComponentManager mgr = null;
	
	@Override
	public String getName() {
		return "Intercloud Exchange";
	}

	@Override
	public String getDescription() {
		return "This is the Intercloud Exchange service.";
	}
/*
	@Override
	public void processPacket(Packet packet) {
		// TODO Auto-generated method stub

	}
	*/
	/**
	 * Override this method to handle the IQ stanzas of type <tt>result</tt>
	 * that are received by the component. If you do not override this method,
	 * the stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>result</tt> that was received by
	 *            this component.
	 */
	@Override
	protected void handleIQResult(IQ iq) {
		// Doesn't do anything. Override this method to process IQ result
		// stanzas.
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>error</tt> that
	 * are received by the component. If you do not override this method, the
	 * stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>error</tt> that was received by this
	 *            component.
	 */
	@Override
	protected void handleIQError(IQ iq) {
		// Doesn't do anything. Override this method to process IQ error
		// stanzas.
		log.info("(serving component '{}') IQ stanza "
				+ "of type <tt>error</tt> received: ", getName(), iq.toXML());
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>get</tt> that
	 * could not be processed by the {@link AbstractComponent} implementation.
	 * 
	 * Note that, as any IQ stanza of type <tt>get</tt> must be replied to,
	 * returning <tt>null</tt> from this method equals returning an IQ error
	 * stanza of type 'feature-not-implemented' (this behavior can be disabled
	 * by setting the <tt>enforceIQResult</tt> argument in the constructor to
	 * <tt>false</tt>).
	 * 
	 * Note that if this method throws an Exception, an IQ stanza of type
	 * <tt>error</tt>, condition 'internal-server-error' will be returned to the
	 * sender of the original request.
	 * 
	 * The default implementation of this method returns <tt>null</tt>. It is
	 * expected that most child classes will override this method.
	 * 
	 * @param iq
	 *            The IQ request stanza of type <tt>get</tt> that was received
	 *            by this component.
	 * @return the response the to request stanza, or <tt>null</tt> to indicate
	 *         'feature-not-available'.
	 */
	@Override
	protected IQ handleIQGet(IQ iq) throws Exception {
		// Doesn't do anything. Override this method to process IQ get
		// stanzas.
		return null;
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>set</tt> that
	 * could not be processed by the {@link AbstractComponent} implementation.
	 * 
	 * Note that, as any IQ stanza of type <tt>set</tt> must be replied to,
	 * returning <tt>null</tt> from this method equals returning an IQ error
	 * stanza of type 'feature-not-implemented' {this behavior can be disabled
	 * by setting the <tt>enforceIQResult</tt> argument in the constructor to
	 * <tt>false</tt>).
	 * 
	 * Note that if this method throws an Exception, an IQ stanza of type
	 * <tt>error</tt>, condition 'internal-server-error' will be returned to the
	 * sender of the original request.
	 * 
	 * The default implementation of this method returns <tt>null</tt>. It is
	 * expected that most child classes will override this method.
	 * 
	 * @param iq
	 *            The IQ request stanza of type <tt>set</tt> that was received
	 *            by this component.
	 * @return the response the to request stanza, or <tt>null</tt> to indicate
	 *         'feature-not-available'.
	 */
	@Override
	protected IQ handleIQSet(IQ iq) throws Exception {
		// Doesn't do anything. Override this method to process IQ set
		// stanzas.
		return null;
	}
	/*

	@Override
	public void initialize(JID jid, ComponentManager componentManager)
			throws ComponentException {
		mgr = (ExternalComponentManager) componentManager;

	}

	@Override
	public void start() {
		// do nothing
	}

	@Override
	public void shutdown() {
		// do nothing
	}
	*/

}
