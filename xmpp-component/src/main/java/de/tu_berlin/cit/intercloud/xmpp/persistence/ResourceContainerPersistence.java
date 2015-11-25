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

package de.tu_berlin.cit.intercloud.xmpp.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class allows to persist representations in relation to its path.
 * 
 * @author Alexander Stanik <alexander.stanik@tu-berlin.de>
 */
public class ResourceContainerPersistence {

	private final static Logger logger = LoggerFactory
			.getLogger(ResourceContainerPersistence.class);

	private static final String persistentUnit = "intercloud";
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistentUnit);


	public static void persist(String path, String representation) {
		logger.info("Persist representation for = " + path);
		// Create new EntityManager
		EntityManager em = emf.createEntityManager();
		// Read the existing entries and write to console
        Query q = em.createQuery("SELECT r FROM RepresentationPersistence r");
        @SuppressWarnings("unchecked")
		List<RepresentationPersistence> list = q.getResultList();
        for (RepresentationPersistence rep : list) {
             logger.info("RepresentationPersistence: " + rep.getPath());
        }
        logger.info("Size: " + list.size());

        // Create new user
        em.getTransaction().begin();
        RepresentationPersistence rep = new RepresentationPersistence();
        rep.setPath(path);
        rep.setRepresentation(representation);
        em.persist(rep);
        em.getTransaction().commit();

        em.close();
	}
/*
    public String validate() {
        String flag="failure";
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
          EntityManager em = factory.createEntityManager();
          Query q = em.createQuery("SELECT u FROM User u WHERE u.Login = :login AND u.Password = :pass");
         q.setParameter("login", userName);
         q.setParameter("pass", password);
         try{
             User user = (User) q.getSingleResult();
           if (userName.equalsIgnoreCase(user.Login)&&password.equals(user.Password)) {
              flag="success";
           }
         }catch(Exception e){      
             return null;
         }

        return flag;
   }
*/
}