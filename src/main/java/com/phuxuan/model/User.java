package com.phuxuan.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class User {
	protected int id;
    protected String name;
    protected String email;
    protected int idcountry;
    protected String password;

    public User() {}

    public User(String name, String email, int idcountry) {
        super();
        this.name = name;
        this.email = email;
        this.idcountry = idcountry;
    }

    public User(int id, String name, String email, int idcountry) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.idcountry = idcountry;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    @NotEmpty (message = "Name not empty")
	@Length(min = 3, max = 10 , message = "Lenght of Name form 3 - 10 character ")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @NotEmpty
	@Email(message = "Email format not right")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

	public int getIdcountry() {
		return idcountry;
	}

	public void setIdcountry(int idcountry) {
		this.idcountry = idcountry;
	}

	@NotEmpty
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", message = "Format password not right")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    

}
