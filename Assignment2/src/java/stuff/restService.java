/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package stuff;

import java.io.StringWriter;
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
    @Produces(MediaType.TEXT_XML)
    public Response getXML() {
        
        //Person test = new Person(1,"bob","ross");
        StringListWrapper wrapper = new StringListWrapper(stringCollection);
        
        try {
            // Create a JAXBContext for the Message class
            JAXBContext jaxbContext = JAXBContext.newInstance(StringListWrapper.class);

            // Create a Marshaller
            Marshaller marshaller = jaxbContext.createMarshaller();

            StringWriter writer = new StringWriter();
            
            // Marshal the wrapper object, which includes the string list
            marshaller.marshal(wrapper, writer);
            
            // Return the XML as a Response
            return Response.ok(writer.toString()).build();
            
        } catch (JAXBException e) {
            // Handle JAXBException if XML serialization fails
            return Response.serverError().entity(e.getMessage()).build();        
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
