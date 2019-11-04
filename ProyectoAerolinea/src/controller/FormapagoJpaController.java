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
import model.Reserva;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Formapago;

/**
 *
 * @author maria
 */
public class FormapagoJpaController implements Serializable {

    public FormapagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Formapago formapago) throws PreexistingEntityException, Exception {
        if (formapago.getReservaList() == null) {
            formapago.setReservaList(new ArrayList<Reserva>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Reserva> attachedReservaList = new ArrayList<Reserva>();
            for (Reserva reservaListReservaToAttach : formapago.getReservaList()) {
                reservaListReservaToAttach = em.getReference(reservaListReservaToAttach.getClass(), reservaListReservaToAttach.getIdReserva());
                attachedReservaList.add(reservaListReservaToAttach);
            }
            formapago.setReservaList(attachedReservaList);
            em.persist(formapago);
            for (Reserva reservaListReserva : formapago.getReservaList()) {
                Formapago oldFormaPagoidFormapagoOfReservaListReserva = reservaListReserva.getFormaPagoidFormapago();
                reservaListReserva.setFormaPagoidFormapago(formapago);
                reservaListReserva = em.merge(reservaListReserva);
                if (oldFormaPagoidFormapagoOfReservaListReserva != null) {
                    oldFormaPagoidFormapagoOfReservaListReserva.getReservaList().remove(reservaListReserva);
                    oldFormaPagoidFormapagoOfReservaListReserva = em.merge(oldFormaPagoidFormapagoOfReservaListReserva);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFormapago(formapago.getIdFormapago()) != null) {
                throw new PreexistingEntityException("Formapago " + formapago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Formapago formapago) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Formapago persistentFormapago = em.find(Formapago.class, formapago.getIdFormapago());
            List<Reserva> reservaListOld = persistentFormapago.getReservaList();
            List<Reserva> reservaListNew = formapago.getReservaList();
            List<String> illegalOrphanMessages = null;
            for (Reserva reservaListOldReserva : reservaListOld) {
                if (!reservaListNew.contains(reservaListOldReserva)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Reserva " + reservaListOldReserva + " since its formaPagoidFormapago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Reserva> attachedReservaListNew = new ArrayList<Reserva>();
            for (Reserva reservaListNewReservaToAttach : reservaListNew) {
                reservaListNewReservaToAttach = em.getReference(reservaListNewReservaToAttach.getClass(), reservaListNewReservaToAttach.getIdReserva());
                attachedReservaListNew.add(reservaListNewReservaToAttach);
            }
            reservaListNew = attachedReservaListNew;
            formapago.setReservaList(reservaListNew);
            formapago = em.merge(formapago);
            for (Reserva reservaListNewReserva : reservaListNew) {
                if (!reservaListOld.contains(reservaListNewReserva)) {
                    Formapago oldFormaPagoidFormapagoOfReservaListNewReserva = reservaListNewReserva.getFormaPagoidFormapago();
                    reservaListNewReserva.setFormaPagoidFormapago(formapago);
                    reservaListNewReserva = em.merge(reservaListNewReserva);
                    if (oldFormaPagoidFormapagoOfReservaListNewReserva != null && !oldFormaPagoidFormapagoOfReservaListNewReserva.equals(formapago)) {
                        oldFormaPagoidFormapagoOfReservaListNewReserva.getReservaList().remove(reservaListNewReserva);
                        oldFormaPagoidFormapagoOfReservaListNewReserva = em.merge(oldFormaPagoidFormapagoOfReservaListNewReserva);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = formapago.getIdFormapago();
                if (findFormapago(id) == null) {
                    throw new NonexistentEntityException("The formapago with id " + id + " no longer exists.");
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
            Formapago formapago;
            try {
                formapago = em.getReference(Formapago.class, id);
                formapago.getIdFormapago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formapago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Reserva> reservaListOrphanCheck = formapago.getReservaList();
            for (Reserva reservaListOrphanCheckReserva : reservaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Formapago (" + formapago + ") cannot be destroyed since the Reserva " + reservaListOrphanCheckReserva + " in its reservaList field has a non-nullable formaPagoidFormapago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(formapago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Formapago> findFormapagoEntities() {
        return findFormapagoEntities(true, -1, -1);
    }

    public List<Formapago> findFormapagoEntities(int maxResults, int firstResult) {
        return findFormapagoEntities(false, maxResults, firstResult);
    }

    private List<Formapago> findFormapagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Formapago.class));
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

    public Formapago findFormapago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Formapago.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormapagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Formapago> rt = cq.from(Formapago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
