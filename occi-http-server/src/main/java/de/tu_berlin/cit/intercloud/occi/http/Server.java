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

package de.tu_berlin.cit.intercloud.occi.http;


public class Server {

	public static void main( String[] args ) throws Exception
	   {
	      String url = ( args.length > 0 ) ? args[0] : "http://localhost:4434";
	      String sec = ( args.length > 1 ) ? args[1] : "10";

//	      SelectorThread srv = GrizzlyServerFactory.create( url );

	      System.out.println( "URL: " + url );
	      Thread.sleep( 1000 * Integer.parseInt( sec ) );
//	      srv.stopEndpoint();
	   }

}