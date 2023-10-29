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
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import org.json.JSONArray;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@Path("rest")
public class restService {
    
    private static ArrayList<String> stringCollection = new ArrayList<>();
    private ArrayList<Person> personCollection = new ArrayList<>();
    
    // iterate through the array list and find the right ID
    // make the same method for findFirstName & findLastName..
    
    private Person findPersonById(int ID) {
    for (Person person : personCollection) {
        if (person.getID() == ID) {
            return person;
        }
    }
        return null;
    }
    
    
    @Context
    private UriInfo context;

    public restService() {
        
    }
    
    // ***********************************************************************
    
     // Default GET method, we don't specify a path for it. 
    /*
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
    */
    
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_XML,MediaType.TEXT_PLAIN})
    //@Context to access the request headers
    public Response getData(@Context HttpHeaders headers) {
        
        // Like it says in the assingment.. 
        List<MediaType> clientMediaTypes = headers.getAcceptableMediaTypes();
        
        
        for (MediaType mediaType : clientMediaTypes) {
        switch (mediaType.toString()) {
            
            case MediaType.APPLICATION_JSON:
            return getJSON();
            
            case MediaType.TEXT_XML:
                return getXML();
                
            case MediaType.TEXT_PLAIN:
                return getText();
            
            default: return getText();
            }
        }
        Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
        return builder.build();
    }
    
    
    @GET
    @Path("json")
    //@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJSON() {
          JSONArray jsonArray = new JSONArray();
          jsonArray.put(stringCollection);
         try{
            return Response
                    .status(Response.Status.OK)
                    .entity(jsonArray.toString())
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
    @Produces(MediaType.TEXT_XML)
    //public ArrayList<String> getXml() {
    public Response getXML() {   
        try{
            return Response
                    .status(Response.Status.OK)
                    .entity(personCollection)
                    .type(MediaType.TEXT_XML)
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
