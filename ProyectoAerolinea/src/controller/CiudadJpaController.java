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
import model.Pais;
import model.Vuelo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Ciudad;

/**
 *
 * @author maria
 */
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) throws PreexistingEntityException, Exception {
        if (ciudad.getVueloList() == null) {
            ciudad.setVueloList(new ArrayList<Vuelo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais paisnombre = ciudad.getPaisnombre();
            if (paisnombre != null) {
                paisnombre = em.getReference(paisnombre.getClass(), paisnombre.getNombre());
                ciudad.setPaisnombre(paisnombre);
            }
            List<Vuelo> attachedVueloList = new ArrayList<Vuelo>();
            for (Vuelo vueloListVueloToAttach : ciudad.getVueloList()) {
                vueloListVueloToAttach = em.getReference(vueloListVueloToAttach.getClass(), vueloListVueloToAttach.getIdVuelo());
                attachedVueloList.add(vueloListVueloToAttach);
            }
            ciudad.setVueloList(attachedVueloList);
            em.persist(ciudad);
            if (paisnombre != null) {
                paisnombre.getCiudadList().add(ciudad);
                paisnombre = em.merge(paisnombre);
            }
            for (Vuelo vueloListVuelo : ciudad.getVueloList()) {
                Ciudad oldCiudadnombreOfVueloListVuelo = vueloListVuelo.getCiudadnombre();
                vueloListVuelo.setCiudadnombre(ciudad);
                vueloListVuelo = em.merge(vueloListVuelo);
                if (oldCiudadnombreOfVueloListVuelo != null) {
                    oldCiudadnombreOfVueloListVuelo.getVueloList().remove(vueloListVuelo);
                    oldCiudadnombreOfVueloListVuelo = em.merge(oldCiudadnombreOfVueloListVuelo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCiudad(ciudad.getNombre()) != null) {
                throw new PreexistingEntityException("Ciudad " + ciudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getNombre());
            Pais paisnombreOld = persistentCiudad.getPaisnombre();
            Pais paisnombreNew = ciudad.getPaisnombre();
            List<Vuelo> vueloListOld = persistentCiudad.getVueloList();
            List<Vuelo> vueloListNew = ciudad.getVueloList();
            List<String> illegalOrphanMessages = null;
            for (Vuelo vueloListOldVuelo : vueloListOld) {
                if (!vueloListNew.contains(vueloListOldVuelo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Vuelo " + vueloListOldVuelo + " since its ciudadnombre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (paisnombreNew != null) {
                paisnombreNew = em.getReference(paisnombreNew.getClass(), paisnombreNew.getNombre());
                ciudad.setPaisnombre(paisnombreNew);
            }
            List<Vuelo> attachedVueloListNew = new ArrayList<Vuelo>();
            for (Vuelo vueloListNewVueloToAttach : vueloListNew) {
                vueloListNewVueloToAttach = em.getReference(vueloListNewVueloToAttach.getClass(), vueloListNewVueloToAttach.getIdVuelo());
                attachedVueloListNew.add(vueloListNewVueloToAttach);
            }
            vueloListNew = attachedVueloListNew;
            ciudad.setVueloList(vueloListNew);
            ciudad = em.merge(ciudad);
            if (paisnombreOld != null && !paisnombreOld.equals(paisnombreNew)) {
                paisnombreOld.getCiudadList().remove(ciudad);
                paisnombreOld = em.merge(paisnombreOld);
            }
            if (paisnombreNew != null && !paisnombreNew.equals(paisnombreOld)) {
                paisnombreNew.getCiudadList().add(ciudad);
                paisnombreNew = em.merge(paisnombreNew);
            }
            for (Vuelo vueloListNewVuelo : vueloListNew) {
                if (!vueloListOld.contains(vueloListNewVuelo)) {
                    Ciudad oldCiudadnombreOfVueloListNewVuelo = vueloListNewVuelo.getCiudadnombre();
                    vueloListNewVuelo.setCiudadnombre(ciudad);
                    vueloListNewVuelo = em.merge(vueloListNewVuelo);
                    if (oldCiudadnombreOfVueloListNewVuelo != null && !oldCiudadnombreOfVueloListNewVuelo.equals(ciudad)) {
                        oldCiudadnombreOfVueloListNewVuelo.getVueloList().remove(vueloListNewVuelo);
                        oldCiudadnombreOfVueloListNewVuelo = em.merge(oldCiudadnombreOfVueloListNewVuelo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = ciudad.getNombre();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Vuelo> vueloListOrphanCheck = ciudad.getVueloList();
            for (Vuelo vueloListOrphanCheckVuelo : vueloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ciudad (" + ciudad + ") cannot be destroyed since the Vuelo " + vueloListOrphanCheckVuelo + " in its vueloList field has a non-nullable ciudadnombre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Pais paisnombre = ciudad.getPaisnombre();
            if (paisnombre != null) {
                paisnombre.getCiudadList().remove(ciudad);
                paisnombre = em.merge(paisnombre);
            }
            em.remove(ciudad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
