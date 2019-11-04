/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Avion;
import model.Ciudad;
import model.Viaje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ParameterExpression;
import model.Reserva;
import model.Vuelo;

/**
 *
 * @author maria
 */
public class VueloJpaController implements Serializable {

    public VueloJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ProyectoAerolineaPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vuelo vuelo) throws PreexistingEntityException, Exception {
        if (vuelo.getViajeList() == null) {
            vuelo.setViajeList(new ArrayList<Viaje>());
        }
        if (vuelo.getReservaList() == null) {
            vuelo.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Avion avionidAvion = vuelo.getAvionidAvion();
            if (avionidAvion != null) {
                avionidAvion = em.getReference(avionidAvion.getClass(), avionidAvion.getIdAvion());
                vuelo.setAvionidAvion(avionidAvion);
            }
            Ciudad ciudadnombre = vuelo.getCiudadnombre();
            if (ciudadnombre != null) {
                ciudadnombre = em.getReference(ciudadnombre.getClass(), ciudadnombre.getNombre());
                vuelo.setCiudadnombre(ciudadnombre);
            }
            List<Viaje> attachedViajeList = new ArrayList<Viaje>();
            for (Viaje viajeListViajeToAttach : vuelo.getViajeList()) {
                viajeListViajeToAttach = em.getReference(viajeListViajeToAttach.getClass(), viajeListViajeToAttach.getIdViaje());
                attachedViajeList.add(viajeListViajeToAttach);
            }
            vuelo.setViajeList(attachedViajeList);
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : vuelo.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            vuelo.setReservaList(attachedReservaList);
            em.persist(vuelo);
            if (avionidAvion != null) {
                avionidAvion.getVueloList().add(vuelo);
                avionidAvion = em.merge(avionidAvion);
            }
            if (ciudadnombre != null) {
                ciudadnombre.getVueloList().add(vuelo);
                ciudadnombre = em.merge(ciudadnombre);
            }
            for (Viaje viajeListViaje : vuelo.getViajeList()) {
                Vuelo oldVueloidVueloOfViajeListViaje = viajeListViaje.getVueloidVuelo();
                viajeListViaje.setVueloidVuelo(vuelo);
                viajeListViaje = em.merge(viajeListViaje);
                if (oldVueloidVueloOfViajeListViaje != null) {
                    oldVueloidVueloOfViajeListViaje.getViajeList().remove(viajeListViaje);
                    oldVueloidVueloOfViajeListViaje = em.merge(oldVueloidVueloOfViajeListViaje);
                }
            }
            for (Reserva reservaListReserva : vuelo.getReservaList()) {
                Vuelo oldVueloidVueloOfReservaListReserva = reservaListReserva.getVueloidVuelo();
                reservaListReserva.setVueloidVuelo(vuelo);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldVueloidVueloOfReservaListReserva != null) {
                    oldVueloidVueloOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldVueloidVueloOfReservaListReserva = em.merge(oldVueloidVueloOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVuelo(vuelo.getIdVuelo()) != null) {
                throw new PreexistingEntityException("Vuelo " + vuelo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vuelo vuelo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vuelo persistentVuelo = em.find(Vuelo.class, vuelo.getIdVuelo());
            Avion avionidAvionOld = persistentVuelo.getAvionidAvion();
            Avion avionidAvionNew = vuelo.getAvionidAvion();
            Ciudad ciudadnombreOld = persistentVuelo.getCiudadnombre();
            Ciudad ciudadnombreNew = vuelo.getCiudadnombre();
            List<Viaje> viajeListOld = persistentVuelo.getViajeList();
            List<Viaje> viajeListNew = vuelo.getViajeList();
            List<Reserva> reservaListOld = persistentVuelo.getReservaList();
            List<Reserva> reservaListNew = vuelo.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Viaje viajeListOldViaje : viajeListOld) {
                if (!viajeListNew.contains(viajeListOldViaje)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Viaje " + viajeListOldViaje + " since its vueloidVuelo field is not nullable.");
                }
            }
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its vueloidVuelo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (avionidAvionNew != null) {
                avionidAvionNew = em.getReference(avionidAvionNew.getClass(), avionidAvionNew.getIdAvion());
                vuelo.setAvionidAvion(avionidAvionNew);
            }
            if (ciudadnombreNew != null) {
                ciudadnombreNew = em.getReference(ciudadnombreNew.getClass(), ciudadnombreNew.getNombre());
                vuelo.setCiudadnombre(ciudadnombreNew);
            }
            List<Viaje> attachedViajeListNew = new ArrayList<Viaje>();
            for (Viaje viajeListNewViajeToAttach : viajeListNew) {
                viajeListNewViajeToAttach = em.getReference(viajeListNewViajeToAttach.getClass(), viajeListNewViajeToAttach.getIdViaje());
                attachedViajeListNew.add(viajeListNewViajeToAttach);
            }
            viajeListNew = attachedViajeListNew;
            vuelo.setViajeList(viajeListNew);
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            vuelo.setReservaList(reservaListNew);
            vuelo = em.merge(vuelo);
            if (avionidAvionOld != null && !avionidAvionOld.equals(avionidAvionNew)) {
                avionidAvionOld.getVueloList().remove(vuelo);
                avionidAvionOld = em.merge(avionidAvionOld);
            }
            if (avionidAvionNew != null && !avionidAvionNew.equals(avionidAvionOld)) {
                avionidAvionNew.getVueloList().add(vuelo);
                avionidAvionNew = em.merge(avionidAvionNew);
            }
            if (ciudadnombreOld != null && !ciudadnombreOld.equals(ciudadnombreNew)) {
                ciudadnombreOld.getVueloList().remove(vuelo);
                ciudadnombreOld = em.merge(ciudadnombreOld);
            }
            if (ciudadnombreNew != null && !ciudadnombreNew.equals(ciudadnombreOld)) {
                ciudadnombreNew.getVueloList().add(vuelo);
                ciudadnombreNew = em.merge(ciudadnombreNew);
            }
            for (Viaje viajeListNewViaje : viajeListNew) {
                if (!viajeListOld.contains(viajeListNewViaje)) {
                    Vuelo oldVueloidVueloOfViajeListNewViaje = viajeListNewViaje.getVueloidVuelo();
                    viajeListNewViaje.setVueloidVuelo(vuelo);
                    viajeListNewViaje = em.merge(viajeListNewViaje);
                    if (oldVueloidVueloOfViajeListNewViaje != null && !oldVueloidVueloOfViajeListNewViaje.equals(vuelo)) {
                        oldVueloidVueloOfViajeListNewViaje.getViajeList().remove(viajeListNewViaje);
                        oldVueloidVueloOfViajeListNewViaje = em.merge(oldVueloidVueloOfViajeListNewViaje);
                    }
                }
            }
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Vuelo oldVueloidVueloOfReservaListNewReserva = reservaListNewReserva.getVueloidVuelo();
                    reservaListNewReserva.setVueloidVuelo(vuelo);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldVueloidVueloOfReservaListNewReserva != null && !oldVueloidVueloOfReservaListNewReserva.equals(vuelo)) {
                        oldVueloidVueloOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldVueloidVueloOfReservaListNewReserva = em.merge(oldVueloidVueloOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = vuelo.getIdVuelo();
                if (findVuelo(id) == null) {
                    throw new NonexistentEntityException("The vuelo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vuelo vuelo;
            try {
                vuelo = em.getReference(Vuelo.class, id);
                vuelo.getIdVuelo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vuelo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Viaje> viajeListOrphanCheck = vuelo.getViajeList();
            for (Viaje viajeListOrphanCheckViaje : viajeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vuelo (" + vuelo + ") cannot be destroyed since the Viaje " + viajeListOrphanCheckViaje + " in its viajeList field has a non-nullable vueloidVuelo field.");
            }
            List<Reserva> reservaListOrphanCheck = vuelo.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Vuelo (" + vuelo + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable vueloidVuelo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Avion avionidAvion = vuelo.getAvionidAvion();
            if (avionidAvion != null) {
                avionidAvion.getVueloList().remove(vuelo);
                avionidAvion = em.merge(avionidAvion);
            }
            Ciudad ciudadnombre = vuelo.getCiudadnombre();
            if (ciudadnombre != null) {
                ciudadnombre.getVueloList().remove(vuelo);
                ciudadnombre = em.merge(ciudadnombre);
            }
            em.remove(vuelo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vuelo> findVueloEntities() {
        return findVueloEntities(true, -1, -1);
    }

    public List<Vuelo> findVueloEntities(int maxResults, int firstResult) {
        return findVueloEntities(false, maxResults, firstResult);
    }

    private List<Vuelo> findVueloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vuelo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Vuelo findVuelo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vuelo.class, id);
        } finally {
            em.close();
        }
    }

    public int getVueloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vuelo> rt = cq.from(Vuelo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<Vuelo> findVueloEntetisFilter(String nombre){
        
        EntityManager em = getEntityManager();
        
        try{
            //contruccion de la sentencia
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Vuelo> tabla = cq.from(Vuelo.class);
            cq.select(tabla);
            ParameterExpression<String> pNombre = cb.parameter(String.class,"pNombre");
            cq.where(cb.like(tabla.get("cuidadorigen"), pNombre));
            //
            
            //Ejecucion de la sentencia
            
            TypedQuery<Vuelo> typedQuery = em.createQuery(cq);
            typedQuery.setParameter("pNombre", "%"+ nombre+ "%");
            
            return typedQuery.getResultList();
            
        }finally{
            em.close();
        }
    }
    
    public List<Vuelo> findVueloEntetisFilter2(String nombre){
        
        EntityManager em = getEntityManager();
        
        try{
            //contruccion de la sentencia
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Vuelo> tabla = cq.from(Vuelo.class);
            cq.select(tabla);
            ParameterExpression<String> pNombre = cb.parameter(String.class,"pNombre");
            cq.where(cb.like(tabla.get("ciudaddestino"), pNombre));
            //
            
            //Ejecucion de la sentencia
            
            TypedQuery<Vuelo> typedQuery = em.createQuery(cq);
            typedQuery.setParameter("pNombre", "%"+ nombre+ "%");
            
            return typedQuery.getResultList();
            
        }finally{
            em.close();
        }
    }
}
