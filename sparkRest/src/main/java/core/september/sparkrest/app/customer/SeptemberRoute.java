package core.september.sparkrest.app.customer;

import core.september.sparkrest.annotation.Customer;

@Customer(name="sepptember")
public class SeptemberRoute extends CustomerRouter {

	private static final String customerPath = "/auth/september/";
	@Override
	public void assign() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getCustomerPath() {
		return customerPath;
	}

}
