package es.urjc.etsii.co.clickandbuyweb.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import es.urjc.etsii.co.clickandbuyweb.dao.UserDAO;
import es.urjc.etsii.co.clickandbuyweb.models.Product;
import es.urjc.etsii.co.clickandbuyweb.models.User;

@Service
public class UserService {
	@Autowired
	private UserDAO userdao;
	
	public List<User> getUsers(){
		return userdao.findAll();
	}
	
	public String newUser(String email, String password) {
		User duplicate = userdao.findByUser_email(email);
		if(duplicate!=null) {
			return "status: user email already exists";
		}
		User u = new User();
		u.setUser_email(email);
		u.setUser_password(password);
		Date d = new Date();
		u.setJoin_date(d);
		userdao.save(u);
		return "status: saved";
	}
	
	public User userEmailSearch(String email) {
		User u = userdao.findByUser_email(email);
		if(u==null) {
			return null;
		}
		return u;
	}
	
	public String deleteUserId(int id) {
		Optional<User> u = userdao.findById(id);
		if(u.isEmpty()) {
			return "status: not found";
		}
		userdao.deleteById(id);
		return "status: deleted";
	}
	
	public User loginUser(String email, String password) {
		User u = userdao.findByUser_email(email);
		if(u==null) {
			return null;
		}
		if(!u.getUser_password().equals(password)) {
			return null;
		}
		Date d = new Date();
		u.setLast_login(d);
		userdao.save(u);
		return u;
	}
	
	public List<User> UserNameSearch (String name){
		return userdao.searchByUser_name(name);
	}
	
	public String addUserProduct(String name, String desc, double price, int units, int usid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user doesn't exist";
		}
		if(!u.isIs_supplier()) {
			return "status: this user is not a supplier and is not allowed to add products";
		}
		Product p = new Product();
		p.setProduct_name(name);
		p.setProduct_description(desc);
		if(units<0) {
			p.setProduct_stock(0);
		} else {
			p.setProduct_stock(units);
		}
		p.setProduct_price(price);
		if(p.getProduct_stock()>0) {
			p.setHas_stock(true);
			p.setIs_active(true);
		}
		List<Product> productlist = u.getUser_product_list();
		productlist.add(p);
		u.setUser_product_list(productlist);
		userdao.save(u);
		return "status: new product saved -> " + p.toString();
	}
	
	public String deleteUserProduct(int usid, int prodid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user doesn't exist";
		}
		if(!u.isIs_supplier()) {
			return "status: this user is not a supplier and has no products";
		}
		boolean found = false;
		List<Product> productlist = u.getUser_product_list();
		Product find = new Product();
		for(Product p: productlist) {
			if(p.getId()==prodid) {
				found = true;
				find = p;
			}
		}
		if(found) {
			productlist.remove(find);
			u.setUser_product_list(productlist);
			userdao.save(u);
			return "status: product deleted";
		} else {
			return "status: product not found";
		}
	}
	
	public String userProducts(int usid) {
		User u = userdao.findByUser_id(usid);
		Gson g = new Gson();
		if(u==null) {
			String status = "status: user not found";
			return g.toJson(status);
		}
		String json = g.toJson(u.getUser_product_list());
		return json;
	}
	
	public String setUserSupplier(int usid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		if(u.isIs_supplier()) {
			return "status: user is already a supplier";
		}
		u.setIs_supplier(true);
		userdao.save(u);
		return "status: user is a supplier now";
	}
	
	public String unsetUserSupplier(int usid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		if(!u.isIs_supplier()) {
			return "status: user is already not a supplier";
		}
		u.setIs_supplier(false);
		userdao.save(u);
		return "status: user is not a supplier anymore";
	}
	
	public String activateUser(int usid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		if(u.isIs_active()) {
			return "status: user is already active";
		}
		u.setIs_active(true);
		userdao.save(u);
		return "status: user is now active";
	}
	
	public String deactivateUser(int usid) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		if(!u.isIs_active()) {
			return "status: user is already deactivated";
		}
		u.setIs_active(false);
		userdao.save(u);
		return "status: user has been deactivated";
	}
	
	public String addressUser(int usid, String address) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_address(address);
		userdao.save(u);
		return "status: address updated";
	}
	
	public String setUserBankaccount(int usid, int account) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_bankaccount(account);
		userdao.save(u);
		return "status: user account saved";
	}
	
	public String nameUser(int usid, String name) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_name(name);
		userdao.save(u);
		return "status: name changed";
	}
	
	public String userUpdatePassword(int usid, String pass) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_password(pass);
		userdao.save(u);
		return "status: updated password";
	}
	
	public String phoneUser(int usid, int phone) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_phone(phone);
		userdao.save(u);
		return "status: phone saved";
	}
	
	public String realnameUser(int usid, String realname) {
		User u = userdao.findByUser_id(usid);
		if(u==null) {
			return "status: user not found";
		}
		u.setUser_realname(realname);
		userdao.save(u);
		return "status: real name saved";
	}
	
}
