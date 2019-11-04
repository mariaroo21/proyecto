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
@Table(name = "formapago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formapago.findAll", query = "SELECT f FROM Formapago f")
    , @NamedQuery(name = "Formapago.findByIdFormapago", query = "SELECT f FROM Formapago f WHERE f.idFormapago = :idFormapago")
    , @NamedQuery(name = "Formapago.findByDescripcion", query = "SELECT f FROM Formapago f WHERE f.descripcion = :descripcion")})
public class Formapago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idFormapago")
    private Integer idFormapago;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "formaPagoidFormapago")
    private List<Reserva> reservaList;

    public Formapago() {
    }

    public Formapago(Integer idFormapago) {
        this.idFormapago = idFormapago;
    }

    public Integer getIdFormapago() {
        return idFormapago;
    }

    public void setIdFormapago(Integer idFormapago) {
        this.idFormapago = idFormapago;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormapago != null ? idFormapago.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Formapago)) {
            return false;
        }
        Formapago other = (Formapago) object;
        if ((this.idFormapago == null && other.idFormapago != null) || (this.idFormapago != null && !this.idFormapago.equals(other.idFormapago))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Formapago[ idFormapago=" + idFormapago + " ]";
    }
    
}
