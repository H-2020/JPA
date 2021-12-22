package be.intec.models;


import java.util.ArrayList;
import java.util.List;


public class CompanyX {

	Integer id;

	String name;

	List< BranchX > branches = new ArrayList< BranchX >();


	public Integer getId() {

		return id;
	}


	public void setId( final Integer id ) {

		this.id = id;
	}


	public String getName() {

		return name;
	}


	public void setName( final String name ) {

		this.name = name;
	}


	public List< BranchX > getBranches() {

		return branches;
	}


	public void setBranches( final List< BranchX > branches ) {

		this.branches = branches;
	}


	@Override
	public String toString() {

		String info = "\nCompany is being printed.\n";
		return info + "CompanyX{" +
				"id=" + id +
				", name='" + name + '\'' +
				", branches=" + branches +
				'}';
	}

}
