package be.intec.models;


public class BranchX {

	private Integer id;

	private String name;

	CompanyX company;


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


	public CompanyX getCompany() {

		return company;
	}


	public void setCompany( final CompanyX company ) {

		this.company = company;
	}


	@Override
	public String toString() {
		String info = "\nBranch is being printed.\n";
		return info + "BranchX{" +
				"id=" + id +
				", name='" + name + '\'' +
				// ", company=" + company +
				'}';
	}

}
