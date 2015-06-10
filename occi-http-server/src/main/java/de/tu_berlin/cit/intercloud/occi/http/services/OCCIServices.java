/**
 * Copyright (C) 2012-2015 TU Berlin. All rights reserved.
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

package de.tu_berlin.cit.intercloud.occi.http.services;

import java.net.URI;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;


@Path("/occi")
public class OCCIServices {

    @Context
    private UriInfo uriInfo;

	private ConcurrentHashMap<Integer,String> computeMap = new ConcurrentHashMap<Integer,String>();
	
    @GET
    @Path( "/flavor" )
    @Produces( MediaType.TEXT_PLAIN )
    public String getFlavor() {
    	StringBuilder flavor = new StringBuilder();
    	flavor.append("Category: compute; \n");
    	flavor.append("scheme='http://schemas.ogf.org/occi/infrastructure#'; \n");
    	flavor.append("class='kind'; \n");
    	flavor.append("X-OCCI-Attribute: occi.core.title='My Dummy VM' \n");
    	flavor.append("X-OCCI-Attribute: occi.compute.architecture='x86' \n");
    	flavor.append("X-OCCI-Attribute: occi.compute.memory=2.0 \n");
    	flavor.append("X-OCCI-Attribute: occi.compute.cores=2 \n");
    	return flavor.toString();
    }

    @POST
    @Path( "/compute" )
    @Consumes( MediaType.TEXT_PLAIN )
    @Produces( "text/uri" )
    public URI createVM( String input ) {
    	// check input
    	if(input == null) {
            String message = "compute resource with 'null' representation cannot be created.";
            throw new ServiceUnavailableException( message );
    	}
    	
    	// find unused id
    	int id = 0;
    	do {
    		Random ran = new Random();
    		id = ran.nextInt(100000);
    	}while(id == 0 || this.computeMap.containsKey(id));
    	
    	// add representation to map
    	this.computeMap.put(id, input);
    	
    	// build uri
    	URI resourcetUri = getUriBuilder().clone().path(Integer.toString(id)).build();
        return resourcetUri; 
    }

    @GET
    @Path( "/compute" )
    @Produces( "text/uri-list" )
    public List<URI> listVMs() {
        List<URI> uris = new Vector<URI>();
        for(Enumeration<Integer> e = this.computeMap.keys(); e.hasMoreElements(); )
        {
            String resourceId = Integer.toString(e.nextElement());

            URI resourceUri = getUriBuilder().path( resourceId ).build();

            uris.add( resourceUri );
        }

        return uris;
    }

    @Path( "/compute/{computeId}" )
    public ComputeService getAgreement( @PathParam( "computeId" ) String pathId )
    {
       	int id = Integer.parseInt(pathId);
        // lookup the map with the given id 
        String computeRepresentation = this.computeMap.get(id);

        // if the map returns null, the resource is not available
        if ( computeRepresentation == null )
        {
                String message =
                    MessageFormat.format( "compute resource with id ''{0}'' is unknown.", id );

                throw new ServiceUnavailableException( message );
        }
        return new ComputeService( this, id, computeRepresentation );
    }

	public void removeCompute(int id) {
		this.computeMap.remove(id);
	}
	
    private UriBuilder getUriBuilder()
    {
        return uriInfo.getAbsolutePathBuilder();
    }


}