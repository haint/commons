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
package org.exoplatform.commons.search.driver.jcr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.exoplatform.calendar.service.Calendar;
import org.exoplatform.calendar.service.CalendarEvent;
import org.exoplatform.calendar.service.CalendarService;
import org.exoplatform.commons.search.api.search.Search;
import org.exoplatform.commons.search.api.search.data.SearchResult;
import org.exoplatform.commons.search.service.UnifiedSearch;
import org.exoplatform.container.ExoContainerContext;

/**
 * Created by The eXo Platform SAS
 * Author : Canh Pham Van
 *          canhpv@exoplatform.com
 * Jan 3, 2013  
 */
public class JcrTaskSearch implements Search{

  @Override
  public Collection<SearchResult> search(String query, Collection<String> sites, Collection<String> types, int offset, int limit, String sort, String order) {
    Collection<SearchResult> searchResults = new ArrayList<SearchResult>();
    
    Map<String, Object> parameters = new HashMap<String, Object>(); 
    parameters.put("sites", sites);
    parameters.put("types", types);
    parameters.put("offset", offset);
    parameters.put("limit", limit);
    parameters.put("sort", sort);
    parameters.put("order", order);
    
    parameters.put("type", UnifiedSearch.TASK);
    parameters.put("repository", "repository");
    parameters.put("workspace", "collaboration");
    parameters.put("from", "exo:calendarEvent");
    parameters.put("where", "exo:eventType='Task'");
    
    Collection<JcrSearchResult> jcrResults = JcrSearchService.search(query, parameters);
    CalendarService calendarService = (CalendarService) ExoContainerContext.getCurrentContainer().getComponentInstanceOfType(CalendarService.class);
    for (JcrSearchResult jcrResult: jcrResults){                
      try {
        String taskId = (String)jcrResult.getProperty("exo:id");        
        String calendarId = (String) jcrResult.getProperty("exo:calendarId");
        
        CalendarEvent calTask = calendarService.getGroupEvent(taskId);                
        Calendar calendar = calendarService.getGroupCalendar(calendarId);

        SearchResult result = new SearchResult(UnifiedSearch.TASK, calendar.getPublicUrl());
        result.setTitle(calTask.getSummary());
        result.setExcerpt(calTask.getDescription()!=null?calTask.getDescription():calTask.getSummary());
        StringBuffer buf = new StringBuffer();
        buf.append(calTask.getEventCategoryName());
        buf.append(" - ");
        SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, MMMMMMMM d, yyyy K:mm a");
        buf.append(sdf.format(calTask.getFromDateTime()));        
        buf.append(" - ");
        buf.append(calTask.getLocation()!=null?calTask.getLocation():calendar.getName());

        result.setDetail(buf.toString());        
        String    avatar = "/csResources/gadgets/tasks/skin/Tasks.png";
        result.setAvatar(avatar);
        searchResults.add(result);
      } catch (Exception e) {
        //e.printStackTrace();
        continue;
      } 
    }
    return searchResults;
  }

}