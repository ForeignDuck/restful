/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stuff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @Note: 
 * I've tried just using plain @XmlRootElement
 * and @XmlElements (on the Getters).
 *
 */
@XmlRootElement(name ="Person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    
    private String firstName;
    private String lastName;
    
    
    //Constuctor
    public Person() {}
    public Person(String firstName,String lastName){
        
        this.firstName =  firstName;
        this.lastName = lastName;
    }
    
    //Getters
    
    public String getLastName(){return this.lastName;}
    public String getFirstName(){return this.firstName;}
    
    //Setters
    public void setLastName(String newLastName){this.lastName = newLastName;}
    public void setFirstName(String newFirstName){this.firstName =  newFirstName;}
}
