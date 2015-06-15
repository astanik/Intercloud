package de.tu_berlin.cit.intercloud.xmpp.component;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import de.tu_berlin.cit.intercloud.xmpp.core.component.AbstractComponent;
import de.tu_berlin.cit.intercloud.xmpp.core.packet.IQ;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceContainer;
import de.tu_berlin.cit.intercloud.xmpp.rest.xml.ResourceDocument;
import de.tu_berlin.cit.intercloud.xmpp.rest.xwadl.ResourceTypeDocument;

public class ExchangeComponent extends AbstractComponent {

	private final ResourceContainer container;
	
	public ExchangeComponent(ResourceContainer container) {
		this.container = container;
	}

	@Override
	public String getName() {
		return "Intercloud Exchange";
	}

	@Override
	public String getDescription() {
		return "This is the Intercloud Exchange service.";
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
		Element child = iq.getChildElement();
		String path = child.attribute("path").getValue();
		ResourceTypeDocument xwadl = this.container.getXWADL(path);
		Document doc = DocumentHelper.parseText(xwadl.toString());
		IQ response = IQ.createResultIQ(iq);
		response.setChildElement(doc.getRootElement());
		return response;
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
		Element child = iq.getChildElement();
		ResourceDocument xmlRequest = ResourceDocument.Factory.parse(child.asXML());
		ResourceDocument xmlResponse = this.container.execute(xmlRequest);
		Document doc = DocumentHelper.parseText(xmlResponse.toString());
		IQ response = IQ.createResultIQ(iq);
		response.setChildElement(doc.getRootElement());
		return response;
	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>result</tt>
	 * that are received by the component. If you do not override this method,
	 * the stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>result</tt> that was received by
	 *            this component.
	 */
//	@Override
//	protected void handleIQResult(IQ iq) {
		// Doesn't do anything. Override this method to process IQ result
		// stanzas.
//	}

	/**
	 * Override this method to handle the IQ stanzas of type <tt>error</tt> that
	 * are received by the component. If you do not override this method, the
	 * stanzas are ignored.
	 * 
	 * @param iq
	 *            The IQ stanza of type <tt>error</tt> that was received by this
	 *            component.
	 */
//	@Override
//	protected void handleIQError(IQ iq) {
		// Doesn't do anything. Override this method to process IQ error
		// stanzas.
//		log.info("(serving component '{}') IQ stanza "
//				+ "of type <tt>error</tt> received: ", getName(), iq.toXML());
//	}

}
