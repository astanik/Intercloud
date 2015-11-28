/**
 * Copyright 2010-2015 Complex and Distributed IT Systems, TU Berlin
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

package de.tu_berlin.cit.intercloud.gateway.openstack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.logging.slf4j.config.SLF4JLoggingModule;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.domain.Flavor;
import org.jclouds.openstack.nova.v2_0.domain.Image;
import org.jclouds.openstack.nova.v2_0.features.FlavorApi;
import org.jclouds.openstack.nova.v2_0.features.ImageApi;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.inject.Module;

import de.tu_berlin.cit.intercloud.configuration.OpenStackConfig;
import de.tu_berlin.cit.intercloud.occi.core.OcciXml;
import de.tu_berlin.cit.intercloud.occi.core.incarnation.RepresentationBuilder;
import de.tu_berlin.cit.intercloud.occi.core.xml.representation.CategoryDocument;
import de.tu_berlin.cit.intercloud.occi.infrastructure.ComputeKind;
import de.tu_berlin.cit.intercloud.xmpp.rest.representations.Representation;

/**
 * TODO
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class OpenStackComputeTemplates extends OcciXml {

	@Override
	public List<Representation> getTemplates() {
		// create open stack connection
		OpenStackConfig openStackConf = OpenStackConfig.getInstance();
		Iterable<Module> modules = ImmutableSet
				.<Module> of(new SLF4JLoggingModule());

		String provider = "openstack-nova";
		String identity = openStackConf.getTenantName() + ":"
				+ openStackConf.getUserName();
		String credential = openStackConf.getPassword();

		NovaApi novaApi = ContextBuilder.newBuilder(provider)
				.endpoint(openStackConf.getEndpoint())
				.credentials(identity, credential).modules(modules)
				.buildApi(NovaApi.class);

		Set<String> regions = novaApi.getConfiguredRegions();

		// create empty template list
		List<Representation> list = new ArrayList<Representation>();

		for (String region : regions) {
			// create flavor templates
			FlavorApi flavorApi = novaApi.getFlavorApi(region);

			for (Flavor flavor : flavorApi.listInDetail().concat()) {
				ComputeKind kind = new ComputeKind(flavor.getId());
				kind.cores = flavor.getVcpus();
				kind.memory = (double) flavor.getRam();
				kind.message = "Flavor name " + flavor.getName();
				try {
					// create document
					CategoryDocument doc;
					doc = RepresentationBuilder.buildRepresentation(kind);
					// add template
					list.add(new OcciXml(doc));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// create image templates
			ImageApi api = novaApi.getImageApi(region);

			for (Image image : api.listInDetail().concat()) {
				OpenStackImageMixin imageMixin = new OpenStackImageMixin(
						image.getId());
				imageMixin.name = image.getName();
				imageMixin.mindisk = image.getMinDisk();
				imageMixin.minram = image.getMinRam();
				try {
					// create document
					CategoryDocument doc;
					doc = RepresentationBuilder.buildRepresentation(imageMixin);
					// add template
					list.add(new OcciXml(doc));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// disconnect
		try {
			Closeables.close(novaApi, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return templates
		return list;
	}

	public ComputeKind getComputeKind() {
		ComputeKind kind = new ComputeKind();
		CategoryDocument cat = this.getDocument();
		if (cat == null)
			return kind;

		try {
			kind = RepresentationBuilder.buildRepresentation(cat, kind);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kind;
	}

	public OpenStackComputeMixin getComputeMixin() {
		OpenStackComputeMixin mixin = new OpenStackComputeMixin();
		CategoryDocument cat = this.getDocument();
		if (cat == null)
			return mixin;

		try {
			mixin = RepresentationBuilder.buildRepresentation(cat, mixin);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mixin;
	}

	public OpenStackImageMixin getImageMixin() {
		OpenStackImageMixin mixin = new OpenStackImageMixin();
		CategoryDocument cat = this.getDocument();
		if (cat == null)
			return mixin;

		try {
			mixin = RepresentationBuilder.buildRepresentation(cat, mixin);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mixin;
	}
}
