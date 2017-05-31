package com.project.micro.resources;

import com.project.micro.web.InfoBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

//@ApplicationScoped
@Path("/posts")
@Stateless
public class MainEndpoint {

    @Inject
    private InfoBean infoBean;

    @GET
    @Produces("text/plain")
    public Response doGet() {
        return Response.ok("I am a live").build();
    }

    @GET
    @Path("/list")
    @Produces("application/json")
    public Response findAll() {
        String json = infoBean.findAllPosts();
        if (json == null) {
            return Response.noContent().build();
        }
        return Response.ok(json).build();
    }

}
