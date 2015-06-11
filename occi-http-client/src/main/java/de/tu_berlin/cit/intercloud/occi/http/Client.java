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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

public class Client {

	private static String baseURL = "http://cit-mac1.cit.tu-berlin.de:8080/intercloud/services";

	private static String flavorURL = baseURL + "/occi/flavor";

	private static String computeURL = baseURL + "/occi/compute";

	public static void main(String[] args) throws URISyntaxException, FileNotFoundException, UnsupportedEncodingException {
		System.out.println("URL: " + baseURL);
		
		// create files
		PrintWriter flavorWriter = new PrintWriter("getFlavor.txt", "UTF-8");
		flavorWriter.println("Count " + PerformanceMeter.getHead());
		PrintWriter createWriter = new PrintWriter("postCompute.txt", "UTF-8");
		createWriter.println("Count " + PerformanceMeter.getHead());
		PrintWriter deleteWriter = new PrintWriter("deleteCompute.txt", "UTF-8");
		deleteWriter.println("Count " + PerformanceMeter.getHead());

		// iterate over resources on server
		for (int r = 0; r < 100; r++) {

			// create performance meter
			PerformanceMeter flavorMeter = new PerformanceMeter();
			PerformanceMeter createMeter = new PerformanceMeter();
			PerformanceMeter deleteMeter = new PerformanceMeter();
	
			// create client
			Client client = new Client();
			String representation = "dummy";
	
			// measure 50 times
			for (int i = 0; i < 50; i++) {
				// get flavor
				flavorMeter.startTimer(i);
				representation = client.getFlavor();
				flavorMeter.stopTimer(i);
//				System.out.println("========Representation:========");
//				System.out.println(representation);
				// create vm
				createMeter.startTimer(i);
				URI vmURI = client.createVM(representation);
				createMeter.stopTimer(i);
//				System.out.println("============VM URI:============");
//				System.out.println(vmURI.getPath());
				// delete vm
				deleteMeter.startTimer(i);
				String message = client.deleteVM(vmURI);
				deleteMeter.stopTimer(i);
//				System.out.println("===========Message:============");
//				System.out.println(message);
			}

			System.out.println(r + " resources available");

			// write sample to file
			System.out.println("===========GET flavor:============");
			flavorMeter.print();
			flavorWriter.println(r + " " + flavorMeter.toString());
			System.out.println("===========POST VM:============");
			createMeter.print();
			createWriter.println(r + " " + createMeter.toString());
			System.out.println("===========DELETE VM:============");
			deleteMeter.print();
			deleteWriter.println(r + " " + deleteMeter.toString());

			// create a new resources and continue
			client.createVM(representation);
		}
				
		// close file writer
		flavorWriter.close();
		createWriter.close();
		deleteWriter.close();
	}

	private String deleteVM(URI vmURI) {
		String output = "";
		try {
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();

			// Create new deleteRequest with URI
			HttpDelete delRequest = new HttpDelete(vmURI);

			// Add additional header to getRequest which accepts text/plain data
//			delRequest.addHeader("Accept", "text/plain");

			// Execute your request and catch response
			HttpResponse response = httpClient.execute(delRequest);

			// Check for HTTP response code: 210 > success
			if (response.getStatusLine().getStatusCode() > 210) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			} else {
				output = response.getStatusLine().getReasonPhrase();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	private URI createVM(String representation) throws URISyntaxException {
		URI vmURI = new URI("");
		try {
			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();

			// Create new postRequest with below mentioned URL
			HttpPost postRequest = new HttpPost(computeURL);

			// Add additional header to postRequest which accepts text/plain data
			postRequest.addHeader("Content-Type", "text/plain");
//			postRequest.addHeader("Accept", "text/plain");

			// Execute your request and catch response
			HttpResponse response = httpClient.execute(postRequest);

			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			// Get-Capture Complete text/plain body response
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			// Simply iterate through text response and build a string
			String line = "";
			while ((line = br.readLine()) != null) {
				vmURI = new URI(line);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return vmURI;
	}

	private String getFlavor() {
		String output = "";
		try {

			// create HTTP Client
			HttpClient httpClient = HttpClientBuilder.create().build();

			// Create new getRequest with below mentioned URL
			HttpGet getRequest = new HttpGet(flavorURL);

			// Add additional header to getRequest which accepts text/plain data
			getRequest.addHeader("Accept", "text/plain");

			// Execute your request and catch response
			HttpResponse response = httpClient.execute(getRequest);

			// Check for HTTP response code: 200 = success
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			// Get-Capture Complete text/plain body response
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));

			// Simply iterate through text response and build a string
			StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				builder.append(line + "\n");
			}

			// return the content
			output = builder.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

}