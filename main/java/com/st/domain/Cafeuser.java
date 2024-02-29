package com.st.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;


@Component
@Entity
@Table
public class Cafeuser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	//@SequenceGenerator(name="seq",sequenceName="seq_cafeuser_cfuid",allocationSize=10)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="seq")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cfuid;
	@Column
	private String cfemail ;
	@Column
	private String cfpassword ;
	@Column
	private String cfcontactno ;
	@Column
	private String cfrole ;
	@Column
	private String cfstatus ;
	@Column
	private String cfname ;
	

	public Cafeuser() {
		super();
	}


	public Cafeuser(Integer cfuid, String cfemail, String cfpassword, String cfcontactno, String cfrole,
			String cfstatus, String cfname) {
		super();
		this.cfuid = cfuid;
		this.cfemail = cfemail;
		this.cfpassword = cfpassword;
		this.cfcontactno = cfcontactno;
		this.cfrole = cfrole;
		this.cfstatus = cfstatus;
		this.cfname = cfname;
	}


	public Integer getCfuid() {
		return cfuid;
	}


	public void setCfuid(Integer cfuid) {
		this.cfuid = cfuid;
	}


	public String getCfemail() {
		return cfemail;
	}


	public void setCfemail(String cfemail) {
		this.cfemail = cfemail;
	}


	public String getCfpassword() {
		return cfpassword;
	}


	public void setCfpassword(String cfpassword) {
		this.cfpassword = cfpassword;
	}


	public String getCfcontactno() {
		return cfcontactno;
	}


	public void setCfcontactno(String cfcontactno) {
		this.cfcontactno = cfcontactno;
	}


	public String getCfrole() {
		return cfrole;
	}


	public void setCfrole(String cfrole) {
		this.cfrole = cfrole;
	}


	public String getCfstatus() {
		return cfstatus;
	}


	public void setCfstatus(String cfstatus) {
		this.cfstatus = cfstatus;
	}


	public String getCfname() {
		return cfname;
	}


	public void setCfname(String cfname) {
		this.cfname = cfname;
	}

	@Override
	public String toString() {
		return "Cafeuser [cfuid=" + cfuid + ", cfemail=" + cfemail + ", cfpassword=" + cfpassword + ", cfcontactno="
				+ cfcontactno + ", cfrole=" + cfrole + ", cfstatus=" + cfstatus + ", cfname=" + cfname + "]";
	}
	
}
