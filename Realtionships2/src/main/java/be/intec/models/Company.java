package be.intec.models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table( name = "company" )
public class Company {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( name = "id", nullable = false )
	private Integer id;

	@Column( name = "naam", nullable = false, length = 100 )
	private String name;

	@Column( name = "gecreeerd" )
	private LocalDate registry;

	@OneToMany
	@JoinColumn( name = "company_id" )
	private List< Branch > branches = new ArrayList<>();

}