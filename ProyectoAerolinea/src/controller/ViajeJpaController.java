/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Viaje;
import model.Vuelo;

/**
 *
 * @author maria
 */
public class ViajeJpaController implements Serializable {

    public ViajeJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ProyectoAerolineaPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viaje viaje) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vuelo vueloidVuelo = viaje.getVueloidVuelo();
            if (vueloidVuelo != null) {
                vueloidVuelo = em.getReference(vueloidVuelo.getClass(), vueloidVuelo.getIdVuelo());
                viaje.setVueloidVuelo(vueloidVuelo);
            }
            em.persist(viaje);
            if (vueloidVuelo != null) {
                vueloidVuelo.getViajeList().add(viaje);
                vueloidVuelo = em.merge(vueloidVuelo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findViaje(viaje.getIdViaje()) != null) {
                throw new PreexistingEntityException("Viaje " + viaje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viaje viaje) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaje persistentViaje = em.find(Viaje.class, viaje.getIdViaje());
            Vuelo vueloidVueloOld = persistentViaje.getVueloidVuelo();
            Vuelo vueloidVueloNew = viaje.getVueloidVuelo();
            if (vueloidVueloNew != null) {
                vueloidVueloNew = em.getReference(vueloidVueloNew.getClass(), vueloidVueloNew.getIdVuelo());
                viaje.setVueloidVuelo(vueloidVueloNew);
            }
            viaje = em.merge(viaje);
            if (vueloidVueloOld != null && !vueloidVueloOld.equals(vueloidVueloNew)) {
                vueloidVueloOld.getViajeList().remove(viaje);
                vueloidVueloOld = em.merge(vueloidVueloOld);
            }
            if (vueloidVueloNew != null && !vueloidVueloNew.equals(vueloidVueloOld)) {
                vueloidVueloNew.getViajeList().add(viaje);
                vueloidVueloNew = em.merge(vueloidVueloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viaje.getIdViaje();
                if (findViaje(id) == null) {
                    throw new NonexistentEntityException("The viaje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viaje viaje;
            try {
                viaje = em.getReference(Viaje.class, id);
                viaje.getIdViaje();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viaje with id " + id + " no longer exists.", enfe);
            }
            Vuelo vueloidVuelo = viaje.getVueloidVuelo();
            if (vueloidVuelo != null) {
                vueloidVuelo.getViajeList().remove(viaje);
                vueloidVuelo = em.merge(vueloidVuelo);
            }
            em.remove(viaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viaje> findViajeEntities() {
        return findViajeEntities(true, -1, -1);
    }

    public List<Viaje> findViajeEntities(int maxResults, int firstResult) {
        return findViajeEntities(false, maxResults, firstResult);
    }

    private List<Viaje> findViajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viaje.class));
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

    public Viaje findViaje(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getViajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viaje> rt = cq.from(Viaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
