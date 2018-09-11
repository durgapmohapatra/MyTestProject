package SuperMartApplication;
 
import java.io.Serializable;
import java.util.Date;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
 
@Entity
@Table(name = "orderDetails", //
uniqueConstraints = { @UniqueConstraint(columnNames = "Order_Num") })
public class orderDetails implements Serializable {
 
  
 
    private int orderId;
    private Date orderDate;
	
    private int orderLineId;
	private int quantity;
	private double price;
 
    private String userName;
	private String pwd;
    private String location;
    private int mobile;
	private String country;
 
    
    //@Column(name = "orderId", length = 10)
    public int orderId() {
        return orderId;
    }
 
    public void setorderId(Int orderId) {
        this.orderId = orderId;
    }
  
   // @Column(name = "orderDate", nullable = false)
    public Date getorderDate() {
        return orderDate;
    }
 
    public void setorderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
	
	//@Column(name = "orderLineId", length = 10)
    public Int orderLineId() {
        return orderLineId;
    }
 
    public void setorderLineId(Int orderLineId) {
        this.orderLineId = orderLineId;
    }
 
    //@Column(name = "price", nullable = false)
    public double getprice() {
        return price;
    }
 
    public void setprice(double price) {
        this.price = price;
    }
 
    //@Column(name = "userName", length = 100, nullable = false)
    public String getuserName() {
        return userName;
    }
 
    public void setuserName(String userName) {
        this.userName = userName;
    }
	
	// @Column(name = "pwd", length = 20, nullable = false)
    public String pwd() {
        return pwd;
    }
 
    public void setpwd(String pwd) {
        this.pwd = pwd;
    }
 
   // @Column(name = "location", length = 255, nullable = false)
    public String getlocation() {
        return location;
    }
 
    public void setlocation(String location) {
        this.location = location;
    }
 
    //@Column(name = "mobile", length = 10, nullable = false)
    public int getmobile() {
        return mobile;
    }
 
    public void setmobile(String mobile) {
        this.mobile = mobile;
    }
 
    
}
