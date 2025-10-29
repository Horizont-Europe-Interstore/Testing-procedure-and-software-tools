package interstore.DERCurve;

import jakarta.persistence.*;

@Entity
@Table(name="curve_data")
public class CurveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x_value")
    Integer x_value;

    @Column(name = "y_value")
    Integer y_value;

    @Column(name = "excitation")
    boolean excitation = false;

//    @ManyToOne(cascade = CascadeType.ALL)
//    private DERCurveEntity derCurveEntity;

    public Long getId() {
        return id;
    }

    public Integer getX_value() {
        return x_value;
    }

    public void setX_value(Integer x_value) {
        this.x_value = x_value;
    }

    public Integer getY_value() {
        return y_value;
    }

    public void setY_value(Integer y_value) {
        this.y_value = y_value;
    }

    public boolean isExcitation() {
        return excitation;
    }

    public void setExcitation(boolean excitation) {
        this.excitation = excitation;
    }

//    public DERCurveEntity getDerCurveEntity() {
//        return derCurveEntity;
//    }
//
//    public void setDerCurveEntity(DERCurveEntity derCurveEntity) {
//        this.derCurveEntity = derCurveEntity;
//    }
}
