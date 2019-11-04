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
@Table(name = "vuelo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vuelo.findAll", query = "SELECT v FROM Vuelo v")
    , @NamedQuery(name = "Vuelo.findByIdVuelo", query = "SELECT v FROM Vuelo v WHERE v.idVuelo = :idVuelo")
    , @NamedQuery(name = "Vuelo.findByHora", query = "SELECT v FROM Vuelo v WHERE v.hora = :hora")
    , @NamedQuery(name = "Vuelo.findByPrecio", query = "SELECT v FROM Vuelo v WHERE v.precio = :precio")
    , @NamedQuery(name = "Vuelo.findByIdayvuelta", query = "SELECT v FROM Vuelo v WHERE v.idayvuelta = :idayvuelta")
    , @NamedQuery(name = "Vuelo.findByDescuento", query = "SELECT v FROM Vuelo v WHERE v.descuento = :descuento")
    , @NamedQuery(name = "Vuelo.findByCuidadorigen", query = "SELECT v FROM Vuelo v WHERE v.cuidadorigen = :cuidadorigen")
    , @NamedQuery(name = "Vuelo.findByCiudaddestino", query = "SELECT v FROM Vuelo v WHERE v.ciudaddestino = :ciudaddestino")
    , @NamedQuery(name = "Vuelo.findByDurcion", query = "SELECT v FROM Vuelo v WHERE v.durcion = :durcion")})
public class Vuelo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVuelo")
    private Integer idVuelo;
    @Basic(optional = false)
    @Column(name = "hora")
    private String hora;
    @Basic(optional = false)
    @Column(name = "precio")
    private float precio;
    @Basic(optional = false)
    @Column(name = "idayvuelta")
    private short idayvuelta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "descuento")
    private Float descuento;
    @Basic(optional = false)
    @Column(name = "cuidadorigen")
    private String cuidadorigen;
    @Basic(optional = false)
    @Column(name = "ciudaddestino")
    private String ciudaddestino;
    @Basic(optional = false)
    @Column(name = "durcion")
    private String durcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vueloidVuelo")
    private List<Viaje> viajeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vueloidVuelo")
    private List<Reserva> reservaList;
    @JoinColumn(name = "Avion_idAvion", referencedColumnName = "idAvion")
    @ManyToOne(optional = false)
    private Avion avionidAvion;
    @JoinColumn(name = "Ciudad_nombre", referencedColumnName = "nombre")
    @ManyToOne(optional = false)
    private Ciudad ciudadnombre;

    public Vuelo() {
    }

    public Vuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    public Vuelo(Integer idVuelo, String hora, float precio, short idayvuelta, String cuidadorigen, String ciudaddestino, String durcion) {
        this.idVuelo = idVuelo;
        this.hora = hora;
        this.precio = precio;
        this.idayvuelta = idayvuelta;
        this.cuidadorigen = cuidadorigen;
        this.ciudaddestino = ciudaddestino;
        this.durcion = durcion;
    }

    public Integer getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public short getIdayvuelta() {
        return idayvuelta;
    }

    public void setIdayvuelta(short idayvuelta) {
        this.idayvuelta = idayvuelta;
    }

    public Float getDescuento() {
        return descuento;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public String getCuidadorigen() {
        return cuidadorigen;
    }

    public void setCuidadorigen(String cuidadorigen) {
        this.cuidadorigen = cuidadorigen;
    }

    public String getCiudaddestino() {
        return ciudaddestino;
    }

    public void setCiudaddestino(String ciudaddestino) {
        this.ciudaddestino = ciudaddestino;
    }

    public String getDurcion() {
        return durcion;
    }

    public void setDurcion(String durcion) {
        this.durcion = durcion;
    }

    @XmlTransient
    public List<Viaje> getViajeList() {
        return viajeList;
    }

    public void setViajeList(List<Viaje> viajeList) {
        this.viajeList = viajeList;
    }

    @XmlTransient
    public List<Reserva> getReservaList() {
        return reservaList;
    }

    public void setReservaList(List<Reserva> reservaList) {
        this.reservaList = reservaList;
    }

    public Avion getAvionidAvion() {
        return avionidAvion;
    }

    public void setAvionidAvion(Avion avionidAvion) {
        this.avionidAvion = avionidAvion;
    }

    public Ciudad getCiudadnombre() {
        return ciudadnombre;
    }

    public void setCiudadnombre(Ciudad ciudadnombre) {
        this.ciudadnombre = ciudadnombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVuelo != null ? idVuelo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vuelo)) {
            return false;
        }
        Vuelo other = (Vuelo) object;
        if ((this.idVuelo == null && other.idVuelo != null) || (this.idVuelo != null && !this.idVuelo.equals(other.idVuelo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Vuelo[ idVuelo=" + idVuelo + " ]";
    }
    
}
