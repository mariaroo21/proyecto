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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Reserva;
import model.Tiquete;

/**
 *
 * @author maria
 */
public class TiqueteJpaController implements Serializable {

    public TiqueteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiquete tiquete) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva reservaidReserva = tiquete.getReservaidReserva();
            if (reservaidReserva != null) {
                reservaidReserva = em.getReference(reservaidReserva.getClass(), reservaidReserva.getIdReserva());
                tiquete.setReservaidReserva(reservaidReserva);
            }
            em.persist(tiquete);
            if (reservaidReserva != null) {
                reservaidReserva.getTiqueteList().add(tiquete);
                reservaidReserva = em.merge(reservaidReserva);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTiquete(tiquete.getIdTiquete()) != null) {
                throw new PreexistingEntityException("Tiquete " + tiquete + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiquete tiquete) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiquete persistentTiquete = em.find(Tiquete.class, tiquete.getIdTiquete());
            Reserva reservaidReservaOld = persistentTiquete.getReservaidReserva();
            Reserva reservaidReservaNew = tiquete.getReservaidReserva();
            if (reservaidReservaNew != null) {
                reservaidReservaNew = em.getReference(reservaidReservaNew.getClass(), reservaidReservaNew.getIdReserva());
                tiquete.setReservaidReserva(reservaidReservaNew);
            }
            tiquete = em.merge(tiquete);
            if (reservaidReservaOld != null && !reservaidReservaOld.equals(reservaidReservaNew)) {
                reservaidReservaOld.getTiqueteList().remove(tiquete);
                reservaidReservaOld = em.merge(reservaidReservaOld);
            }
            if (reservaidReservaNew != null && !reservaidReservaNew.equals(reservaidReservaOld)) {
                reservaidReservaNew.getTiqueteList().add(tiquete);
                reservaidReservaNew = em.merge(reservaidReservaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiquete.getIdTiquete();
                if (findTiquete(id) == null) {
                    throw new NonexistentEntityException("The tiquete with id " + id + " no longer exists.");
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
            Tiquete tiquete;
            try {
                tiquete = em.getReference(Tiquete.class, id);
                tiquete.getIdTiquete();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiquete with id " + id + " no longer exists.", enfe);
            }
            Reserva reservaidReserva = tiquete.getReservaidReserva();
            if (reservaidReserva != null) {
                reservaidReserva.getTiqueteList().remove(tiquete);
                reservaidReserva = em.merge(reservaidReserva);
            }
            em.remove(tiquete);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiquete> findTiqueteEntities() {
        return findTiqueteEntities(true, -1, -1);
    }

    public List<Tiquete> findTiqueteEntities(int maxResults, int firstResult) {
        return findTiqueteEntities(false, maxResults, firstResult);
    }

    private List<Tiquete> findTiqueteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiquete.class));
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

    public Tiquete findTiquete(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiquete.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiqueteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiquete> rt = cq.from(Tiquete.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
