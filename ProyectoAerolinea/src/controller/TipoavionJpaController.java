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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Tipoavion;

/**
 *
 * @author maria
 */
public class TipoavionJpaController implements Serializable {

    public TipoavionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoavion tipoavion) throws PreexistingEntityException, Exception {
        if (tipoavion.getAvionList() == null) {
            tipoavion.setAvionList(new ArrayList<Avion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Avion> attachedAvionList = new ArrayList<Avion>();
            for (Avion avionListAvionToAttach : tipoavion.getAvionList()) {
                avionListAvionToAttach = em.getReference(avionListAvionToAttach.getClass(), avionListAvionToAttach.getIdAvion());
                attachedAvionList.add(avionListAvionToAttach);
            }
            tipoavion.setAvionList(attachedAvionList);
            em.persist(tipoavion);
            for (Avion avionListAvion : tipoavion.getAvionList()) {
                Tipoavion oldTipoAvionidtipoAvionOfAvionListAvion = avionListAvion.getTipoAvionidtipoAvion();
                avionListAvion.setTipoAvionidtipoAvion(tipoavion);
                avionListAvion = em.merge(avionListAvion);
                if (oldTipoAvionidtipoAvionOfAvionListAvion != null) {
                    oldTipoAvionidtipoAvionOfAvionListAvion.getAvionList().remove(avionListAvion);
                    oldTipoAvionidtipoAvionOfAvionListAvion = em.merge(oldTipoAvionidtipoAvionOfAvionListAvion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoavion(tipoavion.getIdtipoAvion()) != null) {
                throw new PreexistingEntityException("Tipoavion " + tipoavion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoavion tipoavion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipoavion persistentTipoavion = em.find(Tipoavion.class, tipoavion.getIdtipoAvion());
            List<Avion> avionListOld = persistentTipoavion.getAvionList();
            List<Avion> avionListNew = tipoavion.getAvionList();
            List<String> illegalOrphanMessages = null;
            for (Avion avionListOldAvion : avionListOld) {
                if (!avionListNew.contains(avionListOldAvion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Avion " + avionListOldAvion + " since its tipoAvionidtipoAvion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Avion> attachedAvionListNew = new ArrayList<Avion>();
            for (Avion avionListNewAvionToAttach : avionListNew) {
                avionListNewAvionToAttach = em.getReference(avionListNewAvionToAttach.getClass(), avionListNewAvionToAttach.getIdAvion());
                attachedAvionListNew.add(avionListNewAvionToAttach);
            }
            avionListNew = attachedAvionListNew;
            tipoavion.setAvionList(avionListNew);
            tipoavion = em.merge(tipoavion);
            for (Avion avionListNewAvion : avionListNew) {
                if (!avionListOld.contains(avionListNewAvion)) {
                    Tipoavion oldTipoAvionidtipoAvionOfAvionListNewAvion = avionListNewAvion.getTipoAvionidtipoAvion();
                    avionListNewAvion.setTipoAvionidtipoAvion(tipoavion);
                    avionListNewAvion = em.merge(avionListNewAvion);
                    if (oldTipoAvionidtipoAvionOfAvionListNewAvion != null && !oldTipoAvionidtipoAvionOfAvionListNewAvion.equals(tipoavion)) {
                        oldTipoAvionidtipoAvionOfAvionListNewAvion.getAvionList().remove(avionListNewAvion);
                        oldTipoAvionidtipoAvionOfAvionListNewAvion = em.merge(oldTipoAvionidtipoAvionOfAvionListNewAvion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoavion.getIdtipoAvion();
                if (findTipoavion(id) == null) {
                    throw new NonexistentEntityException("The tipoavion with id " + id + " no longer exists.");
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
            Tipoavion tipoavion;
            try {
                tipoavion = em.getReference(Tipoavion.class, id);
                tipoavion.getIdtipoAvion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoavion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Avion> avionListOrphanCheck = tipoavion.getAvionList();
            for (Avion avionListOrphanCheckAvion : avionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipoavion (" + tipoavion + ") cannot be destroyed since the Avion " + avionListOrphanCheckAvion + " in its avionList field has a non-nullable tipoAvionidtipoAvion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoavion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipoavion> findTipoavionEntities() {
        return findTipoavionEntities(true, -1, -1);
    }

    public List<Tipoavion> findTipoavionEntities(int maxResults, int firstResult) {
        return findTipoavionEntities(false, maxResults, firstResult);
    }

    private List<Tipoavion> findTipoavionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoavion.class));
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

    public Tipoavion findTipoavion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoavion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoavionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoavion> rt = cq.from(Tipoavion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
