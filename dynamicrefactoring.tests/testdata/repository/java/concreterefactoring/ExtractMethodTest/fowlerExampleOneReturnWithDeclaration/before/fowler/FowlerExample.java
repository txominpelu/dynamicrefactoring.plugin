package fowler;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Example from Fowler's book for Extract Method refactoring.
 * 
 * @author Fowler, M.
 * 
 */
public class FowlerExample {

	private String _name;

	private Vector _orders;

	public void printOwning() {

		// print banner (first extraction without variables)

		System.out.println("**********************");
		System.out.println("**** Custom Owes *****");
		System.out.println("**********************");

		// calculate outstanding (third extraction with one return without
		// formal argument, can move variable e and outstanding declaration and
		// initialization inside extracted method)

		Enumeration e = _orders.elements();
		double outstanding = 0.0;
		while (e.hasMoreElements()) {
			Order each = (Order) e.nextElement();
			outstanding += each.getAmount();
		}

		// print details (second extraction with one formal argument -
		// outstanding)

		System.out.println("name:" + _name);
		System.out.println("amount:" + outstanding);

	}

	public void printOwning2(double previousAmount) {

		Enumeration e = _orders.elements();
		double outstanding = previousAmount * 1.2;

		// print banner (first extraction without variables)
		System.out.println("**********************");
		System.out.println("**** Custom Owes *****");
		System.out.println("**********************");

		// calculate outstanding (third extraction with one return and
		// formal argument, we need to pass the outstanding variable)

		while (e.hasMoreElements()) {
			Order each = (Order) e.nextElement();
			outstanding += each.getAmount();
		}

		// print details (second extraction with one formal argument -
		// outstanding)

		System.out.println("name:" + _name);
		System.out.println("amount:" + outstanding);

	}

	public void printOwning3(double previousAmount) {

		Enumeration<Order> e = _orders.elements();
		double outstanding = previousAmount * 1.2;
		double otherOutstanding = 0.0;

		// print banner (first extraction without variables)
		System.out.println("**********************");
		System.out.println("**** Custom Owes *****");
		System.out.println("**********************");

		// calculate outstanding (impossible extraction with double return)
		while (e.hasMoreElements()) {
			Order each = (Order) e.nextElement();
			outstanding += each.getAmount();
		}
		otherOutstanding += outstanding;

		// print details (second extraction with one formal argument -
		// outstanding)

		System.out.println("name:" + _name);
		System.out.println("amount:" + outstanding);
		System.out.println("collection: " + otherOutstanding);

	}
}
