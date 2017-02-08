/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jpa.Jpa;

/**
 *
 * @author developer
 */
@Stateless
@Path("services")
public class Services {
    @PersistenceContext(unitName = "ServerFinalPU")
    EntityManager em;
    
    @GET
    public String get() {
        return "Ejecuta solamente GET";
    }

    @GET
    @Path("/all")
//    @Produces("text/plain")
    public List<Jpa> getDatos() {
        return em.createNamedQuery("Jpa.todos").getResultList();
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDatoIdTxt(@PathParam("id") int id) {
        return em.find(Jpa.class, id).getDato();
    }
    
    @GET
    @Path("usr/{usr}")
    public List<Jpa> getDatosUsuario(@PathParam("usr") String usr) {
        return em.createNamedQuery("Jpa.usuario").setParameter("par1", usr).getResultList();
    }
    
    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response newDato(@FormParam("usr") String usr, 
                       @FormParam("tipo") String tipo, 
                       @FormParam("dato") String dato) {
        Jpa d = new Jpa();
        d.setUsuario(usr);
        d.setTipo(tipo);
        d.setDato(dato);
        em.persist(d);
        return Response.status(201).build();
    }
    
    @DELETE
    @Path("/delete/{id}")
    public Response borraDatoId(@PathParam("id") int id) {
        String msg = "Este Id no existe";
        Jpa d = em.find(Jpa.class, id);
        if(d != null) {
            em.remove(d);
            msg = "Se ha borrado correctamente";
        }
        return Response.status(201).entity(msg).build();
    }    
    @PUT
    @Path("/update/{id}/{usr}/{tipo}/{dato}")
    public Response newDato(@PathParam("id") int id, 
            @PathParam("usr") String usr, 
            @PathParam("tipo") String tipo, 
            @PathParam("dato") String dato) {
        String msg = "";
        Jpa d = em.find(Jpa.class, id);
        if(d != null) {
            msg = "Se ha actualizado correctamente";
            d.setUsuario(usr);
            d.setTipo(tipo);
            d.setDato(dato);
            em.merge(d);
        } else {
            msg = "ese id no existe, vamos a crear un nuevo registro";
            d = new Jpa();
            d.setId(id);
            d.setUsuario(usr);
            d.setTipo(tipo);
            d.setDato(dato);
            em.persist(d);
        }
        return Response.status(201).entity(msg).build();
    }    
    
    
}
