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

package de.tu_berlin.cit.intercloud.occi.http.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * 
 */
@Provider
public class URIListProvider
    implements MessageBodyWriter<List<URI>>, MessageBodyReader<List<URI>>
{

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyReader#isReadable(java.lang.Class, java.lang.reflect.Type,
     * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    public boolean
        isReadable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        if ( genericType instanceof ParameterizedType )
        {
            ParameterizedType parameterized = (ParameterizedType) genericType;
            Type[] typeArgs = parameterized.getActualTypeArguments();
            if ( parameterized.getRawType() == List.class && typeArgs.length == 1 && typeArgs[0] == URI.class )
            {
                // make sure that the content type is an uri list
                if ( mediaType.isCompatible( new MediaType( "text", "uri-list" ) ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyReader#readFrom(java.lang.Class, java.lang.reflect.Type,
     * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap,
     * java.io.InputStream)
     */
    public List<URI> readFrom( Class<List<URI>> type, Type genericType, Annotation[] annotations,
                               MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
                               InputStream entityStream ) throws IOException
    {
        BufferedReader reader = new BufferedReader( new InputStreamReader( entityStream ) );
        Vector<URI> input = new Vector<URI>();

        String url;
        while ( ( url = reader.readLine() ) != null )
        {
            try
            {
                input.add( new URI( url ) );
            }
            catch ( URISyntaxException e )
            {
                new WebApplicationException( e );
            }
        }

        return input;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#isWriteable(java.lang.Class, java.lang.reflect.Type,
     * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    public boolean
        isWriteable( Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType )
    {
        if ( genericType instanceof ParameterizedType )
        {
            ParameterizedType parameterized = (ParameterizedType) genericType;
            Type[] typeArgs = parameterized.getActualTypeArguments();
            if ( parameterized.getRawType() == List.class && typeArgs.length == 1 && typeArgs[0] == URI.class )
            {
                // make sure that the content type is an uri list
                if ( mediaType.isCompatible( new MediaType( "text", "uri-list" ) ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#getSize(java.lang.Object, java.lang.Class,
     * java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
     */
    public long getSize( List<URI> t, Class<?> type, Type genericType, Annotation[] annotations,
                         MediaType mediaType )
    {
        return toString( t ).getBytes().length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.ws.rs.ext.MessageBodyWriter#writeTo(java.lang.Object, java.lang.Class,
     * java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType,
     * javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
     */
    public void writeTo( List<URI> t, Class<?> type, Type genericType, Annotation[] annotations,
                         MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                         OutputStream entityStream ) throws IOException
    {
        entityStream.write( toString( t ).getBytes() );
    }

    private String toString( List<URI> t )
    {
        StringBuffer result = new StringBuffer();

        Iterator<URI> iterator = t.iterator();
        while ( iterator.hasNext() )
        {
            result.append( iterator.next().toASCIIString() );
            result.append( "\n" );
        }
        return result.toString();
    }
}
