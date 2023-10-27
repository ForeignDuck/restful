/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package stuff;

import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("rest")
public class restService {
    
    // in-memory arrayList that holds all movies;
    
    private static ArrayList<String> stringCollection = new ArrayList<>();
    
    // date object used for updating/creating new movies
    Date lastModified = new Date();
    

    @Context
    private UriInfo context;

    public restService() {
        
    }
    
    // ***********************************************************************
    
     // Default GET method, we don't specify a path for it. 
    @GET
    // The method can produce the 3 types of formats, JSON, XML and plain text.
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    // Look for the Accept Header parameter
    public Response getDefault(@HeaderParam("Accept") String acceptHeader) {
        
        // The client specifies the format they desire in their accept header
        // Consider the case when the Accept header is not provided, so it is null.
        // Therefore this is the default case
        if (acceptHeader == null) {
        return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection.toString())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // If more than one is specified, we answer following the priority JSON > XML > Plain Text
        } else if (acceptHeader.contains(MediaType.APPLICATION_JSON)) {
            // Response.ok(...).build() builds a new instance of ResponseBuilder with a 200 OK status
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            
        } else if (acceptHeader.contains(MediaType.APPLICATION_XML)) {
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection)
                    .type(MediaType.APPLICATION_XML)
                    .build();
            
        } else if (acceptHeader.contains(MediaType.TEXT_PLAIN)) {
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection.toString())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
            //stringCollection.toString()
            
        } else {
            // If no matched format was found,
            // Response.status(...).build() builds a new instance of ResponseBuilder with a 400 BAD REQUEST status
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("No matched format was found")
                    .build();
        }
    }
    
    @GET
    @Path("json")
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
         try{
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch(Exception e){
             //ArrayList<String> message = new ArrayList<>();
            String message = ("{\"error\": \"" + e.getMessage() + "\"}");
            return Response
                    .status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(message)
                    .build();
        }
    }
    
    @GET
    @Path("xml")
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_XML)
    //public ArrayList<String> getXml() {
    public Response getXml() {   
        try{
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection)
                    .type(MediaType.APPLICATION_XML)
                    .build();
        } catch(Exception e){
             //ArrayList<String> message = new ArrayList<>();
            String message = ("<error>" + e.getMessage() + "</error>");
            return Response
                    .status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(message)
                    .build();
        }
    }

    @GET
    @Path("text")
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getText() {
        try{
            return Response
                    .status(Response.Status.OK)
                    .entity(stringCollection.toString())
                    .type(MediaType.TEXT_PLAIN)
                    .build();
            
        } catch(Exception e){
             //ArrayList<String> message = new ArrayList<>();
            String message = ("Error: " + e.getMessage());
            return Response
                    .status(Response.Status.METHOD_NOT_ALLOWED)
                    .entity(message)
                    .build();
        }    
    }
    
    @POST
    // No specific path, so it falls under "/"
    // @Path("/post")
    public Response addString(String newString) {
        
        if (stringCollection.contains(newString)) {
            throw new BadRequestException("String is already inside the collection");
        }
        stringCollection.add(newString);
        return Response.ok("String successfully added").build();
    }
    
    @DELETE
    // No specific path, so it falls under "/"
    // @Path("/delete")
    public Response deleteString(String stringDelete) {
        
        if (!stringCollection.remove(stringDelete)) {
            throw new BadRequestException("String was not found");
        }
        return Response.ok("String was deleted").build();
    }
    
    // ***********************************************************************
    
}
