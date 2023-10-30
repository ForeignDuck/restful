/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/GenericResource.java to edit this template
 */
package stuff;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@Path("rest")
public class GenericResource {
    
    // This holds our Data list of Person Objects, Person:(first Name, last Name)
    public static ArrayList<Person> personCollection = new ArrayList<>();
    
    // Finder Method
    private Person findPersonByFullName(String firstName, String lastName) {
        for (Person person : personCollection) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return person;
            }
        }
            return null; // Person not found
    }

    
    //Don't what it is.. just leave it. AUTO-GENERATED
    @Context
    private UriInfo context;
    // Creates a new instance of GenericResource. AUTO-GENERATED
    public GenericResource() {
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_XML,MediaType.TEXT_PLAIN})
    //@Context to access the request headers
    public Response getData(@Context HttpHeaders headers, @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        
        // List of client Media Types found in the header
        List<MediaType> clientMediaTypes = headers.getAcceptableMediaTypes();
        
        // Swtich case structure will return the header with priority
        for (MediaType mediaType : clientMediaTypes) {
        switch (mediaType.toString()) {
            
            case MediaType.APPLICATION_JSON:
            return getJSON(firstName,lastName);
            
            case MediaType.TEXT_XML:
                return getXML(firstName,lastName);
                
            case MediaType.TEXT_PLAIN:
                return getText(firstName,lastName);
            
            default: return getText(firstName,lastName);
            }
        }
        Response.ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
        return builder.build();
    }
    
    
    @GET
    @Path("/text")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getText(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        
        Person foundPerson = findPersonByFullName(firstName,lastName);
        if(foundPerson!=null){
            return Response.status(Response.Status.OK)
                    .entity("Person :" +foundPerson.getFirstName()+" "+foundPerson.getLastName()+" was Found.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }else{
            return Response.status(Response.Status.NOT_FOUND)
            .entity("Person :" +foundPerson.getFirstName()+firstName +" "+lastName+" was Not Found." )
            .type(MediaType.TEXT_PLAIN)
            .build();
        }
    }
    
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJSON(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        
        Person foundPerson = findPersonByFullName(firstName,lastName);
        JSONObject jsonObject = new JSONObject();
        if(foundPerson!=null){
            jsonObject.put("Found",foundPerson.getFirstName()+" "+foundPerson.getLastName());
            return Response.status(Response.Status.FOUND)
                    .entity(jsonObject.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }else{
            jsonObject.put("Not Found",firstName+" "+lastName);
            return Response.status(Response.Status.NOT_FOUND)
            .entity(jsonObject.toString())
            .type(MediaType.APPLICATION_JSON)
            .build();
        }
    }
    
    @GET
    @Path("/xml")
    @Produces(MediaType.TEXT_XML)
    public Response getXML(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        
        Person foundPerson = findPersonByFullName(firstName,lastName);
        if(foundPerson!=null){
            PersonWrapper person = new PersonWrapper(foundPerson);

            try {
                // Create a JAXBContext for the class
                JAXBContext jaxbContext = JAXBContext.newInstance(PersonWrapper.class);

                // Create a Marshaller
                Marshaller marshaller = jaxbContext.createMarshaller();

                StringWriter writer = new StringWriter();

                // 
                marshaller.marshal(person, writer);

                // Return the XML as a Response
                return Response.ok("Person "+ writer.toString() + " was Found.").build();

            } catch (JAXBException e) {
                // Handle JAXBException if XML serialization fails
                return Response.serverError().entity(e.getMessage()).build();}
        }else{
            
            ErrorMessage error = new ErrorMessage("Person: "+firstName+" "+lastName+ " was Not Found.");
            
            try {
                // Create a JAXBContext for the class
                JAXBContext jaxbContext = JAXBContext.newInstance(ErrorMessage.class);

                // Create a Marshaller
                Marshaller marshaller = jaxbContext.createMarshaller();

                StringWriter writer = new StringWriter();

                marshaller.marshal(error, writer);

                // Return the XML as a Response
                return Response.ok(writer.toString()).build();

            } catch (JAXBException e) {
                // Handle JAXBException if XML serialization fails
                return Response.serverError().entity(e.getMessage()).build();}
                    
        }
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_PLAIN)
    public void putText(String content) {
    }
    
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response addPerson(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        Person foundPerson = findPersonByFullName(firstName, lastName);
        
        if (foundPerson == null) {
            // Add the new person to the collection
            Person newPerson = new Person(firstName, lastName);
            personCollection.add(newPerson);

            return Response.status(Response.Status.OK)
                    .entity("Person " + firstName + " " + lastName + " was successfully added."+"\nPerson Collection Size: "+Integer.toString(personCollection.size()))
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Person " + firstName + " " + lastName + " already exists."+"\nPerson Collection Size: "+Integer.toString(personCollection.size()))
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePerson(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
    Person foundPerson = findPersonByFullName(firstName, lastName);
    
    if (foundPerson != null) {
        // Remove the person from the collection
        personCollection.remove(foundPerson);

        return Response.status(Response.Status.OK)
                .entity("Person " + firstName + " " + lastName + " was successfully deleted." + "\nPerson Collection Size: " + Integer.toString(personCollection.size()))
                .type(MediaType.TEXT_PLAIN)
                .build();
    } else {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Person " + firstName + " " + lastName + " does not exist." + "\nPerson Collection Size: " + Integer.toString(personCollection.size()))
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
}
