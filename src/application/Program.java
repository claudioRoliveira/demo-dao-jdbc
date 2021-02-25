package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		Seller seller = sellerDao.findById(3);
		
		Department dep = new Department(2,null);
		
		System.out.println(seller);
		
		System.out.println("TESTE 2");
		
		
		List<Seller> list = sellerDao.findAll();
		
		for (Seller obj:list) {
			System.out.println(obj);	
		}
		
		System.out.println("TESTE 3");
		
		Seller newSeller = new Seller(null,"Zeze","zeze@email",new Date(),4000.00,dep);
		sellerDao.insert(newSeller);
		
		System.out.println(newSeller);
		
	}

}
