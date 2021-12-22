package be.intec.views;


import be.intec.models.BranchX;
import be.intec.models.CompanyX;

public class BiDirectionalApp {

	public static void main( String[] args ) {

		CompanyX tesla = new CompanyX();
		tesla.setName( "Tesla Co." );
		tesla.setId( 1 );

		BranchX belgium = new BranchX();
		belgium.setName( "Tesla BE" );
		belgium.setId( 2 );
		belgium.setCompany( tesla );

		BranchX usa = new BranchX();
		usa.setId( 3 );
		usa.setName( "Tesla US" );
		usa.setCompany( tesla );

		tesla.getBranches().add( belgium );
		tesla.getBranches().add( usa );

		System.out.println(tesla);



	}

}
