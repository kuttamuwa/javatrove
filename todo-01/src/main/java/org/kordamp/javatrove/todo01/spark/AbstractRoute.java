/*
 * Copyright 2016-2020 Andres Almiray
 *
 * This file is part of Java Trove Examples
 *
 * Java Trove Examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java Trove Examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java Trove Examples. If not, see <http://www.gnu.org/licenses/>.
 */
package org.kordamp.javatrove.todo01.spark;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.mustache.MustacheTemplateEngine;

import javax.inject.Inject;

/**
 * @author Andres Almiray
 */
public abstract class AbstractRoute implements Route {
    @Inject private ObjectMapper objectMapper;
    @Inject private MustacheTemplateEngine mustacheTemplateEngine;

    @Override
    public final Object handle(Request request, Response response) throws Exception {
        ModelAndView modelAndView = doHandle(request, response);
        response.status(200);
        if (shouldReturnHtml(request)) {
            response.type("text/html");
            return mustacheTemplateEngine.render(modelAndView);
        } else {
            response.type("application/json");
            return objectMapper.writeValueAsString(modelAndView.getModel());
        }
    }

    protected abstract ModelAndView doHandle(Request request, Response response) throws Exception;

    private boolean shouldReturnHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }
}
