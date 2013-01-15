/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
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
package org.exoplatform.commons.search.driver.jcr.connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.commons.api.search.SearchServiceConnector;
import org.exoplatform.commons.api.search.data.SearchResult;
import org.exoplatform.commons.search.driver.jcr.JcrSearch;
import org.exoplatform.commons.search.driver.jcr.JcrSearchResult;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Created by The eXo Platform SAS
 * Author : Canh Pham Van
 *          canhpv@exoplatform.com
 * Jan 3, 2013  
 */
public class JcrEventSearch extends SearchServiceConnector {
  private final static Log LOG = ExoLogger.getLogger(JcrEventSearch.class);
  
  @Override
  public Collection<SearchResult> search(String query, Collection<String> sites, int offset, int limit, String sort, String order) {
    Collection<SearchResult> searchResults = new ArrayList<SearchResult>();
    
    Map<String, Object> parameters = new HashMap<String, Object>(); 
    parameters.put("sites", sites);
    parameters.put("offset", offset);
    parameters.put("limit", limit);
    parameters.put("sort", sort);
    parameters.put("order", order);
    
    parameters.put("repository", "repository");
    parameters.put("workspace", "collaboration");
    parameters.put("from", "exo:calendarEvent");
    parameters.put("where", "exo:eventType='Event'");
    
    Collection<JcrSearchResult> jcrResults = JcrSearch.search(query, parameters);
    CalendarService calendarService = (CalendarService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(CalendarService.class);
    for (JcrSearchResult jcrResult: jcrResults){                
      try {
        String eventId = (String)jcrResult.getProperty("exo:id");        
        String calendarId = (String) jcrResult.getProperty("exo:calendarId");
        
        CalendarEvent calEvent = calendarService.getGroupEvent(eventId);                
        Calendar calendar = calendarService.getGroupCalendar(calendarId);

        SearchResult result = new SearchResult(calendar.getPrivateUrl());
        result.setTitle(calEvent.getSummary());
        result.setExcerpt(calEvent.getDescription()!=null?calEvent.getDescription():calEvent.getSummary());
        StringBuffer buf = new StringBuffer();
        buf.append(calEvent.getEventCategoryName());
        buf.append(" - ");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, MMMMMMMM d, yyyy K:mm a");
        buf.append(sdf.format(calEvent.getFromDateTime()));        
        buf.append(" - ");
        buf.append(calEvent.getLocation()!=null?calEvent.getLocation():calendar.getName());

        result.setDetail(buf.toString());        
        String    avatar = "/csResources/gadgets/events/skin/Events.png";
        result.setImageUrl(avatar);
        searchResults.add(result);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
      } 
    }
    return searchResults;
  }

}