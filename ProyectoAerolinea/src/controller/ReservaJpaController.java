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
import model.Formapago;
import model.Usuario;
import model.Vuelo;
import model.Tiquete;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Reserva;

/**
 *
 * @author maria
 */
public class ReservaJpaController implements Serializable {

    public ReservaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Reserva reserva) throws PreexistingEntityException, Exception {
        if (reserva.getTiqueteList() == null) {
            reserva.setTiqueteList(new ArrayList<Tiquete>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Formapago formaPagoidFormapago = reserva.getFormaPagoidFormapago();
            if (formaPagoidFormapago != null) {
                formaPagoidFormapago = em.getReference(formaPagoidFormapago.getClass(), formaPagoidFormapago.getIdFormapago());
                reserva.setFormaPagoidFormapago(formaPagoidFormapago);
            }
            Usuario usuarioidUsuario = reserva.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario = em.getReference(usuarioidUsuario.getClass(), usuarioidUsuario.getIdUsuario());
                reserva.setUsuarioidUsuario(usuarioidUsuario);
            }
            Vuelo vueloidVuelo = reserva.getVueloidVuelo();
            if (vueloidVuelo != null) {
                vueloidVuelo = em.getReference(vueloidVuelo.getClass(), vueloidVuelo.getIdVuelo());
                reserva.setVueloidVuelo(vueloidVuelo);
            }
            List<Tiquete> attachedTiqueteList = new ArrayList<Tiquete>();
            for (Tiquete tiqueteListTiqueteToAttach : reserva.getTiqueteList()) {
                tiqueteListTiqueteToAttach = em.getReference(tiqueteListTiqueteToAttach.getClass(), tiqueteListTiqueteToAttach.getIdTiquete());
                attachedTiqueteList.add(tiqueteListTiqueteToAttach);
            }
            reserva.setTiqueteList(attachedTiqueteList);
            em.persist(reserva);
            if (formaPagoidFormapago != null) {
                formaPagoidFormapago.getReservaList().add(reserva);
                formaPagoidFormapago = em.merge(formaPagoidFormapago);
            }
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getReservaList().add(reserva);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            if (vueloidVuelo != null) {
                vueloidVuelo.getReservaList().add(reserva);
                vueloidVuelo = em.merge(vueloidVuelo);
            }
            for (Tiquete tiqueteListTiquete : reserva.getTiqueteList()) {
                Reserva oldReservaidReservaOfTiqueteListTiquete = tiqueteListTiquete.getReservaidReserva();
                tiqueteListTiquete.setReservaidReserva(reserva);
                tiqueteListTiquete = em.merge(tiqueteListTiquete);
                if (oldReservaidReservaOfTiqueteListTiquete != null) {
                    oldReservaidReservaOfTiqueteListTiquete.getTiqueteList().remove(tiqueteListTiquete);
                    oldReservaidReservaOfTiqueteListTiquete = em.merge(oldReservaidReservaOfTiqueteListTiquete);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReserva(reserva.getIdReserva()) != null) {
                throw new PreexistingEntityException("Reserva " + reserva + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Reserva reserva) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Reserva persistentReserva = em.find(Reserva.class, reserva.getIdReserva());
            Formapago formaPagoidFormapagoOld = persistentReserva.getFormaPagoidFormapago();
            Formapago formaPagoidFormapagoNew = reserva.getFormaPagoidFormapago();
            Usuario usuarioidUsuarioOld = persistentReserva.getUsuarioidUsuario();
            Usuario usuarioidUsuarioNew = reserva.getUsuarioidUsuario();
            Vuelo vueloidVueloOld = persistentReserva.getVueloidVuelo();
            Vuelo vueloidVueloNew = reserva.getVueloidVuelo();
            List<Tiquete> tiqueteListOld = persistentReserva.getTiqueteList();
            List<Tiquete> tiqueteListNew = reserva.getTiqueteList();
            List<String> illegalOrphanMessages = null;
            for (Tiquete tiqueteListOldTiquete : tiqueteListOld) {
                if (!tiqueteListNew.contains(tiqueteListOldTiquete)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tiquete " + tiqueteListOldTiquete + " since its reservaidReserva field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (formaPagoidFormapagoNew != null) {
                formaPagoidFormapagoNew = em.getReference(formaPagoidFormapagoNew.getClass(), formaPagoidFormapagoNew.getIdFormapago());
                reserva.setFormaPagoidFormapago(formaPagoidFormapagoNew);
            }
            if (usuarioidUsuarioNew != null) {
                usuarioidUsuarioNew = em.getReference(usuarioidUsuarioNew.getClass(), usuarioidUsuarioNew.getIdUsuario());
                reserva.setUsuarioidUsuario(usuarioidUsuarioNew);
            }
            if (vueloidVueloNew != null) {
                vueloidVueloNew = em.getReference(vueloidVueloNew.getClass(), vueloidVueloNew.getIdVuelo());
                reserva.setVueloidVuelo(vueloidVueloNew);
            }
            List<Tiquete> attachedTiqueteListNew = new ArrayList<Tiquete>();
            for (Tiquete tiqueteListNewTiqueteToAttach : tiqueteListNew) {
                tiqueteListNewTiqueteToAttach = em.getReference(tiqueteListNewTiqueteToAttach.getClass(), tiqueteListNewTiqueteToAttach.getIdTiquete());
                attachedTiqueteListNew.add(tiqueteListNewTiqueteToAttach);
            }
            tiqueteListNew = attachedTiqueteListNew;
            reserva.setTiqueteList(tiqueteListNew);
            reserva = em.merge(reserva);
            if (formaPagoidFormapagoOld != null && !formaPagoidFormapagoOld.equals(formaPagoidFormapagoNew)) {
                formaPagoidFormapagoOld.getReservaList().remove(reserva);
                formaPagoidFormapagoOld = em.merge(formaPagoidFormapagoOld);
            }
            if (formaPagoidFormapagoNew != null && !formaPagoidFormapagoNew.equals(formaPagoidFormapagoOld)) {
                formaPagoidFormapagoNew.getReservaList().add(reserva);
                formaPagoidFormapagoNew = em.merge(formaPagoidFormapagoNew);
            }
            if (usuarioidUsuarioOld != null && !usuarioidUsuarioOld.equals(usuarioidUsuarioNew)) {
                usuarioidUsuarioOld.getReservaList().remove(reserva);
                usuarioidUsuarioOld = em.merge(usuarioidUsuarioOld);
            }
            if (usuarioidUsuarioNew != null && !usuarioidUsuarioNew.equals(usuarioidUsuarioOld)) {
                usuarioidUsuarioNew.getReservaList().add(reserva);
                usuarioidUsuarioNew = em.merge(usuarioidUsuarioNew);
            }
            if (vueloidVueloOld != null && !vueloidVueloOld.equals(vueloidVueloNew)) {
                vueloidVueloOld.getReservaList().remove(reserva);
                vueloidVueloOld = em.merge(vueloidVueloOld);
            }
            if (vueloidVueloNew != null && !vueloidVueloNew.equals(vueloidVueloOld)) {
                vueloidVueloNew.getReservaList().add(reserva);
                vueloidVueloNew = em.merge(vueloidVueloNew);
            }
            for (Tiquete tiqueteListNewTiquete : tiqueteListNew) {
                if (!tiqueteListOld.contains(tiqueteListNewTiquete)) {
                    Reserva oldReservaidReservaOfTiqueteListNewTiquete = tiqueteListNewTiquete.getReservaidReserva();
                    tiqueteListNewTiquete.setReservaidReserva(reserva);
                    tiqueteListNewTiquete = em.merge(tiqueteListNewTiquete);
                    if (oldReservaidReservaOfTiqueteListNewTiquete != null && !oldReservaidReservaOfTiqueteListNewTiquete.equals(reserva)) {
                        oldReservaidReservaOfTiqueteListNewTiquete.getTiqueteList().remove(tiqueteListNewTiquete);
                        oldReservaidReservaOfTiqueteListNewTiquete = em.merge(oldReservaidReservaOfTiqueteListNewTiquete);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = reserva.getIdReserva();
                if (findReserva(id) == null) {
                    throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.");
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
            Reserva reserva;
            try {
                reserva = em.getReference(Reserva.class, id);
                reserva.getIdReserva();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reserva with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tiquete> tiqueteListOrphanCheck = reserva.getTiqueteList();
            for (Tiquete tiqueteListOrphanCheckTiquete : tiqueteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Reserva (" + reserva + ") cannot be destroyed since the Tiquete " + tiqueteListOrphanCheckTiquete + " in its tiqueteList field has a non-nullable reservaidReserva field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Formapago formaPagoidFormapago = reserva.getFormaPagoidFormapago();
            if (formaPagoidFormapago != null) {
                formaPagoidFormapago.getReservaList().remove(reserva);
                formaPagoidFormapago = em.merge(formaPagoidFormapago);
            }
            Usuario usuarioidUsuario = reserva.getUsuarioidUsuario();
            if (usuarioidUsuario != null) {
                usuarioidUsuario.getReservaList().remove(reserva);
                usuarioidUsuario = em.merge(usuarioidUsuario);
            }
            Vuelo vueloidVuelo = reserva.getVueloidVuelo();
            if (vueloidVuelo != null) {
                vueloidVuelo.getReservaList().remove(reserva);
                vueloidVuelo = em.merge(vueloidVuelo);
            }
            em.remove(reserva);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Reserva> findReservaEntities() {
        return findReservaEntities(true, -1, -1);
    }

    public List<Reserva> findReservaEntities(int maxResults, int firstResult) {
        return findReservaEntities(false, maxResults, firstResult);
    }

    private List<Reserva> findReservaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Reserva.class));
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

    public Reserva findReserva(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Reserva.class, id);
        } finally {
            em.close();
        }
    }

    public int getReservaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Reserva> rt = cq.from(Reserva.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
