/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.commons.search.driver.solr;


import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.exoplatform.commons.search.SearchService;
import org.exoplatform.commons.search.SearchType;
import org.exoplatform.commons.search.sample.BaseTest;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : Tung Vu Minh
 *          tungvm@exoplatform.com
 * Nov 21, 2012  
 */
public class SolrTest extends BaseTest  {
  private final static Log LOG = ExoLogger.getLogger(SolrTest.class);
  private static final String SOLR_HOME = "src/test/resources/solr";
  SolrServer server;

  @Override
  public void setUp() throws Exception {
    System.setProperty("solr.solr.home", SOLR_HOME);
    CoreContainer.Initializer initializer = new CoreContainer.Initializer();
    CoreContainer coreContainer = initializer.initialize();
    server = new EmbeddedSolrServer(coreContainer, "");
    //server = new HttpSolrServer("http://localhost:8983/solr");
    
    indexingService = new SolrIndexingService(server);
    SolrSearchService.setServer(server);
    SearchService.register(new SearchType("user", "User", null, SolrGenericSearch.class));
    SearchService.register(new SearchType("topic", "Forum topic", null, SolrGenericSearch.class));
    super.setUp();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    FileUtils.deleteDirectory(new File(SOLR_HOME + "/data")); // cleanup local Solr server's generated data
  }

  public void testSearch() throws Exception {
    categorizedSearch("\"anthony cena\" mary");
    search("creationAuthor_t:john"); //TODO: hide the underscore
  }
}