/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stuff;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PersonWrapper {
    
    private Person person;
    
    
    //constructors
    public PersonWrapper(){};
    
    
    public PersonWrapper(Person person){
        this.person = person;
    };
    
    @XmlElement
    public Person getPerson(){
        return this.person;
    }
    
    public void setPerson(Person person){
        this.person = person;
    }
    
}
