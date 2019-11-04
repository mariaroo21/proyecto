/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author maria
 */
@Entity
@Table(name = "reserva")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reserva.findAll", query = "SELECT r FROM Reserva r")
    , @NamedQuery(name = "Reserva.findByIdReserva", query = "SELECT r FROM Reserva r WHERE r.idReserva = :idReserva")
    , @NamedQuery(name = "Reserva.findByCantasientos", query = "SELECT r FROM Reserva r WHERE r.cantasientos = :cantasientos")
    , @NamedQuery(name = "Reserva.findByPrecioTotal", query = "SELECT r FROM Reserva r WHERE r.precioTotal = :precioTotal")})
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idReserva")
    private Integer idReserva;
    @Basic(optional = false)
    @Column(name = "cantasientos")
    private int cantasientos;
    @Basic(optional = false)
    @Column(name = "precioTotal")
    private float precioTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservaidReserva")
    private List<Tiquete> tiqueteList;
    @JoinColumn(name = "FormaPago_idFormapago", referencedColumnName = "idFormapago")
    @ManyToOne(optional = false)
    private Formapago formaPagoidFormapago;
    @JoinColumn(name = "Usuario_idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario usuarioidUsuario;
    @JoinColumn(name = "Vuelo_idVuelo", referencedColumnName = "idVuelo")
    @ManyToOne(optional = false)
    private Vuelo vueloidVuelo;

    public Reserva() {
    }

    public Reserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Reserva(Integer idReserva, int cantasientos, float precioTotal) {
        this.idReserva = idReserva;
        this.cantasientos = cantasientos;
        this.precioTotal = precioTotal;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public int getCantasientos() {
        return cantasientos;
    }

    public void setCantasientos(int cantasientos) {
        this.cantasientos = cantasientos;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    @XmlTransient
    public List<Tiquete> getTiqueteList() {
        return tiqueteList;
    }

    public void setTiqueteList(List<Tiquete> tiqueteList) {
        this.tiqueteList = tiqueteList;
    }

    public Formapago getFormaPagoidFormapago() {
        return formaPagoidFormapago;
    }

    public void setFormaPagoidFormapago(Formapago formaPagoidFormapago) {
        this.formaPagoidFormapago = formaPagoidFormapago;
    }

    public Usuario getUsuarioidUsuario() {
        return usuarioidUsuario;
    }

    public void setUsuarioidUsuario(Usuario usuarioidUsuario) {
        this.usuarioidUsuario = usuarioidUsuario;
    }

    public Vuelo getVueloidVuelo() {
        return vueloidVuelo;
    }

    public void setVueloidVuelo(Vuelo vueloidVuelo) {
        this.vueloidVuelo = vueloidVuelo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReserva != null ? idReserva.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reserva)) {
            return false;
        }
        Reserva other = (Reserva) object;
        if ((this.idReserva == null && other.idReserva != null) || (this.idReserva != null && !this.idReserva.equals(other.idReserva))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Reserva[ idReserva=" + idReserva + " ]";
    }
    
}
