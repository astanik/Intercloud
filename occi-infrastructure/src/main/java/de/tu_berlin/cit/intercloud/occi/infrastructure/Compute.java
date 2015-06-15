package de.tu_berlin.cit.intercloud.occi.infrastructure;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.tu_berlin.cit.intercloud.occi.core.Link;
import de.tu_berlin.cit.intercloud.occi.core.Resource;
import de.tu_berlin.cit.intercloud.occi.core.classification.Kind;
import de.tu_berlin.cit.intercloud.occi.core.classification.Mixin;
import de.tu_berlin.cit.intercloud.xmpp.rest.ResourceInstance;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Method;
import de.tu_berlin.cit.intercloud.xmpp.rest.annotations.Path;

@Path("/compute")
@Kind()
public class Compute extends Resource implements ResourceInstance {

	final static public String InfrastructureSchema = AbstractKind.InfrastructureSchema + "compute";

	public Compute(String id, String title, Kind kind, List<Mixin> mixins,
			List<Link> links, String summary) {
		super(id, title, kind, mixins, links, summary);
	}
	
	
	public Compute() {
		// TODO Auto-generated constructor stub
	}

	@Method("GET")
	@Produces()
	public List<XmppURI> getVMs() {
		
	}

	@Method("POST")
    @Consumes( MediaType.TEXT_PLAIN )
    @Produces( "text/uri" )
	public XmppURI createVM(@Param() ImageMixin image, @Param() FlavorMixin falvor) {
		// create a virtual machine and return its uri
	}

	//	@Action()
	public void start() {
		
	}
	
//	@Action()
	public void stop() {
		
	}
	
	
}