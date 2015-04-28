package de.tu_berlin.cit.intercloud.root.services;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("test")
@Stateless
public class Test {
	
	@EJB
    private TestSingleton nameBean;

    @GET
    @Produces("text/html")
    public String getHtml() {
        return "<h2>Hello "+nameBean.getName()+"</h2>";
    }

    @PUT
    @Consumes("text/plain")
    public void put(String content) {
        nameBean.setName(content);
    }
}
