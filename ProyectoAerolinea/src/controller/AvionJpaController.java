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
import model.Tipoavion;
import model.Vuelo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Avion;

/**
 *
 * @author maria
 */
public class AvionJpaController implements Serializable {

    public AvionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Avion avion) throws PreexistingEntityException, Exception {
        if (avion.getVueloList() == null) {
            avion.setVueloList(new ArrayList<Vuelo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoavion tipoAvionidtipoAvion = avion.getTipoAvionidtipoAvion();
            if (tipoAvionidtipoAvion != null) {
                tipoAvionidtipoAvion = em.getReference(tipoAvionidtipoAvion.getClass(), tipoAvionidtipoAvion.getIdtipoAvion());
                avion.setTipoAvionidtipoAvion(tipoAvionidtipoAvion);
            }
            List<Vuelo> attachedVueloList = new ArrayList<Vuelo>();
            for (Vuelo vueloListVueloToAttach : avion.getVueloList()) {
                vueloListVueloToAttach = em.getReference(vueloListVueloToAttach.getClass(), vueloListVueloToAttach.getIdVuelo());
                attachedVueloList.add(vueloListVueloToAttach);
            }
            avion.setVueloList(attachedVueloList);
            em.persist(avion);
            if (tipoAvionidtipoAvion != null) {
                tipoAvionidtipoAvion.getAvionList().add(avion);
                tipoAvionidtipoAvion = em.merge(tipoAvionidtipoAvion);
            }
            for (Vuelo vueloListVuelo : avion.getVueloList()) {
                Avion oldAvionidAvionOfVueloListVuelo = vueloListVuelo.getAvionidAvion();
                vueloListVuelo.setAvionidAvion(avion);
                vueloListVuelo = em.merge(vueloListVuelo);
                if (oldAvionidAvionOfVueloListVuelo != null) {
                    oldAvionidAvionOfVueloListVuelo.getVueloList().remove(vueloListVuelo);
                    oldAvionidAvionOfVueloListVuelo = em.merge(oldAvionidAvionOfVueloListVuelo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAvion(avion.getIdAvion()) != null) {
                throw new PreexistingEntityException("Avion " + avion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Avion avion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Avion persistentAvion = em.find(Avion.class, avion.getIdAvion());
            Tipoavion tipoAvionidtipoAvionOld = persistentAvion.getTipoAvionidtipoAvion();
            Tipoavion tipoAvionidtipoAvionNew = avion.getTipoAvionidtipoAvion();
            List<Vuelo> vueloListOld = persistentAvion.getVueloList();
            List<Vuelo> vueloListNew = avion.getVueloList();
            List<String> illegalOrphanMessages = null;
            for (Vuelo vueloListOldVuelo : vueloListOld) {
                if (!vueloListNew.contains(vueloListOldVuelo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Vuelo " + vueloListOldVuelo + " since its avionidAvion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoAvionidtipoAvionNew != null) {
                tipoAvionidtipoAvionNew = em.getReference(tipoAvionidtipoAvionNew.getClass(), tipoAvionidtipoAvionNew.getIdtipoAvion());
                avion.setTipoAvionidtipoAvion(tipoAvionidtipoAvionNew);
            }
            List<Vuelo> attachedVueloListNew = new ArrayList<Vuelo>();
            for (Vuelo vueloListNewVueloToAttach : vueloListNew) {
                vueloListNewVueloToAttach = em.getReference(vueloListNewVueloToAttach.getClass(), vueloListNewVueloToAttach.getIdVuelo());
                attachedVueloListNew.add(vueloListNewVueloToAttach);
            }
            vueloListNew = attachedVueloListNew;
            avion.setVueloList(vueloListNew);
            avion = em.merge(avion);
            if (tipoAvionidtipoAvionOld != null && !tipoAvionidtipoAvionOld.equals(tipoAvionidtipoAvionNew)) {
                tipoAvionidtipoAvionOld.getAvionList().remove(avion);
                tipoAvionidtipoAvionOld = em.merge(tipoAvionidtipoAvionOld);
            }
            if (tipoAvionidtipoAvionNew != null && !tipoAvionidtipoAvionNew.equals(tipoAvionidtipoAvionOld)) {
                tipoAvionidtipoAvionNew.getAvionList().add(avion);
                tipoAvionidtipoAvionNew = em.merge(tipoAvionidtipoAvionNew);
            }
            for (Vuelo vueloListNewVuelo : vueloListNew) {
                if (!vueloListOld.contains(vueloListNewVuelo)) {
                    Avion oldAvionidAvionOfVueloListNewVuelo = vueloListNewVuelo.getAvionidAvion();
                    vueloListNewVuelo.setAvionidAvion(avion);
                    vueloListNewVuelo = em.merge(vueloListNewVuelo);
                    if (oldAvionidAvionOfVueloListNewVuelo != null && !oldAvionidAvionOfVueloListNewVuelo.equals(avion)) {
                        oldAvionidAvionOfVueloListNewVuelo.getVueloList().remove(vueloListNewVuelo);
                        oldAvionidAvionOfVueloListNewVuelo = em.merge(oldAvionidAvionOfVueloListNewVuelo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = avion.getIdAvion();
                if (findAvion(id) == null) {
                    throw new NonexistentEntityException("The avion with id " + id + " no longer exists.");
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
            Avion avion;
            try {
                avion = em.getReference(Avion.class, id);
                avion.getIdAvion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The avion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Vuelo> vueloListOrphanCheck = avion.getVueloList();
            for (Vuelo vueloListOrphanCheckVuelo : vueloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Avion (" + avion + ") cannot be destroyed since the Vuelo " + vueloListOrphanCheckVuelo + " in its vueloList field has a non-nullable avionidAvion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipoavion tipoAvionidtipoAvion = avion.getTipoAvionidtipoAvion();
            if (tipoAvionidtipoAvion != null) {
                tipoAvionidtipoAvion.getAvionList().remove(avion);
                tipoAvionidtipoAvion = em.merge(tipoAvionidtipoAvion);
            }
            em.remove(avion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Avion> findAvionEntities() {
        return findAvionEntities(true, -1, -1);
    }

    public List<Avion> findAvionEntities(int maxResults, int firstResult) {
        return findAvionEntities(false, maxResults, firstResult);
    }

    private List<Avion> findAvionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Avion.class));
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

    public Avion findAvion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Avion.class, id);
        } finally {
            em.close();
        }
    }

    public int getAvionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Avion> rt = cq.from(Avion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
