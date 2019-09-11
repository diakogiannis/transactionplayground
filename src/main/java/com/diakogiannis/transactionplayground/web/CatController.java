package com.diakogiannis.transactionplayground.web;

import com.diakogiannis.transactionplayground.model.entity.Cat;
import com.diakogiannis.transactionplayground.services.cdi.CatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Stateful
@Path("/cats")
public class CatController {
    Logger log = LoggerFactory.getLogger(CatController.class);

    @EJB
    CatService catService;


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response allCats(){
        return Response.ok().entity(catService.findAll()).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAllCats(){
        catService.saveMultipleRandomCats(false);
        return Response.ok().build();
    }

    @GET
    @Path("/all-exception")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAllCatsException(){
        try {
            catService.saveMultipleRandomCats(true);
        }catch (RuntimeException e){
            log.error("Oups...");
        }
        return Response.ok().build();    }

    @GET
    @Path("/all-parallel")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAllCatsParallel(){
        catService.saveMultipleRandomCatsInParallel((false));
        return Response.ok().build();    }

    @GET
    @Path("/all-parallel-exception")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAllCatsParallelException(){
        try{
        catService.saveMultipleRandomCatsInParallel((true));
        }catch (RuntimeException e){
            log.error("Oups...");
        }

        return Response.ok().build();
    }


    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response cleanup(){
        catService.deleteAllCats();
        return Response.ok().build();
    }

}
